/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

import taiga.gpvm.render.Renderer;

/**
 *
 * @author russell
 */
public class RenderingEntry extends RegistryEntry {
  
  public final Class<? extends Renderer> renderer;
  
  public final RenderingInfo info;
  
  public final TileEntry tile;

  public RenderingEntry(String name, Class<? extends Renderer> rend, RenderingInfo inf, TileEntry t) {
    super(name);
    
    renderer = rend;
    info = inf;
    tile = t;
  }
  
}
