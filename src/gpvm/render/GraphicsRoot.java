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
import taiga.code.opengl.GraphicsSystem;

/**
 * Maintains a screen and controls the rendering for the entire game. 
 * Input is also dependent on this system as the input comes through the window.
 * Only a single Opengl context is supported with a single thread interacting 
 * with it.
 * 
 * @author russell
 */
public class GraphicsRoot extends GraphicsSystem {

  /**
   * Sets the {@link GameMap} that the rendering system is currently
   * rendering.
   * 
   * @param map The new {@link GameMap} for the rendering system.
   */
//  public void setMap(GameMap map) {
//    //TODO: this should not be hard coded.
//    if(renderer == null) renderer = new MapData();
//    MapRenderer rend = new MapRenderer(VertexArrayBatch.class);
//    rend.setMap(map);
//    this.map = map;
//    renderer.map = rend;
//  }
//  
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
   * Returns the camera that is currently in use by the {@link GraphicsRoot}.
   * 
   * @return The current {@link Camera}
   */
  public Camera getCamera() {
    return cam;
  }
  
  public void addUpdater(Updateable up) {
    updaters.add(up);
  }
  
  private DisplayMode mode;
  private Camera cam;
//  private MapData renderer;
  private GameMap map;
  private List<Updateable> updaters;

  public GraphicsRoot() {
    super(GRAPHICSSYSTEM_NAME);
    
    this.mode = mode;
    cam = new Camera();
    updaters = new ArrayList<>();
  }
  
  private void pumpUpdaters() {
    for(Updateable up : updaters)
      up.Update();
  }
//  
//  private void render() {
//    //if there is no map there is nothing to render.
//    if(renderer == null) return;
//    
//    //clear color is magenta to indicate a problem. The clear color should not
//    //be visible
//    GL11.glClearColor(1, 0, 1, 1);
//    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
//    GL11.glEnable(GL11.GL_DEPTH_TEST);
//    
//    //setup the camera
//    cam.loadCamera();
//    
//    //update the rendering info if needed
//    ThreadingManager.getInstance().requestRead();
//    try {
//      renderer.map.update(cam);
//    } finally {
//      ThreadingManager.getInstance().releaseRead();
//    }
//    
//    //now draw the map
//    renderer.map.renderGrid(false);
//    renderer.map.render(cam);
//  }
  
  private static GraphicsRoot instance;

  @Override
  protected void resetObject() {
  }

  @Override
  protected void rendering() {
  }
}
