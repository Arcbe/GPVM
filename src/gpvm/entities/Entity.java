/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gpvm.entities;

/**
 * The base class for all entities.  This class provies an interface for drawing
 * the {@link Entity} and a location within the map.
 * 
 * @author russell
 */
public abstract class Entity {
  protected float locx;
  protected float locy;
  
  /**
   * Called by the rendering thread, this method allows the {@link Entity} to
   * render itself.
   */
  public abstract void render();
}
