package gpvm.map;

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
   * @param opaque Whether the {@link Tile} is opaque.
   */
  public TileDefinition(String name, String canonname, boolean opaque) {
    
    this.name = name;
    this.canonname = canonname;
    this.opaque = opaque;
  }
}
