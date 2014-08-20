/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.render;

import taiga.code.math.Matrix4;
import taiga.code.opengl.Renderable;
import taiga.gpvm.HardcodedValues;

/**
 * Base class for sky boxes.  There will be one sky box per world.
 * @author russell
 */
public abstract class SkyBoxRenderer extends Renderable {

  /**
   * Creates a new {@link SkyBoxRenderer} with the given name.
   * @param name 
   */
  public SkyBoxRenderer(String name) {
    super(name);
    
    setPasses(HardcodedValues.SKY_LAYER);
  }
  
  @Override
  protected final void renderSelf(int pass, Matrix4 proj) {
    if(pass == HardcodedValues.SKY_LAYER) renderSky(proj);
  }
  
  @Override
  protected void updateRenderable() {}
  
  /**
   * Renders the sky for a given world.  This will only be called on the first
   * rendering pass.
   * 
   * @param proj The projection matrix that will be used for the scene.
   */
  protected abstract void renderSky(Matrix4 proj);
}
