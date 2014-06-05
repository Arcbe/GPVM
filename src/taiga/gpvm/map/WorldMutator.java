/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.map;

import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.text.TextLocalizer;
import taiga.gpvm.util.geom.Coordinate;
import taiga.gpvm.registry.TileEntry;

/**
 * A class to control changes made to a {@link World}.  Only one {@link WorldMutator}
 * is allowed per {@link World}.
 * 
 * @author russell
 */
public class WorldMutator {
  
  /**
   * Changes the {@link TileEntry} of the {@link Tile} at the given {@link Coordinate}.
   * If the {@link TileEntry} is null then this will simply set the {@link Tile}
   * to be air.
   * 
   * @param ent The {@link TileEntry} to change to or null.
   * @param loc The {@link Coordinate} of the {@link Tile} to change.
   */
  public TileEntry setTileEntry(TileEntry ent, Coordinate loc) {
    Tile t = getTile(loc);
    if(t == null) return null;
    
    
    //set the type and reset the damage
    TileEntry old = t.type;
    t.type = ent;
    t.damage = 0;
    
    return old;
  }
  
  /**
   * Changes the damage value of the {@link Tile} at the given {@link Coordinate}.
   * 
   * @param damage The damage value to the set for the {@link Tile}.
   * @param loc The {@link Coordinate} of the {@link Tile} to change.
   */
  public Long setDamageValue(long damage, Coordinate loc) {
    Tile t = getTile(loc);
    if(t == null) return null;
    
    long old = t.damage;
    t.damage = damage;
    
    return old;
  }
  
  /**
   * Applies damage to the {@link Tile} at the given {@link Coordinate}.
   * 
   * @param damage The amount of damage to apply.
   * @param loc The {@link Coordinate} of the {@link Tile} to change.
   */
  public Long damageTile(long damage, Coordinate loc) {
    Tile t = getTile(loc);
    if(t == null) return null;
    
    long old = t.damage;
    t.damage -= damage;
    
    return old;
  }
  
  /**
   * Sets the {@link World} that this {@link WorldMutator} will affect.
   * 
   * @param tar The target {@link World}.
   * @throws MutatorPresentException Thrown if there is already a {@link WorldMutator}
   *  for the target {@link World}.
   */
  public void setWorld(World tar) throws MutatorPresentException {
    //remove the previous target if there is one.
    if(target != null) {
      assert target.mutator == this;
      
      target.mutator = null;
    }
    
    if(tar.mutator != null) throw new MutatorPresentException(
      TextLocalizer.localize(MUTATOR_PRESENT_EX, tar.getFullName()));
    
    tar.mutator = this;
    target = tar;
  }
  
  private World target;
  
  private Tile getTile(Coordinate loc) {
    if(target == null) {
      log.log(Level.WARNING, NO_TARGET);
      return null;
    }
    
    Tile t = target.getTile(loc);
    if(t == null) {
      log.log(Level.WARNING, TILE_NOT_LOADED, new Object[] {target.getFullName(), loc});
      return null;
    }
    
    return t;
  }
  
  private static final String locprefix = WorldMutator.class.getName().toLowerCase();
  
  private static final String MUTATOR_PRESENT_EX = locprefix + ".mutator_present";
  private static final String NO_TARGET = locprefix + ".no_target";
  private static final String TILE_NOT_LOADED = locprefix + ".tile_not_loaded";
  
  private static final Logger log = Logger.getLogger(locprefix, 
    System.getProperty("tagia.code.logging.text"));
}
