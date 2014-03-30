/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gpvm.entities;

import gpvm.render.GraphicsSystem;
import java.util.ArrayList;

/**
 * This class keeps track of and updates all {@link Entity}s.  It also provides
 * a way to draw the {@link Entity}s for the {@link GraphicsSystem}
 * 
 * @author russell
 */
public class EntityManager {

  public EntityManager() {
    entities = new ArrayList<>();
  }
  
  /**
   * Calls the render method on all managed {@link Entity}s.
   */
  public void render() {
    for(Entity ent : entities)
      ent.render();
  }
  
  /**
   * Adds a new static {@link Entity} to the manager.  This {@link Entity} will
   * not have any physics applied to it by the manager.
   * 
   * @param ent The {@link Entity} to add.
   */
  public void addEntity(Entity ent) {
    entities.add(ent);
  }
  
  private ArrayList<Entity> entities;
}
