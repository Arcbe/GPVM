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
   * The z coordinate.
   */
  protected float z;
}
