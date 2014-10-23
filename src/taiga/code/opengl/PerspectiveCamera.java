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

package taiga.code.opengl;

import org.lwjgl.opengl.Display;
import taiga.code.math.Matrix4;
import taiga.code.math.Matrix4Utils;
import taiga.code.math.Vector4;

public abstract class PerspectiveCamera implements Camera {

  public PerspectiveCamera() {
    if(Display.isVisible()) {
      aspect = (float) Display.getWidth() / (float) Display.getHeight();
    }
  }

  public PerspectiveCamera(float fov, float near, float far, float aspect) {
    this.fov = fov;
    this.near = near;
    this.far = far;
    this.aspect = aspect;
  }
  
  public void setFar(float f) {
    far = f;
    proj = null;
  }
  
  public void setNear(float n) {
    near = n;
    proj = null;
  }
  
  public void setAspect(float asp) {
    aspect = asp;
    proj = null;
  }
  
  public void setFOV(float field) {
    fov = field;
    proj = null;
  }
  
  /**
   * Converts the given screen coordinate into the vector for the closest
   * possible eye coordinate that would map to the given screen coordinate.
   * 
   * @param x The x component of the screen coordinate.
   * @param y The y component of the screen coordinate.
   * @return The eye space vector for the given screen coordinate.
   */
  public Vector4 screenPointToEyeVector(float x, float y) {
    if(x > 1)
      x /= (float) Display.getWidth();
    if(y > 1)
      y /= (float) Display.getHeight();
    
    Vector4 out = new Vector4(x, y, 1, 1);
    Matrix4 invtrans = getProjection().invert(new Matrix4());
    invtrans.transform(out, out);
    return out;
  }
  
  public Vector4 screenPointToWorld(float x, float y, float z) {
    Vector4 out = new Vector4(x, y, z, 1);
    Matrix4 invtrans = new Matrix4(getProjection());
    invtrans.mul(getViewMatrix()).invert(invtrans);
    invtrans.transform(out, out);
    return out;
  }
  
  @Override
  public Matrix4 getProjection() {
    if(Float.isInfinite(aspect) || Float.isNaN(aspect)) {
      aspect = (float) Display.getWidth() / (float) Display.getHeight();
    }
    
    if(proj == null) {
      proj = Matrix4Utils.perspective(fov, aspect, near, far, new Matrix4());
    }
      
    return proj;
  }
  
  private float fov;
  private float near;
  private float far;
  private float aspect;
  private Matrix4 proj;
}
