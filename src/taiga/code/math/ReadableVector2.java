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
 * A 2d vector that provides only read operations.  However, subclasses
 * may change the coordinates.
 * 
 * @author Russell Smith
 */
public class ReadableVector2 implements Cloneable {
  /**
   * Constructs a {@link ReadableVector2} set to the origin.
   */
  public ReadableVector2() {
    x = 0;
    y = 0;
  }
  
  /**
   * Constructs a {@link ReadableVector2} with the given coordinates.
   * 
   * @param x The x coordinate to use.
   * @param y The y coordinate to use.
   */
  public ReadableVector2(float x, float y) {
    this.x = x;
    this.y = y;
  }
  
  public ReadableVector2(ReadableVector2 other) {
    this.x = other.x;
    this.y = other.y;
  }
  
  /**
   * Returns the current x coordinate of this {@link ReadableVector2}.
   * 
   * @return The current x coordinate.
   */
  public final float getX() {
    return x;
  }
  
  /**
   * Returns the current y coordinate of this {@link ReadableVector2}.
   * 
   * @return The current y coordinate.
   */
  public final float getY() {
    return y;
  }
  
  /**
   * Returns the length of this {@link ReadableVector2} in the x-y plane.
   * 
   * @return The 2D length.
   */
  public final float len2D() {
    return (float) Math.sqrt(lenSquared2D());
  }
  
  /**
   * Returns the length squared of this {@link ReadableVector2} in the
   * x-y plane.
   * 
   * @return The 2D length squared.
   */
  public final float lenSquared2D() {
    return x * x + y * y;
  }
  
  /**
   * Returns the dot product of two {@link ReadableVector2}s.
   * 
   * @param other The second {@link ReadableVector2} in the dot product.
   * @return The dot product of the two {@link ReadableVector2}.
   */
  public final float dot(ReadableVector2 other) {
    return x * other.x + y * other.y;
  }
  
  /**
   * Returns a clone of this {@link ReadableVector2}, or a subclass.
   * 
   * @param <T> The class of the current {@link ReadableVector2}.
   * @return A clone of this {@link ReadableVector2}.
   */
  public <T extends ReadableVector2> T getClone() {
    try {
      return (T) clone();
    } catch (CloneNotSupportedException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  /**
   * The x coordinate.
   */
  protected float x;
  /**
   * The y coordinate.
   */
  protected float y;
  
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("<");
    result.append(x);
    result.append(", ");
    result.append(y);
    result.append(">");
    
    return result.toString();
  }
}
