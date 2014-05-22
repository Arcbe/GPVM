package taiga.gpvm.map;

import gpvm.map.TileData;
import taiga.gpvm.registry.TileEntry;

/**
 * Represents a single unit of the game map.  Each tile has some tile type
 * that corresponds to a list of available tiles, along with an optional set of 
 * data fields.  A damage value is also stored in each tile.
 * 
 * @author russell
 */
public final class Tile {
  /**
   * An instance of a Tile that only has function that can read its state and
   * not modify it.
   */
  public class ReadOnlyTile {
    /**
     * Returns a {@link TileEntry} for the definition of the type of this tittle.
     * @return The {@link TileEntry} for this tiles type.
     */
    public TileEntry getType() {
      return type;
    }
    
    /**
     * Returns the current damage value of the tile.
     * @return The damage value
     */
    public long getDamage() {
      return damage;
    }
    
    /**
     * Returns a read only instance of the tile data for this tile.  If
     * there is no data then this method will return null.
     * @return The {@link TileData} for this {@link Tile}
     */
    public TileData.ReadOnlyTileData getTileData() {
      if(data == null) return null;
      else return data.new ReadOnlyTileData();
    }
  }
  
  /**
   * The {@link TileEntry} that defines the type of this tile.
   */
  public TileEntry type;
  /**
   * The amount of damage that the block has taken.
   */
  public long damage;
  /**
   * Any data associated with this {@link Tile} or null if there is not any data.
   */
  public TileData data;
}
