/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm.render;

import static gpvm.HardcodedValues.GRAPHICSSYSTEM_NAME;
import gpvm.map.GameMap;
import org.lwjgl.opengl.DisplayMode;
import gpvm.util.Updateable;
import java.util.List;
import taiga.code.opengl.GraphicsSystem;
import taiga.code.registration.ChildListener;
import taiga.code.registration.RegisteredObject;

/**
 * Maintains a screen and controls the rendering for the entire game. 
 * Input is also dependent on this system as the input comes through the window.
 * Only a single Opengl context is supported with a single thread interacting 
 * with it.
 * 
 * @author russell
 */
public class GraphicsRoot extends GraphicsSystem implements ChildListener {
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
  
  private DisplayMode mode;
  private Camera cam;
//  private MapData renderer;
  private GameMap map;
  private List<Updateable> updaters;

  public GraphicsRoot() {
    super(GRAPHICSSYSTEM_NAME);
    
    this.mode = mode;
    cam = new Camera();
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

  @Override
  protected void resetObject() {
  }

  @Override
  protected void rendering() {
  }

  @Override
  public void childAdded(RegisteredObject parent, RegisteredObject child) {
  }

  @Override
  public void childRemoved(RegisteredObject parent, RegisteredObject child) {
    
  }
}
