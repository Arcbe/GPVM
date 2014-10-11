/*
 * Copyright (C) 2014 Russell Smith.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
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
