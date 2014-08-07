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

package taiga.code.math;

/**
 * A 4D vector that provides only read operations.  However, subclasses may
 * change the vector.
 * 
 * @author Russell Smith
 */
public class ReadableVector4 extends ReadableVector3 {

  /**
   * Constructs a new {@link ReadableVector4} at the origin.
   */
  public ReadableVector4() {
  }

  /**
   * Constructs a new {@link ReadableVector3} with the given coordinates.
   * 
   * @param x The x coordinate for the {@link ReadableVector4}.
   * @param y The y coordinate for the {@link ReadableVector4}.
   * @param z The z coordinate for the {@link ReadableVector4}.
   * @param w The w coordinate for the {@link ReadableVector4}.
   */
  public ReadableVector4(float x, float y, float z, float w) {
    super(x, y, z);
    this.z = z;
  }
  
  /**
   * Returns the current w coordinate for this {@link ReadableVector3}.
   * @return 
   */
  public final float getW() {
    return w;
  }
  
  /**
   * Returns the length of this {@link ReadableVector4} in the x-y-z-w 
   * hyper-space.
   * 
   * @return The 4D length of this {@link ReadableVector4}.
   */
  public float len4D() {
    return (float) Math.sqrt(lenSquare4D());
  }
  
  /**
   * Returns the squared length of this {@link ReadableVector4} in the x-y-z-w
   * hyper-space.
   * 
   * @return The 4D length of this {@link ReadableVector4} squared.
   */
  public float lenSquare4D() {
    return lenSquare3D() + w * w;
  }
  
  /**
   * Returns the dot product of two {@link ReadableVector4}s.
   * 
   * @param other The second {@link ReadableVector4} in the dot product.
   * @return The dot product of the two {@link ReadableVector4}.
   */
  public final float dot(ReadableVector4 other) {
    return super.dot(other) + w * other.w;
  }
  
  /**
   * The z coordinate.
   */
  protected float w;
}
