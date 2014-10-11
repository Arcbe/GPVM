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

package taiga.gpvm.entity;

import java.util.logging.Logger;

public class EntityChange {
  
  public enum EntityChangeType {
    /**
     * A change in the direction an {@link Entity} is moving.  This requires
     * both the new direction of movement and the current location.
     */
    Movement,
    /**
     * Adds some amount of damage to the targeted {@link Entity}.  The
     * amount of damage can be negative, and will result in healing the {@link Entity}.
     */
    Damage
  }
  
  public final Entity target;
  public final EntityChangeType type;
  
  public final Object first;
  public final Object second;

  public EntityChange(Entity target, EntityChangeType type, Object first, Object second) {
    this.target = target;
    this.type = type;
    this.first = first;
    this.second = second;
  }
  
  

  private static final String locprefix = EntityChange.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
