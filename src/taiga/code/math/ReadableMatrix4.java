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

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

/**
 * A {@link ReadableMatrix4} with only methods that can read the state.
 * This does not mean that this is immutable since subclasses are able to change
 * the state.
 * 
 * @author Russell Smith
 */
public class ReadableMatrix4 {
  
  /**
   * The values stored in this {@link Matrix4}.  The first index is the row
   * and the second is the column index.
   */
  protected final float[][] values;

  /**
   * Creates a new identity {@link Matrix4}.
   */
  public ReadableMatrix4() {
    this.values = new float[4][4];
    
    values[0][0] = 1;
    values[1][1] = 1;
    values[2][2] = 1;
    values[3][3] = 1;
  }

  /**
   * Creates a new {@link ReadableMatrix4} using the given array for
   * storing values.
   * 
   * @param values The array to store values in.
   */
  public ReadableMatrix4(float[][] values) {
    assert values.length >= 4 || 
      values[0].length >= 4 ||
      values[1].length >= 4 ||
      values[2].length >= 4 ||
      values[3].length >= 4;
    
    this.values = values;
  }
  
  /**
   * Transforms the given {@link ReadableVector4} with this {@link Matrix4}.
   * The {@link ReadableVector4} is pre multiplied as a column vectored by this
   * {@link Matrix4}.
   * 
   * @param vec The {@link ReadableVector4} to transform.
   * @param out The {@link Vector4} to place the result.
   * @return A reference to the out parameter.
   */
  public Vector4 transform(ReadableVector4 vec, Vector4 out) {
    float x = 
      values[0][0] * vec.getX() +
      values[0][1] * vec.getY() +
      values[0][2] * vec.getZ() +
      values[0][3] * vec.getW();
    float y =
      values[1][0] * vec.getX() +
      values[1][1] * vec.getY() +
      values[1][2] * vec.getZ() +
      values[1][3] * vec.getW();
    float z = 
      values[2][0] * vec.getX() +
      values[2][1] * vec.getY() +
      values[2][2] * vec.getZ() +
      values[2][3] * vec.getW();
    float w =
      values[3][0] * vec.getX() +
      values[3][1] * vec.getY() +
      values[3][2] * vec.getZ() +
      values[3][3] * vec.getW();
    
    out.x = x;
    out.y = y;
    out.z = z;
    out.w = w;
    return out;
  }
  
  /**
   * Returns the value of a specific cell.
   * 
   * @param row The row of the cell.
   * @param col The column of the cell.
   * @return The current value of the cell.
   */
  public float getValue(int row, int col) {
    return values[row][col];
  }
  
  /**
   * Calculates the determinant of this {@link Matrix4}.
   * 
   * @return The determinant.
   */
  public final float determinant() {
    float result = 0;
    
    for(int i = 0; i < 3; i++) {
      for(int j = i + 1; j < 4; j++) {
        float pp = 0;
        float pn = 0;
        float np = 0;
        float nn = 0;
        boolean first = true;
        
        for(int k = 0; k < 4; k++) {
          if(k == i) {
            pp = values[0][k];
            pn = values[1][k];
          } else if(k == j) {
            pp *= values[1][k];
            pn *= values[0][k];
          } else if(first) {
            first = false;
            
            np = values[2][k];
            nn = values[3][k];
          } else {
            np *= values[3][k];
            nn *= values[2][k];
          }
        }
        
        if(j % 2 == 1)
          result += (pp - pn) * (np - nn);
        else
          result -= (pp - pn) * (np - nn);
      }
      
      result = -result;
    }
    
    return -result;
  }
  
  /**
   * Stores this {@link Matrix4} in row major order in the given {@link ByteBuffer}.
   * The buffer is not reset or otherwise changed other than storing the values.
   * 
   * @param buffer The {@link ByteBuffer} to store this {@link Matrix4} in.
   * @return A reference to the given {@link ByteBuffer}.
   */
  public final ByteBuffer store(ByteBuffer buffer) {
    store(buffer.asFloatBuffer());
    
    return buffer;
  }
  
  /**
   * Stores this {@link Matrix4} in row major order in the given {@link FloatBuffer}.
   * The buffer is not reset or otherwise changed other than storing the values.
   * 
   * @param buffer The {@link FloatBuffer} to store this {@link Matrix4} in.
   * 
   * @return A reference to the given {@link FloatBuffer}.
   */
  public final FloatBuffer store(FloatBuffer buffer) {
    return store(buffer, true);
  }
  
  /**
   * Stores this {@link Matrix4} in the given {@link FloatBuffer}.
   * The buffer is not reset or otherwise changed other than storing the values.
   * 
   * @param buffer The {@link FloatBuffer} to store this {@link Matrix4} in.
   * @param row Selects whether the {@link Matrix4} is stored as row major or column order.
   * @return A reference to the given {@link FloatBuffer}.
   */
  public final FloatBuffer store(FloatBuffer buffer, boolean row) {
    if(row)
      for(int i = 0; i < 4; i++)
        buffer.put(values[i]);
    else
      for(int j = 0; j < 4; j++)
        for(int i = 0; i < 4; i++)
          buffer.put(values[i][j]);
    
    return buffer;
  }
  
  @Override
  public boolean equals(Object other) {
    if(!(other instanceof ReadableMatrix4)) return false;
    
    ReadableMatrix4 mat = (ReadableMatrix4) other;
    return Arrays.deepEquals(values, mat.values);
  }
  
  @Override
  public String toString() {
    return 
      Arrays.toString(values[0]) + 
      Arrays.toString(values[1]) + 
      Arrays.toString(values[2]) + 
      Arrays.toString(values[3]);
  }
}
