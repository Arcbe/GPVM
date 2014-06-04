/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.map;

import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.text.TextLocalizer;
import taiga.code.util.geom.Coordinate;
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
  public void setTileEntry(TileEntry ent, Coordinate loc) {
    if(target == null) {
      log.log(Level.WARNING, NO_TARGET);
      return;
    }
    
    Tile t = target.getTile(loc);
    if(t == null) {
      log.log(Level.WARNING, TILE_NOT_LOADED, new Object[] {target.getFullName(), loc});
      return;
    }
    
    //set the type and reset the damage
    t.type = ent;
    t.damage = 0;
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
    
    target.mutator = this;
    target = tar;
  }
  
  private World target;
  
  private static final String locprefix = WorldMutator.class.getName().toLowerCase();
  
  private static final String MUTATOR_PRESENT_EX = locprefix + ".mutator_present";
  private static final String NO_TARGET = locprefix + ".no_target";
  private static final String TILE_NOT_LOADED = locprefix + ".tile_not_loaded";
  
  private static final Logger log = Logger.getLogger(locprefix, 
    System.getProperty("tagia.code.logging.text"));
}
