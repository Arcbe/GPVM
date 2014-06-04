/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
