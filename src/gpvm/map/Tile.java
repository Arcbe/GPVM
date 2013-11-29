package gpvm.map;

/**
 * Represents a single unit of the game map.  Each tile has some tile type
 * that corresponds to a list of available tiles, along with an optional set of 
 * data fields.  A damage value is also stored in each tile.
 * 
 * @author russell
 */
public final class Tile {
  public static final long NULL = 0;
  /**
   * An instance of a Tile that only has function that can read its state and
   * not modify it.
   */
  public class ReadOnlyTile {
    /**
     * Returns a long integer that is the tile id for this tile.
     * @return The tile id.
     */
    public long getType() {
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
   * The tile id for this {@link Tile}, an id of 0 indicates that the block is
   * empty.
   */
  public long type;
  /**
   * The amount of damage that the block has taken.
   */
  public long damage;
  /**
   * Any data associated with this {@link Tile} or null if there is not any data.
   */
  public TileData data;
}
