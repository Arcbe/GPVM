/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package placeholder.util.geometry;

/**
 * Represents the location of a tile in space.
 * 
 * @author russell
 */
public class Coordinate {
  public int x;
  public int y;
  public int z;

  public Coordinate() {
  }

  public Coordinate(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
}
