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

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Represents a direction and magnitude as a point in 4d space.
 * 
 * @author Russell Smith
 */
public final class Vector4 extends ReadableVector4 implements Serializable {
  /**
   * Creates a new {@link Vector4}.
   */
  public Vector4() {
  }
  
  /**
   * Creates a new {@link Vector4} using the given array for its values.
   * 
   * @param x The x component of this {@link Vector4}.
   * @param y The y component of this {@link Vector4}.
   * @param z The z component of this {@link Vector4}.
   * @param w The w component of this {@link Vector4}.
   */
  public Vector4(float x, float y, float z, float w) {
    super(x, y, z, w);
  }
  
  /**
   * Returns the length of this {@link Vector4} squared.
   * 
   * @return The squared length.
   */
  public float lengthSquare() {
    return dot(this);
  }
  
  /**
   * Returns the length of this {@link Vector4}.
   * 
   * @return The length.
   */
  public float length() {
    return (float) Math.sqrt(lengthSquare());
  }
  
  /**
   * Returns the component of this {@link Vector4} in the same direction
   * as the given {@link Vector4}.
   * 
   * @param other The {@link Vector4} to project this one onto.
   * @return A reference to the out parameter.
   */
  public Vector4 project(Vector4 other) {
    return project(other, this);
  }
  
  /**
   * Calculates the component of this {@link Vector4} in the same direction
   * as the given {@link Vector4} and stores the result in a separate
   * {@link Vector4}.
   * 
   * @param other The {@link Vector4} to project this one onto.
   * @param out The {@link Vector4} to store the result in.
   * @return A reference to the out parameter.
   */
  public Vector4 project(Vector4 other, Vector4 out) {
    return other.scale(
      dot(other) / other.lengthSquare(),
      out);
  }
  
  /**
   * Scales this {@link Vector4} by the given amount.
   * 
   * @param factor The amount to scale by.
   * @return A reference to this {@link Vector4}.
   */
  public Vector4 scale(float factor) {
    return scale(factor, this);
  }
  
  /**
   * Scales this {@link Vector4} by the given amount.
   * 
   * @param factor The amount to scale by.
   * @return A reference to this {@link Vector4}.
   */
  public Vector4 scale(float factor, Vector4 out) {
    out.x = x * factor;
    out.y = y * factor;
    out.z = z * factor;
    out.w = w * factor;
    
    return out;
  }
  
  /**
   * Subtracts the given {@link Vector4} to this {@link Vector4}.
   * 
   * @param other The {@link Vector4} to subtract.
   * @return A reference to this {@link Vector4}.
   */
  public Vector4 sub(Vector4 other) {
    return add(other, this);
  }
  
  /**
   * Subtracts the given {@link Vector4} to this {@link Vector4} and puts the
   * result in a separate {@link Vector4}.
   * 
   * @param other The {@link Vector4} to subtract.
   * @param out The {@link Vector4} to store the result in.
   * @return A reference to the out parameter.
   */
  public Vector4 sub(Vector4 other, Vector4 out) {
    out.x = x - other.x;
    out.y = y - other.y;
    out.z = z - other.z;
    out.w = w - other.w;
    
    return out;
  }
  
  /**
   * Adds the given {@link Vector4} to this {@link Vector4}.
   * 
   * @param other The {@link Vector4} to add.
   * @return A reference to this {@link Vector4}.
   */
  public Vector4 add(Vector4 other) {
    return add(other, this);
  }
  
  /**
   * Adds the given {@link Vector4} to this {@link Vector4} and puts the
   * result in a separate {@link Vector4}.
   * 
   * @param other The {@link Vector4} to add.
   * @param out The {@link Vector4} to store the result in.
   * @return A reference to the out parameter.
   */
  public Vector4 add(Vector4 other, Vector4 out) {
    out.x = x + other.x;
    out.y = y + other.y;
    out.z = z + other.z;
    out.w = w + other.w;
    
    return out;
  }
  
  /**
   * Loads values for this {@link Vector4} from the given {@link ByteBuffer}.
   * 
   * @param buffer The {@link ByteBuffer} to load values from.
   */
  public void load(ByteBuffer buffer) {
    load(buffer.asFloatBuffer());
  }
  
  /**
   * Loads values for this {@link Vector4} from the given {@link FloatBuffer}.
   * 
   * @param buffer The {@link FloatBuffer} to load values from.
   */
  public void load(FloatBuffer buffer) {
    x = buffer.get();
    y = buffer.get();
    z = buffer.get();
    w = buffer.get();
  }
}
