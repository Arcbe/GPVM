/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.map;

/**
 *
 * @author russell
 */
public final class Region {
  /**
   * Each region will be a cube with edges of length REGION_SIZE.
   */
  public static final byte REGION_SIZE = 16;
  
  Region() {
    tiles = new Tile[REGION_SIZE * REGION_SIZE * REGION_SIZE];
  }
  
  Region(Tile[] data) {
    assert data.length == REGION_SIZE * REGION_SIZE * REGION_SIZE;
    tiles = data;
  }
  
  //retreives the tile at the given point.
  private Tile getTile(byte x, byte y, byte z) {
    return tiles[z * REGION_SIZE * REGION_SIZE + x * REGION_SIZE + y];
  }
  
  private Tile[] tiles;
}
