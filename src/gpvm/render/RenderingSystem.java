/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.render;

import gpvm.map.GameMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import gpvm.util.Settings;

/**
 * Maintains a screen and controls the rendering for the entire game. 
 * Input is also dependent on this system as the input comes through the window.
 * Only a single Opengl context is supported with a single thread interacting 
 * with it.
 * 
 * @author russell
 */
public class RenderingSystem {
  /**
   * Creates a new Rendering system using the given mode.
   * 
   * @param mode The display mode for the window that will be created.
   */
  public static void createSystem(DisplayMode mode) {
    if(instance != null) {
      instance.destroy();
    }
    
    instance = new RenderingSystem(mode);
    instance.start();
  }
  
  /**
   * Returns a reference to the current rendering system.  If no rendering
   * system has been created then this will simply return null.
   * 
   * @return A instance of a rendering system or null
   */
  public static RenderingSystem getInstance() {
    return instance;
  }

  
  private RenderingSystem(DisplayMode mode) {
    this.mode = mode;
    renderer = new MapRenderer(VertexArrayBatch.class);
    cam = new Camera();
    rendrunner = new Runner();
    renderingthread = new Thread(rendrunner);
    renderingthread.setName("Rendering System");
  }
  
  /**
   * Causes the rendering thread to start.  If the thread is already started
   * then this function has no effect.  Once destroyed the rendering system
   * cannot be restarted with this method.
   */
  public void start() {
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

  public void setMap(GameMap map) {
    
  }
  
  private Runner rendrunner;
  private Thread renderingthread;
  private DisplayMode mode;
  private Camera cam;
  private MapRenderer renderer;
  private GameMap map;
  
  private void render() {
    //clear color is magenta to indicate a problem. The clear color should not
    //be visible
    GL11.glClearColor(1, 0, 1, 1);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    
    //setup the camera
    cam.loadCamera();
    
    //now draw the map
    renderer.renderGrid(true);
    renderer.render(cam);
  }
  
  private static RenderingSystem instance;
  
  //inner class used for rendering thread.
  private class Runner implements Runnable {
    public boolean pause = false;
    public boolean running = true;
    
    @Override
    public void run() {
      try {
        //initialize the display
        Display.setDisplayModeAndFullscreen(mode);
        Display.create();
        
        //start doig the rendering
        while(running && !Display.isCloseRequested()) {
          if(!pause)
            render();
          
          Display.update();
        }
      } catch (LWJGLException ex) {
        Logger.getLogger(RenderingSystem.class.getName()).log(Level.SEVERE, Settings.getLocalString("err_rendering_init"), ex);
      } finally {
        Display.destroy();
        running = false;
      }
    }
  }
}
