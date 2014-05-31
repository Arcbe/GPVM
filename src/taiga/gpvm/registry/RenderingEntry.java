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
public class RenderingEntry extends RegistryEntry {
  
  /**
   * The {@link Class} of the {@link Renderer} that will be used to render the
   * given {@link Tile}s.
   */
  public final Class<? extends Renderer> renderer;
  
  /**
   * Additional information used by the {@link Renderer}.  THis may be null if
   * no information is available.
   */
  public final RenderingInfo info;
  
  /**
   * The {@link TileEntry} for the tiles that will use this {@link RenderingEntry}
   * for rendering.
   */
  public final TileEntry tile;

  /**
   * Creates a new {@link RenderingEntry} with the given attributes.
   * 
   * @param rend The class of the {@link Renderer} for this {@link RenderingEntry}.
   * @param inf Any additional information for rendering, or null if there is none.
   * @param t The {@link TileEntry} that this {@link RenderingEntry} is for.
   */
  public RenderingEntry(Class<? extends Renderer> rend, RenderingInfo inf, TileEntry t) {
    super(t.getName());
    
    renderer = rend;
    info = inf;
    tile = t;
  }
  
}
