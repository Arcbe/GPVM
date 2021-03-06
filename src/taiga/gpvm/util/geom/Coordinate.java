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

package taiga.gpvm.util.geom;

import java.io.Serializable;
import java.text.MessageFormat;
import taiga.gpvm.map.Region;
import taiga.gpvm.map.World;

/**
 * Represents the location of a tile in space.
 * 
 * @author russell
 */
public class Coordinate implements Cloneable, Serializable {
  /**
   * The position of the {@link Coordinate} on the x axis.
   */
  public int x;
  
  /**
   * The position of the {@link Coordinate} on the y axis.
   */
  public int y;
  
  /**
   * The position of the {@link Coordinate} on the z axis.
   */
  public int z;
  
  /**
   * The id of the {@link World} this {@link Coordinate} is in. An id of
   * -1 indicates that this is the current world if there is a current world in
   * the given context.
   */
  public World world;

  /**
   * Creates a coordinate at the origin.
   */
  public Coordinate() {
    world = null;
  }

  /**
   * Creates a {@link Coordinate} at the given location, in the default {@link World}.
   * 
   * @param x The x coordinate
   * @param y The y coordinate
   * @param z The x coordinate
   */
  public Coordinate(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  /**
   * Creates a {@link Coordinate} at the given location in the given {@link World}.
   * 
   * @param x The x coordinate
   * @param y The y coordinate
   * @param z The x coordinate
   * @param w The {@link World} that this {@link Coordinate} is in.
   */
  public Coordinate(int x, int y, int z, World w) {
    this.x = x;
    this.y = y;
    this.z = z;
    world = w;
  }
  
  /**
   * Adds the given value to this {@link Coordinate}.
   * 
   * @param i The value to add to the x coordinate
   * @param j The value to add to the y coordinate.
   * @param k The value to add to the z coordinate.
   * @return A reference to this coordinate.
   */
  public Coordinate add(int i, int j, int k) {
    x += i;
    y += j;
    z += k;
    
    return this;
  }
  
  /**
   * Adds the given {@link Coordinate} to this {@link Coordinate} and 
   * returns a reference to this {@link Coordinate}
   * 
   * @param cor The {@link Coordinate} to add to this one.
   * @return This {@link Coordinate} after the addition.
   */
  public Coordinate add(Coordinate cor) {
    x += cor.x;
    y += cor.y;
    z += cor.z;
    
    return this;
  }
  
  /**
   * Adds the given value to this {@link Coordinate}, but stores the
   * result in the given coordinate without modifying the this one.
   * 
   * @param i The value to add to the x coordinate
   * @param j The value to add to the y coordinate.
   * @param k The value to add to the z coordinate.
   * @param out The {@link Coordinate} to store the result.
   * @return A reference to the resultant {@link Coordinate}
   */
  public Coordinate add(int i, int j, int k, Coordinate out) {
    out.x = x + i;
    out.y = y + j;
    out.z = z + k;
    return out;
  }
  
  /**
   * Returns the coordinate of the bottom, south western most block in
   * the same region as this coordinate.
   * 
   * @return The resulting coordinate
   */
  public Coordinate getRegionCoordinate() {
    return getRegionCoordinate(new Coordinate());
  }
  
  /**
   * Returns the coordinate of the bottom, south western most block in
   * the same region as this coordinate.
   * 
   * @param c The coordinate to put the result in.
   * @return The resulting coordinate
   */
  public Coordinate getRegionCoordinate(Coordinate c) {
    int tx = x % Region.REGION_SIZE;
    int ty = y % Region.REGION_SIZE;
    int tz = z % Region.REGION_SIZE;
    
    if(tx < 0) tx += Region.REGION_SIZE;
    if(ty < 0) ty += Region.REGION_SIZE;
    if(tz < 0) tz += Region.REGION_SIZE;
    
    c.x = x - tx;
    c.y = y - ty;
    c.z = z - tz;
    
    return c;
  }
  
  /**
   * Returns a {@link Coordinate} of this location relative to the origin of
   * the containing {@link Region}.
   * 
   * @return The {@link Coordinate} relative to the {@link Region}s origin.
   */
  public Coordinate getRelativeCoordinate() {
    return getRelativeCoordinate(new Coordinate());
  }
  
  /**
   * Returns the given {@link Coordinate} with the location relative to the 
   * origin of the {@link Region}.
   * 
   * @param c The {@link Coordinate} to store the location in.
   * @return A reference to this given {@link Coordinate}
   */
  public Coordinate getRelativeCoordinate(Coordinate c) {
    c.x = x % Region.REGION_SIZE;
    c.y = y % Region.REGION_SIZE;
    c.z = z % Region.REGION_SIZE;
    
    if(c.x < 0) c.x += Region.REGION_SIZE;
    if(c.y < 0) c.y += Region.REGION_SIZE;
    if(c.z < 0) c.z += Region.REGION_SIZE;
    
    return c;
  }

  /**
   * Returns a hash code of this {@link Coordinate}.
   * 
   * @return A has of this {@link Coordinate}.
   */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 17 * hash + this.x;
    hash = 17 * hash + this.y;
    hash = 17 * hash + this.z;
    if(world != null) hash = 17 * hash + world.getWorldID();
    return hash;
  }

  /**
   * Determines whether the given {@link Coordinate} indicates the same location
   * as this {@link Coordinate}.
   * 
   * @param obj The {@link Coordinate} to check equivalence with.
   * @return Whether the two {@link Coordinate}s are equal.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Coordinate other = (Coordinate) obj;
    if (this.x != other.x) {
      return false;
    }
    if (this.y != other.y) {
      return false;
    }
    if (this.z != other.z) {
      return false;
    }
    return true;
  }
  
  /**
   *
   * @return
   */
  @Override
  public String toString() {
    return MessageFormat.format("({0},{1},{2})", x, y, z);
  }
}
