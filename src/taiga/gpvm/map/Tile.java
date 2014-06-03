package taiga.gpvm.map;

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
  //private Map<Integer, Object> data;
}
