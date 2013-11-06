package gpvm.map;

/**
 * Represents a single unit of the game map.  Each tile has some tile type
 * that corresponds to a list of available tiles, along with an optional set of 
 * data fields.  A damage value is also stored in each tile.
 * 
 * @author russell
 */
public final class Tile {
  public class ReadOnlyTile {
    /**
     * Returns a long integer that is the tile id for this tile.
     */
    public long getType() {
      return type;
    }
    
    /**
     * Returns the current damage value of the tile.
     */
    public long getDamage() {
      return damage;
    }
    
    /**
     * Returns a read only instance of the tile data for this tile.  If
     * there is no data then this method will return null.
     */
    public TileData.ReadOnlyTileData getTileData() {
      if(data == null) return null;
      else return data.new ReadOnlyTileData();
    }
  }
  
  public long type;
  public long damage;
  public TileData data;
}
