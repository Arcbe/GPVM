/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.util.geometry;

import gpvm.map.Region;

/**
 * Represents the location of a tile in space.
 * 
 * @author russell
 */
public class Coordinate {
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
   * Creates a coordinate at the origin.
   */
  public Coordinate() {
  }
  
  /**
   * Creates a copy of the given coordinate.
   * 
   * @param orig The {@link Coordinate} to copy.
   */
  public Coordinate(Coordinate orig) {
    x = orig.x;
    y = orig.y;
    z = orig.z;
  }

  /**
   * Creates a {@link Coordinate} at the given location.
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
    c.x = x - x % Region.REGION_SIZE;
    c.y = y - y % Region.REGION_SIZE;
    c.z = z - z % Region.REGION_SIZE;
    
    return c;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 17 * hash + this.x;
    hash = 17 * hash + this.y;
    hash = 17 * hash + this.z;
    return hash;
  }

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
}
