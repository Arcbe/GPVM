/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gpvm.render;

import taiga.code.graphics.Renderable;

/**
 * Base class for sky boxes.  There will be one sky box per world.
 * @author russell
 */
public abstract class SkyBoxRenderer extends Renderable {

  public SkyBoxRenderer(String name) {
    super(name);
    
    setPasses(sky_pass);
  }
  
  protected final void renderSelf(int pass) {
    if(pass == sky_pass) renderSky();
  }
  
  /**
   * 
   */
  protected void updateSelf() {}
  
  /**
   * Renders the sky for a given world.
   */
  protected abstract void renderSky();
  
  /**
   * This is the pass that the sky should be rendered on.  The default is 0.
   */
  public static int sky_pass = 1;
}
