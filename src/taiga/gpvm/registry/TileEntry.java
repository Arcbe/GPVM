/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

import com.sun.org.apache.xpath.internal.operations.Mod;
import taiga.gpvm.map.Tile;

/**
 * Contains the data for a tile of a given type.
 * 
 * @author russell
 */
public class TileEntry extends RegistryEntry {
  
  /**
   * The name of the mod that added this tile.
   */
  public final String modname;
  /**
   * The name of the tile within the name space of the mod.  The get name method
   * will return the name prepended with the modname and separated by a '.'.
   */
  public final String name;
  
  /**
   * Whether the tile is opaque.  This should only be true if the tile completely
   * covers the side of any adjacent tile.
   */
  public final boolean opaque;
  
  /**
   * Whether the tile is a solid cube.  This should only be true if the tile
   * prevents entities from entering its area.
   */
  public final boolean solid;

  /**
   * Creates a new {@link TileEntry} with the given information.
   * 
   * @param modname The name of the {@link Mod} that this {@link TileEntry} is from.
   * @param name The internal name for this {@link TileEntry}.
   * @param opaque Whether {@link Tile}s behind this one are at least partially visible.
   * @param solid Whether entities can pass through {@link Tile}s using this {@link TileEntry}.
   */
  public TileEntry(String modname, String name, boolean opaque, boolean solid) {
    super(modname + "." + name);
    this.modname = modname;
    this.name = name;
    this.opaque = opaque;
    this.solid = solid;
  }

  /**
   * Returns the name of this {@link TileEntry}.  This will have the mod name prepended.
   * 
   * @return The full name of the {@link TileEntry}.
   */
  public String getName() {
    StringBuilder builder = new StringBuilder();
    
    builder.append(modname);
    builder.append('.');
    builder.append(name);
    
    return builder.toString();
  } 
}
