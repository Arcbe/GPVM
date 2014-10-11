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

import org.lwjgl.opengl.Display;
import taiga.code.math.Matrix4;
import taiga.code.math.Matrix4Utils;
import taiga.code.math.Vector3;
import taiga.code.math.Vector4;
import taiga.code.opengl.PerspectiveCamera;

/**
 * A simple camera that just stores the acts as a storage for the various
 * rendering parameters.
 * 
 * @author russell
 */
public class StationaryCamera extends PerspectiveCamera {

  /**
   * The up direction of the {@link StationaryCamera}.
   */
  public final Vector3 up;
  /**
   * The direction the {@link StationaryCamera} is looking.
   */
  public final Vector3 direction;
  /**
   * The position of the {@link StationaryCamera}.
   */
  public final Vector3 position;

  /**
   * Creates a new {@link StationaryCamera} with a pi/3 viewing angle and
   * 1 and 100 for the near and far planes respectively.
   */
  public StationaryCamera() {
    setFOV((float) Math.PI / 3);
    setFar(1);
    setFar(100);
    setAspect((float) Display.getWidth() / (float) Display.getHeight());
    
    up = new Vector3();
    direction = new Vector3();
    position = new Vector3();
    view = new Matrix4();
  }

  /**
   * Creates a new {@link StationaryCamera} from the given values.
   * 
   * @param up The up direction.
   * @param direction The direction the {@link StationaryCamera} is looking.
   * @param position The position of the {@link StationaryCamera}.
   * @param fov The field of view
   * @param near The near plane of the viewing frustum.
   * @param far The far plane of the viewing frustum.
   */
  public StationaryCamera(Vector3 up, Vector3 direction, Vector3 position, float fov, float near, float far) {
    super(fov, near, far, (float) Display.getWidth() / (float) Display.getHeight());
    
    this.up = up;
    this.direction = direction;
    this.position = position;
    view = new Matrix4();
  }
  
  public void recenterOnScreenPoint(float x, float y) {
    Vector4 vec = screenPointToEyeVector(x, y);
    //vec.scale(1f / vec.getW());
    Vector3 result = new Vector3();
    
    Matrix4 view = getViewMatrix();
    result.setX(
      vec.getX() * view.getValue(0, 0) + 
      vec.getY() * view.getValue(1, 0) +
      vec.getZ() * view.getValue(2, 0));
    result.setY(
      vec.getX() * view.getValue(0, 1) + 
      vec.getY() * view.getValue(1, 1) +
      vec.getZ() * view.getValue(2, 1));
    result.setZ(
      vec.getX() * view.getValue(0, 2) + 
      vec.getY() * view.getValue(1, 2) +
      vec.getZ() * view.getValue(2, 2));
    
    direction.add(result);
    direction.scale(.5f);
  }

  @Override
  public Matrix4 getViewMatrix() {
    return Matrix4Utils.lookAt(position, up, direction, view);
  }
  
  private final Matrix4 view;
}
