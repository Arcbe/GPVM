/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

import taiga.gpvm.render.Renderer;

/**
 * An entry that contains all of the information needed to render a given type
 * of {@link Tile}.
 * 
 * @author russell
 */
public class EntityRenderingEntry extends RegistryEntry {
  
  /**
   * The {@link Class} of the {@link Renderer} that will be used to render the
   * given {@link Tile}s.
   */
  public final EntityRenderer renderer;
  
  /**
   * Additional information used by the {@link Renderer}.  THis may be null if
   * no information is available.
   */
  public final RenderingInfo info;
  
  /**
   * The {@link EntityEntry} for the entities that will use this {@link EntityRenderingEntry}
   * for rendering.
   */
  public final EntityEntry entity;

  /**
   * Creates a new {@link RenderingEntry} with the given attributes.
   * 
   * @param rend The class of the {@link Renderer} for this {@link RenderingEntry}.
   * @param inf Any additional information for rendering, or null if there is none.
   * @param e The {@link TileEntry} that this {@link RenderingEntry} is for.
   */
  public EntityRenderingEntry(EntityRenderer rend, RenderingInfo inf, EntityEntry e) {
    super(e.getName());
    
    renderer = rend;
    info = inf;
    entity = e;
  }
  
}
