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

public final class Vector3 extends ReadableVector3 {
  
  public Vector3() {
  }
  
  public Vector3(float x, float y, float z) {
    super(x, y, z);
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
  
  public Vector3 add(Vector3 other) {
    return add(other, this);
  }
  
  public Vector3 add(Vector3 other, Vector3 out) {
    out.x = x + other.x;
    out.y = y + other.y;
    out.z = z + other.z;
    
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
    float ty = x * other.z - other.x * z;
    float tz = x * other.y - other.x * y;
    
    return out.setX(tx).setY(ty).setZ(ty);
  }
}
