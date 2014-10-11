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

package taiga.gpvm.map;

import taiga.code.registration.NamedObject;
import taiga.gpvm.HardcodedValues;

/**
 * This is the base class for objects that determine which {@link Region}s should be
 * loaded for a given {@link World}.
 * 
 * @author russell
 */
public abstract class RegionManager extends NamedObject {
  /**
   * Creates a new {@link RegionManager}.
   */
  public RegionManager() {
    super(HardcodedValues.REGION_MANAGER_NAME);
  }
  
  /**
   * Checks to see if this {@link RegionManager} is currently attache to a {@link World}.
   * 
   * @return Whether this {@link RegionManager} is attached.
   */
  public boolean hasParentMap() {
    return getParent() instanceof World;
  }
  
  /**
   * Returns the {@link World} that this {@link RegionManager} is responsible for
   * if there is one, otherwise null.
   * 
   * @return The {@link World} this {@link RegionManager} manages or null.
   */
  public World getParentMap() {
    if(hasParentMap())
      return (World) getParent();
    else
      return null;
  }
  
  @Override
  protected void attached(NamedObject par) {
    if(par instanceof World)
      mapAttached((World) par);
  }
  
  /**
   * Called when this {@link RegionManager} is attached to a {@link World}.
   * 
   * @param map The {@link World} this {@link RegionManager} is attached to.
   */
  protected abstract void mapAttached(World map);
}
