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

/**
 * A name entry into a registry.
 * 
 * @author russell
 */
public class RegistryEntry {
  /**
   * The name of the {@link RegistryEntry}.
   */
  public final String name;
  /**
   * The id of the {@link RegistryEntry} assigned by the {@link Registry}
   */
  protected int id;

  /**
   * Creates a new {@link RegistryEntry} with the given name.
   * 
   * @param name The name for the nw {@link RegistryEntry}.
   */
  public RegistryEntry(String name) {
    this.name = name;
    
    id = -1;
  }
  
  /**
   * Returns the id assigned to this {@link RegistryEntry}.  If it has not been
   * added to a registry, or ids have not yet been assigned then this will return -1.
   * @return 
   */
  public int getID() {
    return id;
  }
}
