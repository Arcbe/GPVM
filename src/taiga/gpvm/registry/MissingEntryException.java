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
 * An {@link Exception} thrown when an entry is not present within a {@link Registry}.
 * 
 * @author russell
 */
class MissingEntryException extends RuntimeException {

  /**
   * Constructs a new instances using the given name of the missing {@link RegistryEntry}.
   * 
   * @param name The name of the missing {@link RegistryEntry}
   */
  public MissingEntryException(String name) {
    super(name);
  }
  
  private static final long serialVersionUID = 1;
}
