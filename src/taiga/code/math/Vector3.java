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

public final class Vector3 extends ReadableVector3 {
  
  public Vector3() {
  }
  
  public Vector3(float x, float y, float z) {
    super(x, y, z);
  }
  
  public Vector3(ReadableVector3 other) {
    super(other);
  }
  
  public Vector3 setX(float x) {
    this.x = x;
    
    return this;
  }
  
  public Vector3 setY(float y) {
    this.y = y;
    
    return this;
  }
  
  public Vector3 setZ(float z) {
    this.z = z;
    
    return this;
  }
  
  public Vector3 add(ReadableVector3 other) {
    return add(other, this);
  }
  
  public Vector3 add(ReadableVector3 other, Vector3 out) {
    out.x = x + other.x;
    out.y = y + other.y;
    out.z = z + other.z;
    
    return out;
  }
  
  public Vector3 sub(ReadableVector3 other) {
    return sub(other, this);
  }
  
  public Vector3 sub(ReadableVector3 other, Vector3 out) {
    out.x = x - other.x;
    out.y = y - other.y;
    out.z = z - other.z;
    
    return out;
  }
  
  public Vector3 scale(float scale) {
    x *= scale;
    y *= scale;
    z *= scale;
    
    return this;
  }
  
  public Vector3 normalize() {
    return normalize(this);
  }
  
  public Vector3 normalize(Vector3 out) {
    return out.setX(x).setY(y).setZ(z).scale(1f / len3D());
  }
  
  public Vector3 cross(ReadableVector3 other) {
    return cross(other, this);
  }
  
  public Vector3 cross(ReadableVector3 other, Vector3 out) {
    float tx = y * other.z - other.y * z;
    float ty = other.x * z - x * other.z;
    float tz = x * other.y - other.x * y;
    
    return out.setX(tx).setY(ty).setZ(tz);
  }
}
