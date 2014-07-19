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

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * A 4x4 matrix row major matrix, meaning the first index is the row, and
 * the second is the column index.
 * 
 * @author Russell Smith
 */
public final class Matrix4 implements Serializable {
  /**
   * The values stored in this {@link Matrix4}.  The first index is the row
   * and the second is the column index.
   */
  public final float[][] values;

  /**
   * Creates a new identity {@link Matrix4}.
   */
  public Matrix4() {
    this.values = new float[4][4];
    
    values[0][0] = 1;
    values[1][1] = 1;
    values[2][2] = 1;
    values[3][3] = 1;
  }
  
  /**
   * Multiplies this {@link Matrix4} by the given {@link Matrix4}.
   * This {@link Matrix4} is the left hand side of the multiplication and the 
   * other {@link Matrix4} is the right hand side of the multiplication.
   * 
   * @param other The {@link Matrix4} to multiply this {@link Matrix4} by.
   * @return A reference to this {@link Matrix4}.
   */
  public Matrix4 mul(Matrix4 other) {
    return mul(other, this);
  }
  
  /**
   * Multiplies this {@link Matrix4} by the given {@link Matrix4} and stores
   * the result in a separate {@link Matrix4}.  This {@link Matrix4} is the
   * left hand side of the multiplication and the other {@link Matrix4} is the
   * right hand side of the multiplication.
   * 
   * @param other The {@link Matrix4} to multiply this {@link Matrix4} by.
   * @param out The {@link Matrix4} to store the result in.
   * @return A reference to the out parameter.
   */
  public Matrix4 mul(Matrix4 other, Matrix4 out) {
    float[] vec = new float[4];
    
    //if the out matrix is the same as the other one then multiply by the columns
    //on the other matrix.
    if(out == other) {
      for(int j = 0; j < 4; j++) {
        for(int i = 0; i < 4; i++)
          vec[i] = 
            values[i][0] * other.values[0][j] +
            values[i][1] * other.values[1][j] +
            values[i][2] * other.values[2][j] +
            values[i][3] * other.values[3][j];
        
        out.values[0][j] = vec[0];
        out.values[1][j] = vec[1];
        out.values[2][j] = vec[2];
        out.values[3][j] = vec[3];
      }
    } else {
      
      for(int i = 0; i < 4; i++) {
        for(int j = 0; j < 4; j++)
          vec[j] = 
            values[i][0] * other.values[0][j] +
            values[i][1] * other.values[1][j] +
            values[i][2] * other.values[2][j];
        
        System.arraycopy(vec, 0, out.values[i], 0, 4);
      }
    }
    
    return out;
  }
  
  /**
   * Adds the values from the given {@link Matrix4} from this {@link Matrix4}.
   * 
   * @param other The {@link Matrix4} to add to this {@link Matrix4}.
   * @return A reference to this {@link Matrix4} for chaining operations.
   */
  public Matrix4 add(Matrix4 other) {
    return add(other, this);
  }
  
  /**
   * Adds the values from the given {@link Matrix4} from this {@link Matrix4}
   * and places the result in a separate {@link Matrix4}.
   * 
   * @param other The {@link Matrix4} to add from this {@link Matrix4}.
   * @param out The {@link Matrix4} to put the result into.
   * @return A reference to this {@link Matrix4} for chaining operations.
   */
  public Matrix4 add(Matrix4 other, Matrix4 out) {
    for(int i = 0; i < 4; i++)
      for(int j = 0; j < 4; j++)
        out.values[i][j] = values[i][j] + other.values[i][j];
    
    return out;
  }
  
  /**
   * Subtracts the values from the given {@link Matrix4} from this {@link Matrix4}.
   * 
   * @param other The {@link Matrix4} to subtract from this {@link Matrix4}.
   * @return A reference to this {@link Matrix4} for chaining operations.
   */
  public Matrix4 sub(Matrix4 other) {
    return add(other, this);
  }
  
  /**
   * Subtracts the values from the given {@link Matrix4} from this {@link Matrix4}
   * and places the result in a separate {@link Matrix4}.
   * 
   * @param other The {@link Matrix4} to subtract from this {@link Matrix4}.
   * @param out The {@link Matrix4} to put the result into.
   * @return A reference to this {@link Matrix4} for chaining operations.
   */
  public Matrix4 sub(Matrix4 other, Matrix4 out) {
    for(int i = 0; i < 4; i++)
      for(int j = 0; j < 4; j++)
        out.values[i][j] = values[i][j] - other.values[i][j];
    
    return out;
  }
  
  /**
   * Stores this {@link Matrix4} in row major order in the given {@link ByteBuffer}.
   * The buffer is not reset or otherwise changed other than storing the values.
   * 
   * @param buffer The {@link ByteBuffer} to store this {@link Matrix4} in.
   */
  public void store(ByteBuffer buffer) {
    store(buffer.asFloatBuffer());
  }
  
  /**
   * Stores this {@link Matrix4} in row major order in the given {@link FloatBuffer}.
   * The buffer is not reset or otherwise changed other than storing the values.
   * 
   * @param buffer The {@link FloatBuffer} to store this {@link Matrix4} in.
   */
  public void store(FloatBuffer buffer) {
    for(int i = 0; i < 4; i++)
      buffer.put(values[i]);
  }
  
  /**
   * Loads values into this {@link Matrix4} in row major order from the 
   * given {@link ByteBuffer}. The buffer is not reset or otherwise changed 
   * other than loading the values.
   * 
   * @param buffer The {@link ByteBuffer} to load values from.
   */
  public void load(ByteBuffer buffer) {
    load(buffer.asFloatBuffer());
  }
  
  /**
   * Loads values into this {@link Matrix4} in row major order from the 
   * given {@link FloatBuffer}. The buffer is not reset or otherwise changed
   * other than storing the values.
   * 
   * @param buffer The {@link FloatBuffer} to load values form.
   */
  public void load(FloatBuffer buffer) {
    for(int i = 0; i < 4; i++)
      for(int j = 0; j < 4; j++)
        values[i][j] = buffer.get();
  }
}
