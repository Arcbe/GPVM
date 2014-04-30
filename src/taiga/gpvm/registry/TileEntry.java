/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

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

  public TileEntry(String modname, String name, boolean opaque, boolean solid) {
    super(modname + "." + name);
    this.modname = modname;
    this.name = name;
    this.opaque = opaque;
    this.solid = solid;
  }

  public String getName() {
    StringBuilder builder = new StringBuilder();
    
    builder.append(modname);
    builder.append('.');
    builder.append(name);
    
    return builder.toString();
  } 
}
