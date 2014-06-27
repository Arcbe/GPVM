/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

import java.util.logging.Logger;
import taiga.gpvm.entity.Entity;

/**
 * Contains the definitions for a type of {@link Entity}.
 * 
 * @author russell
 */
public class EntityEntry extends RegistryEntry {
  
  /**
   * The name of the mod that added this {@link Entity}.
   */
  public final String modname;
  
  /**
   * The name of the {@link Entity} within the name space of the mod.  The get name method
   * will return the name prepended with the modname and separated by a '.'.
   */
  public final String name;
  
  /**
   * Whether the tile is opaque.  This should only be true if the tile completely
   * covers the side of any adjacent tile.
   */

  /**
   * Creates a new {@link EntityEntry} with the given information.
   * 
   * @param modname The name of the {@link Mod} that this {@link EntityEntry} is from.
   * @param name The internal name for this {@link EntityEntry}.
   */
  public EntityEntry(String modname, String name) {
    super(modname + "." + name);
    this.modname = modname;
    this.name = name;
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

  private static final String locprefix = EntityEntry.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
