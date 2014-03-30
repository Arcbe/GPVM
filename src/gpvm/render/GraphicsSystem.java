/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.render;

import static gpvm.HardcodedValues.GRAPHICSSYSTEM_NAME;
import gpvm.ThreadingManager;
import gpvm.input.InputSystem;
import gpvm.map.GameMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import gpvm.util.StringManager;
import gpvm.util.Updateable;
import java.util.ArrayList;
import java.util.List;
import taiga.code.registration.RegisteredSystem;

/**
 * Maintains a screen and controls the rendering for the entire game. 
 * Input is also dependent on this system as the input comes through the window.
 * Only a single Opengl context is supported with a single thread interacting 
 * with it.
 * 
 * @author russell
 */
public class GraphicsSystem extends RegisteredSystem {
  /**
   * Creates a new Rendering system using the given mode.
   * 
   * @param mode The display mode for the window that will be created.
   */
  public static void createSystem(DisplayMode mode) {
    if(instance != null) {
      instance.destroy();
    }
    
    instance = new GraphicsSystem(mode);
    instance.start();
  }
  
  /**
   * Returns a reference to the current rendering system.  If no rendering
   * system has been created then this will simply return null.
   * 
   * @return A instance of a rendering system or null
   */
  public static GraphicsSystem getInstance() {
    return instance;
  }
  
  /**
   * Causes the rendering thread to start.  If the thread is already started
   * then this function has no effect.  Once destroyed the rendering system
   * cannot be restarted with this method.
   */
  @Override
  public void startSystem() {
    assert renderingthread != null;
    assert rendrunner != null;
    if(!renderingthread.isAlive())
      renderingthread.start();
    else
      rendrunner.pause = false;
  }
  
  /**
   * causes the current thread to wait until the rendering system closes.
   */
  public void waitForClose() {
    while(renderingthread.isAlive()) {
      try {
        renderingthread.join();
      } catch (InterruptedException ex) {
        //ignore interrupts
      }
    }
  }
  
  /**
   * Stops the rendering system and releases all assets stored within.
   */
  public void destroy() {
    //signal the rendering thread to shutdown and wait for it to die.
    rendrunner.running = false;
    while(renderingthread.isAlive()) {
      try {
        renderingthread.join();
      } catch (InterruptedException ex) {
        //ignore interrupts
      }
    }
    
    rendrunner = null;
    renderingthread = null;
  }

  /**
   * Sets the {@link GameMap} that the rendering system is currently
   * rendering.
   * 
   * @param map The new {@link GameMap} for the rendering system.
   */
  public void setMap(GameMap map) {
    //TODO: this should not be hard coded.
    if(renderer == null) renderer = new MapData();
    MapRenderer rend = new MapRenderer(VertexArrayBatch.class);
    rend.setMap(map);
    this.map = map;
    renderer.map = rend;
  }
  
  public void setSkyBox(SkyBoxRenderer sky) {
    if(renderer == null) renderer = new MapData();
    renderer.sky = sky;
  }
  
  /**
   * Sets the {@link Camera} that will be used to create the projection matrix
   * for rendering.
   * 
   * @param camera The {@link Camera} for the rendering system to use.
   */
  public void setCamera(Camera camera) {
    cam = camera;
  }
  
  /**
   * Returns the camera that is currently in use by the {@link GraphicsSystem}.
   * 
   * @return The current {@link Camera}
   */
  public Camera getCamera() {
    return cam;
  }
  
  public boolean isPaused() {
    assert rendrunner != null;
    
    return rendrunner.pause;
  }
  
  public void addUpdater(Updateable up) {
    updaters.add(up);
  }
  
  private Runner rendrunner;
  private Thread renderingthread;
  private DisplayMode mode;
  private Camera cam;
  private MapData renderer;
  private GameMap map;
  private List<Updateable> updaters;

  private GraphicsSystem(DisplayMode mode) {
    super(GRAPHICSSYSTEM_NAME);
    
    this.mode = mode;
    cam = new Camera();
    rendrunner = new Runner();
    updaters = new ArrayList<>();
    
    renderingthread = new Thread(rendrunner);
    renderingthread.setName(GRAPHICSSYSTEM_NAME);
  }
  
  private void pumpUpdaters() {
    for(Updateable up : updaters)
      up.Update();
  }
  
  private void render() {
    //if there is no map there is nothing to render.
    if(renderer == null) return;
    
    //clear color is magenta to indicate a problem. The clear color should not
    //be visible
    GL11.glClearColor(1, 0, 1, 1);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    
    //setup the camera
    cam.loadCamera();
    
    //update the rendering info if needed
    ThreadingManager.getInstance().requestRead();
    try {
      renderer.map.update(cam);
    } finally {
      ThreadingManager.getInstance().releaseRead();
    }
    
    //now draw the map
    renderer.map.renderGrid(false);
    renderer.map.render(cam);
  }
  
  private static GraphicsSystem instance;

  @Override
  protected void resetObject() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected void stopSystem() {
    rendrunner.pause = true;
  }
  
  //inner class used for rendering thread.
  private class Runner implements Runnable {
    public boolean pause = false;
    public boolean running = true;
    
    @Override
    public synchronized void run() {
      try {
        //initialize the display
        Display.setDisplayModeAndFullscreen(mode);
        Display.setVSyncEnabled(true);
        Display.create();
        
        //start doig the rendering
        while(running && !Display.isCloseRequested()) {
          if(!pause) {
            InputSystem.getInstance().pump();
            pumpUpdaters();
            
            render();
          }
          
          
          Display.update();
          
          try {
            wait(16);
          } catch (InterruptedException ex) {
            Logger.getLogger(GraphicsSystem.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      } catch (LWJGLException ex) {
        Logger.getLogger(GraphicsSystem.class.getName()).log(Level.SEVERE, StringManager.getLocalString("err_rendering_init"), ex);
      } finally {
        Display.destroy();
        running = false;
      }
    }
  }
  
  private class MapData {
    SkyBoxRenderer sky;
    MapRenderer map;
  }
}
