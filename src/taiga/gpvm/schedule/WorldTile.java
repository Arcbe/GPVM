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

package taiga.gpvm.schedule;

import taiga.gpvm.map.Tile;
import taiga.gpvm.map.World;
import taiga.gpvm.registry.TileEntry;

/**
 *
 * @author russell
 */
public class WorldTile {

  public WorldTile(Tile tile, World parent) {
    this.tile = tile;
    this.parent = parent;
  }
  
  public long getDamage() {
    return tile.damage;
  }
  
  public TileEntry getType() {
    return tile.type;
  }
  
  public void setType(TileEntry type) {
    
  }
  
  /**
   * Adds the given amount of damage to the {@link WorldTile}.  If this goes
   * beyond the maximum amount of damage that the type of {@link Tile} can sustain,
   * then this will also destroy the {@link Tile}
   * @param damage 
   */
  public void damage(long damage) {
    
  }
  
  public void setDamage(long damage) {
    
  }
  
  private World parent;
  private Tile tile;
}
