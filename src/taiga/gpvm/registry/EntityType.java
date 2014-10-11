/*
 * Copyright (C) 2014 Russell Smith.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package taiga.gpvm.registry;

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
