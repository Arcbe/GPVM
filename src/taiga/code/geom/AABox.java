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

package taiga.code.geom;

import java.util.logging.Logger;
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Represents an axis-aligned 3d rectangular prism with the z axis as the
 * vertical axis, and the y axis as the axis from front to back.
 * 
 * @author russell
 */
public final class AABox implements BoundingVolume {

  /**
   * Creates a 1x1x1 {@link AABox} with a corner at the origin.
   */
  public AABox() {
    blfcorner = new Vector3f();
    trbcorner = new Vector3f(1, 1, 1);
  }

  /**
   * Creates a new {@link AABox} with the smallest size and the given points as corners.
   * 
   * @param p1 The first corner for the box, this can be any corner.
   * @param p2 Te second corner for the box, this can be any other corner.
   */
  public AABox(Vector3f p1, Vector3f p2) {
    blfcorner = new Vector3f(
      Math.min(p1.x, p2.x),
      Math.min(p1.y, p2.y),
      Math.min(p1.z, p2.z));
   
    trbcorner = new Vector3f(
      Math.max(p1.x, p2.x),
      Math.max(p1.y, p2.y),
      Math.max(p1.z, p2.z));
  }
  
  /**
   * Returns the size of the {@link AABox} in the z direction.
   * 
   * @return The height of the {@link AABox}.
   */
  public float getHeight() {
    return trbcorner.z - blfcorner.z;
  }

  @Override
  public AABox getBounds() {
    return this;
  }

  /**
   * Returns the {@link Vector3f} foe the center of this {@link AABox}.
   * 
   * @return The center of the {@link AABox}.
   */
  public ReadableVector3f getCenter() {
    return new Vector3f(
      (blfcorner.x + trbcorner.x) / 2,
      (blfcorner.y + trbcorner.y) / 2,
      (blfcorner.z + trbcorner.z) / 2);
  }
  
  /**
   * Checks whether this {@link AABox} intersects with the given {@link AABox}.
   * 
   * @param box The {@link AABox} to check for intersections.
   * @return Whether the two {@link AABox}es intersect.
   */
  public boolean collides(AABox box) {
    return 
      blfcorner.x <= box.trbcorner.x &&
      box.blfcorner.x <= trbcorner.x &&
      blfcorner.y <= box.trbcorner.y &&
      box.blfcorner.y <= trbcorner.y &&
      blfcorner.z <= box.trbcorner.z &&
      box.blfcorner.z <= trbcorner.z;
  }
  
  /**
   * Returns the coordinate of the top plane of the {@link AABox}.
   * 
   * @return The top of this {@link AABox}.
   */
  public float top() {
    return trbcorner.z;
  }
  
  /**
   * Returns the coordinate of the right plane of the {@link AABox}.
   * 
   * @return The right of this {@link AABox}.
   */
  public float right() {
    return trbcorner.x;
  }
  
  /**
   * Returns the coordinate of the top plane of the {@link AABox}.
   * 
   * @return The top of this {@link AABox}.
   */
  public float back() {
    return trbcorner.y;
  }
  
  /**
   * Returns the coordinate of the left plane of the {@link AABox}.
   * 
   * @return The left of this {@link AABox}.
   */
  public float left() {
    return blfcorner.x;
  }
  
  /**
   * Returns the coordinate of the front plane of the {@link AABox}.
   * 
   * @return The front of this {@link AABox}.
   */
  public float front() {
    return blfcorner.y;
  }
  
  /**
   * Returns the coordinate of the bottom plane of the {@link AABox}.
   * 
   * @return The bottom of this {@link AABox}.
   */
  public float bottom() {
    return blfcorner.z;
  }
  
  private final Vector3f blfcorner;
  private final Vector3f trbcorner;
  
  private static final String locprefix = AABox.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
