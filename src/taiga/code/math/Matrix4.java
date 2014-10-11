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
 * A 4x4 matrix row major matrix, meaning the first index is the row, and
 * the second is the column index.
 * 
 * @author Russell Smith
 */
public final class Matrix4 extends ReadableMatrix4 implements Serializable {

  /**
   * Creates a new identity {@link Matrix4}.
   */
  public Matrix4() {
    super();
  }
  
  /**
   * Creates a {@link Matrix4} that uses the given array for storing
   * its values;
   * @param vals 
   */
  public Matrix4(float[][] vals) {
    super(vals);
  }
  
  /**
   * Copies the values from the given {@link ReadableMatrix4} into a new
   * {@link Matrix4}.
   * 
   * @param other The {@link ReadableMatrix4} to copy.
   */
  public Matrix4(ReadableMatrix4 other) {
    setValues(other);
  }
  
  public final Matrix4 setIdentity() {
    values[0][0] = 1;
    values[0][1] = 0;
    values[0][2] = 0;
    values[0][3] = 0;
    
    values[1][0] = 0;
    values[1][1] = 1;
    values[1][2] = 0;
    values[1][3] = 0;
    
    values[2][0] = 0;
    values[2][1] = 0;
    values[2][2] = 1;
    values[2][3] = 0;
    
    values[3][0] = 0;
    values[3][1] = 0;
    values[3][2] = 0;
    values[3][3] = 1;
    
    return this;
  }
  
  /**
   * Sets this {@link Matrix4} to the inverse of the given {@link ReadableMatrix4}.
   * 
   * @param other The {@link ReadableMatrix4} to get the inverse from.
   * @return A reference to this {@link Matrix4}.
   */
  public final Matrix4 asInverse(ReadableMatrix4 other) {
    setValues(other);
    return invert();
  }
  
  /**
   * Inverts this {@link Matrix4}.
   * 
   * @return A reference to this {@link Matrix4}.
   */
  public final Matrix4 invert() {
    return invert(this);
  }
  
  /**
   * Sets the given {@link Matrix4} to the inverse of this {@link Matrix4}.
   * If this {@link Matrix4} is not invertible, then the output of this method is
   * null and the state of the out parameter is undefined.
   * 
   * @param out The {@link Matrix4} to set to the inverse.
   * @return A reference to the out parameter.
   */
  public final Matrix4 invert(Matrix4 out) {
    float[][] orig;
    //create a copy of the original values if needed
    if(out == this) {
      orig = new float[4][4];
      for(int i = 0; i < 4; i++)
        System.arraycopy(values[i], 0, orig[i], 0, 4);
    } else {
      //just use the array in this matrix if possible.
      orig = values;
    }
    
    //first half of the matrix
    float tmp0 = orig[2][2] * orig[3][3];
    float tmp1 = orig[2][3] * orig[3][2];
    float tmp2 = orig[2][1] * orig[3][3];
    float tmp3 = orig[2][3] * orig[3][1];
    float tmp4 = orig[2][1] * orig[3][2];
    float tmp5 = orig[2][2] * orig[3][1];
    float tmp6 = orig[2][0] * orig[3][3];
    float tmp7 = orig[2][3] * orig[3][0];
    float tmp8 = orig[2][0] * orig[3][2];
    float tmp9 = orig[2][2] * orig[3][0];
    float tmp10 = orig[2][0] * orig[3][1];
    float tmp11 = orig[2][1] * orig[3][0];
    
    out.values[0][0] = 
      orig[1][1] * (tmp0 - tmp1) +
      orig[1][2] * (tmp3 - tmp2) +
      orig[1][3] * (tmp4 - tmp5);
    
    out.values[1][0] = 
      orig[1][0] * (tmp1 - tmp0) +
      orig[1][2] * (tmp6 - tmp7) +
      orig[1][3] * (tmp9 - tmp8);
    
    out.values[2][0] = 
      orig[1][0] * (tmp2 - tmp3) +
      orig[1][1] * (tmp7 - tmp6) +
      orig[1][3] * (tmp10 - tmp11);
    
    out.values[3][0] = 
      orig[1][0] * (tmp5 - tmp4) +
      orig[1][1] * (tmp8 - tmp9) +
      orig[1][2] * (tmp11 - tmp10);
    
    out.values[0][1] = 
      orig[0][1] * (tmp1 - tmp0) +
      orig[0][2] * (tmp2 - tmp3) +
      orig[0][3] * (tmp5 - tmp4);
    
    out.values[1][1] = 
      orig[0][0] * (tmp0 - tmp1) +
      orig[0][2] * (tmp7 - tmp6) +
      orig[0][3] * (tmp8 - tmp9);
    
    out.values[2][1] = 
      orig[0][0] * (tmp3 - tmp2) +
      orig[0][1] * (tmp6 - tmp7) +
      orig[0][3] * (tmp11 - tmp10);
    
    out.values[3][1] = 
      orig[0][0] * (tmp4 - tmp5) +
      orig[0][1] * (tmp9 - tmp8) +
      orig[0][2] * (tmp10 - tmp11);
    
    //second half of the matrix
    tmp0 = orig[0][2] * orig[1][3];
    tmp1 = orig[0][3] * orig[1][2];
    tmp2 = orig[0][1] * orig[1][3];
    tmp3 = orig[0][3] * orig[1][1];
    tmp4 = orig[0][1] * orig[1][2];
    tmp5 = orig[0][2] * orig[1][1];
    tmp6 = orig[0][0] * orig[1][3];
    tmp7 = orig[0][3] * orig[1][0];
    tmp8 = orig[0][0] * orig[1][2];
    tmp9 = orig[0][2] * orig[1][0];
    tmp10 = orig[0][0] * orig[1][1];
    tmp11 = orig[0][1] * orig[1][0];
    
    out.values[0][2] = 
      orig[3][1] * (tmp0 - tmp1) +
      orig[3][2] * (tmp3 - tmp2) +
      orig[3][3] * (tmp4 - tmp5);
    
    out.values[1][2] = 
      orig[3][0] * (tmp1 - tmp0) +
      orig[3][2] * (tmp6 - tmp7) +
      orig[3][3] * (tmp9 - tmp8);
    
    out.values[2][2] = 
      orig[3][0] * (tmp2 - tmp3) +
      orig[3][1] * (tmp7 - tmp6) +
      orig[3][3] * (tmp10 - tmp11);
    
    out.values[3][2] = 
      orig[3][0] * (tmp5 - tmp4) +
      orig[3][1] * (tmp8 - tmp9) +
      orig[3][2] * (tmp11 - tmp10);
    
    out.values[0][3] = 
      orig[2][1] * (tmp1 - tmp0) +
      orig[2][2] * (tmp2 - tmp3) +
      orig[2][3] * (tmp5 - tmp4);
    
    out.values[1][3] = 
      orig[2][0] * (tmp0 - tmp1) +
      orig[2][2] * (tmp7 - tmp6) +
      orig[2][3] * (tmp8 - tmp9);
    
    out.values[2][3] = 
      orig[2][0] * (tmp3 - tmp2) +
      orig[2][1] * (tmp6 - tmp7) +
      orig[2][3] * (tmp11 - tmp10);
    
    out.values[3][3] = 
      orig[2][0] * (tmp4 - tmp5) +
      orig[2][1] * (tmp9 - tmp8) +
      orig[2][2] * (tmp10 - tmp11);
    
    float det = 
      out.values[0][0] * orig[0][0] +
      out.values[1][0] * orig[0][1] +
      out.values[2][0] * orig[0][2] +
      out.values[3][0] * orig[0][3];
    
    if(det == 0) return null;
    else return out.scale(1 / det);
  }
  
  /**
   * Multiplies all of the values of this matrix by the given amount.
   * 
   * @param amt The amount to scale the {@link Matrix4} by.
   * @return A reference to this {@link Matrix4}.
   */
  public final Matrix4 scale(float amt) {
    return scale(amt, this);
  }
  
  /**
   * Multiplies all of the values of this matrix by the given amount and
   * places the results in the given out {@link Matrix4}.
   * 
   * @param amt The amount to scale the {@link Matrix4} by.
   * @param out The {@link Matrix4} to place the result in.
   * @return A reference to the out parameter.
   */
  public final Matrix4 scale(float amt, Matrix4 out) {
    for(int i = 0; i < 4; i++)
      for(int j = 0; j < 4; j++)
        out.values[i][j] = amt * values[i][j];
    
    return out;
  }
  
  /**
   * Returns a copy of this {@link Matrix4} as a {@link ReadableMatrix4}
   * with no mutator methods.  Both of these matrices share the same data and
   * the {@link ReadableMatrix4} will be changed when the original is changed.
   * 
   * @return A read only copy of this matrix.
   */
  public final ReadableMatrix4 asReadOnly() {
    return new ReadableMatrix4(values);
  }
  
  /**
   * Sets the value of a specific cell in this matrix.
   * 
   * @param row The row of the cell.
   * @param col The column of the cell.
   * @param val The value to set the cell to.
   */
  public final void setValue(int row, int col, float val) {
    values[row][col] = val;
  }
  
  /**
   * Sets the values of this {@link Matrix4} from the values in the given
   * {@link ReadableMatrix4}.  Neither matrix will be changed when the other is
   * changed because of this method.
   * 
   * @param other The {@link ReadableMatrix4} to copy values from.
   */
  public final void setValues(ReadableMatrix4 other) {
    setValues(other.values);
  }
  
  /**
   * Sets the values of this {@link Matrix4} from the given values.
   * 
   * @param vals The new values for this {@link Matrix4}.
   */
  public final void setValues(float[][] vals) {
    for(int i = 0; i < 4; i++)
      System.arraycopy(vals[i], 0, values[i], 0, 4);
  }
  
  /**
   * Multiplies this {@link Matrix4} by the given {@link Matrix4}.
   * This {@link Matrix4} is the left hand side of the multiplication and the 
   * other {@link Matrix4} is the right hand side of the multiplication.
   * 
   * @param other The {@link Matrix4} to multiply this {@link Matrix4} by.
   * @return A reference to this {@link Matrix4}.
   */
  public final Matrix4 mul(ReadableMatrix4 other) {
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
  public final Matrix4 mul(ReadableMatrix4 other, Matrix4 out) {
    return mul(values, other.values, out);
  }
  
  /**
   * Same as {@link #mul(taiga.code.math.ReadableMatrix4, taiga.code.math.Matrix4) }
   * but uses this {@link Matrix4} as the right hand side instead of the
   * left hand side.
   * 
   * @param other The other {@link Matrix4} in the multiplication.
   * @param out The {@link Matrix4} to store the result in.
   * @return A reference to the out parameter.
   */
  public final Matrix4 mulRHS(ReadableMatrix4 other, Matrix4 out) {
    return mul(other.values, values, out);
  }
  
  /**
   * Adds the values from the given {@link Matrix4} from this {@link Matrix4}.
   * 
   * @param other The {@link Matrix4} to add to this {@link Matrix4}.
   * @return A reference to this {@link Matrix4} for chaining operations.
   */
  public final Matrix4 add(ReadableMatrix4 other) {
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
  public final Matrix4 add(ReadableMatrix4 other, Matrix4 out) {
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
  public final Matrix4 sub(ReadableMatrix4 other) {
    return sub(other, this);
  }
  
  /**
   * Subtracts the values from the given {@link Matrix4} from this {@link Matrix4}
   * and places the result in a separate {@link Matrix4}.
   * 
   * @param other The {@link Matrix4} to subtract from this {@link Matrix4}.
   * @param out The {@link Matrix4} to put the result into.
   * @return A reference to this {@link Matrix4} for chaining operations.
   */
  public final Matrix4 sub(ReadableMatrix4 other, Matrix4 out) {
    for(int i = 0; i < 4; i++)
      for(int j = 0; j < 4; j++)
        out.values[i][j] = values[i][j] - other.values[i][j];
    
    return out;
  }
  
  /**
   * Loads values into this {@link Matrix4} in row major order from the 
   * given {@link ByteBuffer}. The buffer is not reset or otherwise changed 
   * other than loading the values.
   * 
   * @param buffer The {@link ByteBuffer} to load values from.
   * @return A reference to the given {@link ByteBuffer}.
   */
  public final ByteBuffer load(ByteBuffer buffer) {
    load(buffer.asFloatBuffer());
    
    return buffer;
  }
  
  /**
   * Loads values into this {@link Matrix4} in row major order from the 
   * given {@link FloatBuffer}. The buffer is not reset or otherwise changed
   * other than storing the values.
   * 
   * @param buffer The {@link FloatBuffer} to load values form.
   * @return A reference to the given {@link FloatBuffer}.
   */
  public final FloatBuffer load(FloatBuffer buffer) {
    for(int i = 0; i < 4; i++)
      for(int j = 0; j < 4; j++)
        values[i][j] = buffer.get();
    
    return buffer;
  }
  
  private final Matrix4 mul(float[][] left, float[][] right, Matrix4 out) {
    float[] vec = new float[4];
    
    //if the out matrix is the same as the other one then multiply by the columns
    //on the other matrix.
    if(out.values == right) {
      for(int j = 0; j < 4; j++) {
        for(int i = 0; i < 4; i++)
          vec[i] = 
            left[i][0] * right[0][j] +
            left[i][1] * right[1][j] +
            left[i][2] * right[2][j] +
            left[i][3] * right[3][j];
        
        out.values[0][j] = vec[0];
        out.values[1][j] = vec[1];
        out.values[2][j] = vec[2];
        out.values[3][j] = vec[3];
      }
    //otherwise assume that the out parameter is this matrix.
    } else {
      
      for(int i = 0; i < 4; i++) {
        for(int j = 0; j < 4; j++)
          vec[j] = 
            left[i][0] * right[0][j] +
            left[i][1] * right[1][j] +
            left[i][2] * right[2][j] +
            left[i][3] * right[3][j];
        
        System.arraycopy(vec, 0, out.values[i], 0, 4);
      }
    }
    
    return out;
  }
}
