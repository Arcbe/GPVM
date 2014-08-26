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
public class EntityType extends RegistryEntry {
  
  /**
   * The name of the mod that added this {@link Entity}.
   */
  public final String modname;
  
  /**
   * The name of the {@link Entity} within the name space of the mod.  The get name method
   * will return the name prepended with the modname and separated by a '.'.
   */
  public final String simplename;

  /**
   * Creates a new {@link EntityEntry} with the given information.
   * 
   * @param modname The name of the {@link Mod} that this {@link EntityEntry} is from.
   * @param name The internal name for this {@link EntityEntry}.
   */
  public EntityType(String modname, String name) {
    super(modname + "." + name);
    this.modname = modname;
    this.simplename = name;
  }
}
