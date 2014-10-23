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

package taiga.code.math;

/**
 * A 3D vector that provides only read operations.  However, subclasses may
 * change the vector.
 * 
 * @author Russell Smith
 */
public class ReadableVector3 extends ReadableVector2 {

  /**
   * Constructs a new {@link ReadableVector3} at the origin.
   */
  public ReadableVector3() {
  }

  /**
   * Constructs a new {@link ReadableVector3} with the given coordinates.
   * 
   * @param x The x coordinate for the {@link ReadableVector3}.
   * @param y The y coordinate for the {@link ReadableVector3}.
   * @param z The z coordinate for the {@link ReadableVector3}.
   */
  public ReadableVector3(float x, float y, float z) {
    super(x, y);
    this.z = z;
  }
  
  public ReadableVector3(ReadableVector3 other) {
    super(other);
    this.z = other.z;
  }
  
  /**
   * Returns the current z coordinate for this {@link ReadableVector3}.
   * @return 
   */
  public final float getZ() {
    return z;
  }
  
  /**
   * Returns the length of this {@link ReadableVector3} in the x-y-z space.
   * 
   * @return The 3D length of this {@link ReadableVector3}.
   */
  public final float len3D() {
    return (float) Math.sqrt(lenSquare3D());
  }
  
  /**
   * Returns the squared length of this {@link ReadableVector3} in the x-y-z space.
   * 
   * @return The 3D length of this {@link ReadableVector3} squared.
   */
  public final float lenSquare3D() {
    return lenSquared2D() + z * z;
  }
  
  /**
   * Returns the dot product of two {@link ReadableVector3}s.
   * 
   * @param other The second {@link ReadableVector3} in the dot product.
   * @return The dot product of the two {@link ReadableVector3}.
   */
  public final float dot(ReadableVector3 other) {
    return super.dot(other) + z * other.z;
  }
  
  /**
   * The z coordinate.
   */
  protected float z;
  
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("<");
    result.append(x);
    result.append(", ");
    result.append(y);
    result.append(", ");
    result.append(z);
    result.append(">");
    
    return result.toString();
  }
}
