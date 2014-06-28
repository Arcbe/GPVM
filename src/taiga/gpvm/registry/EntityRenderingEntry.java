/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

import taiga.gpvm.render.EntityRenderer;
import taiga.gpvm.render.TileRenderer;

/**
 * An entry that contains all of the information needed to render a given type
 * of {@link Tile}.
 * 
 * @author russell
 */
public class EntityRenderingEntry extends RegistryEntry {
  
  /**
   * The {@link Class} of the {@link TileRenderer} that will be used to render the
   * given {@link Tile}s.
   */
  public final EntityRenderer renderer;
  
  /**
   * The {@link EntityEntry} for the entities that will use this {@link EntityRenderingEntry}
   * for rendering.
   */
  public final EntityEntry entity;

  /**
   * Creates a new {@link RenderingEntry} with the given attributes.
   * 
   * @param rend The class of the {@link TileRenderer} for this {@link RenderingEntry}.
   * @param e The {@link TileEntry} that this {@link RenderingEntry} is for.
   */
  public EntityRenderingEntry(EntityRenderer rend, EntityEntry e) {
    super(e.getName());
    
    renderer = rend;
    entity = e;
  }
  
}
