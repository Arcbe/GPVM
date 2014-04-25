package gpvm.map;

import taiga.gpvm.render.TileRenderer;

/**
 *
 * @author russell
 */
public class TileDefinition {
  /**
   * The simple name of the tiles using this definition.  This is the name 
   * that will be shown in game to the player.  This name does not need to be
   * unique.
   */
  public final String name;
  
  /**
   * The canonical name of the tile using this definition.  This is the
   * name used within the program, and must be unique between definitions.
   */
  public final String canonname;
  
  /**
   * The amount of metadata that can be stored in the tile ids.  Tile metadata
   * will be a single integer value less than this metadata value.  The maximum
   * allowed value is 2^48 or approximately 280 million.
   */
  public final long metadata;
  
  /**
   * Determines whether the tiles using this definition are opaque.
   * The renderer uses this value to determine what to draw, while incorrect
   * usage will not likely produce an error it will produce undefined behavior.
   */
  public final boolean opaque;

  /**
   * Creates a new TileDefinition.
   * 
   * @param name The name of the tile.
   * @param canonname The canonical name of the tile
   * @param metadata The metadata to store in the tile id.
   * @param opaque Whether the {@link Tile} is opaque.
   */
  public TileDefinition(String name, String canonname, long metadata, boolean opaque) {
    assert metadata <= (long)2 << 48;
    
    this.name = name;
    this.canonname = canonname;
    this.metadata = metadata;
    this.opaque = opaque;
  }
}
