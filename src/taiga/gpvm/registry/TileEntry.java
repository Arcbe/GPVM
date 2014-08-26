/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

import taiga.code.util.DataNode;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.map.Tile;

/**
 * Contains the data for a tile of a given type.
 * 
 * @author russell
 */
public class TileEntry extends RegistryEntry {
  
  /**
   * The name for the field that corresponds with {@link #opaque}.
   */
  public static final String FIELD_NAME_OPAQUE = "opaque";
  
  /**
   * The name for the field that corresponds with {@link #solid}.
   */
  public static final String FIELD_NAME_SOLID = "solid";
  
  /**
   * The name of the mod that added this tile.
   */
  public final String modname;
  /**
   * The name of the tile within the name space of the mod.  The get name method
   * will return the name prepended with the modname and separated by a '.'.
   */
  public final String simplename;
  
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
    super(modname + HardcodedValues.NAMESPACE_SEPERATOR + name);
    this.modname = modname;
    this.simplename = name;
    this.opaque = opaque;
    this.solid = solid;
  }
  
  /**
   * Creates a new {@link TileEntry} using the given {@link DataNode} in
   * the given mods name space.
   * 
   * @param modname The name of the mod that this {@link TileEntry} is associated
   * with.
   * @param name The simple name for this {@link TileEntry}.
   * @param data The {@link DataNode} that stores the information for this 
   * {@link TileEntry}.
   */
  public TileEntry(String modname, String name, DataNode data) {
    super(modname + HardcodedValues.NAMESPACE_SEPERATOR + name);
    
    this.modname = modname;
    this.simplename = name;
    
    Boolean val = data.getValueByName(FIELD_NAME_OPAQUE);
    if(val != null) this.opaque = val;
    else this.opaque = false;
    
    val = data.getValueByName(FIELD_NAME_SOLID);
    if(val != null) this.solid = val;
    else this.solid = false;
  }
}
