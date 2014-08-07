/*
 * Copyright (C) 2014 Russell Smith
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package taiga.code.opengl;

import org.lwjgl.opengl.Display;
import taiga.code.math.Matrix4;
import taiga.code.math.Matrix4Utils;

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
  
  @Override
  public Matrix4 getProjection() {
    if(proj == null) {
      proj = Matrix4Utils.perspective(fov, aspect, near, far, proj);
    }
      
    return proj;
  }
  
  private float fov;
  private float near;
  private float far;
  private float aspect;
  private Matrix4 proj;
}
