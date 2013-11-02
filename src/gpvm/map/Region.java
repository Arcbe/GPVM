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
  
  /**
   * Constructs a region with only 
   */
  public Region() {
    tiles = new Tile[REGION_SIZE * REGION_SIZE * REGION_SIZE];
    
    for(int i = 0; i < tiles.length; i++)
      tiles[i] = new Tile();
  }
  
  public Region(Tile[] data) {
    assert data.length == REGION_SIZE * REGION_SIZE * REGION_SIZE;
    tiles = data;
  }
  
  /**
   * Retrieves the tile at the given point.  The location is relative to the
   * origin of the region and not the origin of the map.
   * 
   * @param x The x coordinate of the tile.
   * @param y The y coordinate of the tile.
   * @param z The z coordinate of the tile.
   * @return The tile at the given point.
   */
  public Tile getTile(byte x, byte y, byte z) {
    return tiles[z * REGION_SIZE * REGION_SIZE + x * REGION_SIZE + y];
  }
  
  private Tile[] tiles;
}
