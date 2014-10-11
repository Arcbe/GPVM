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

package taiga.gpvm.render;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.map.Region;
import taiga.gpvm.map.RegionListener;
import taiga.gpvm.map.Tile;
import taiga.gpvm.util.geom.Coordinate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import taiga.code.math.ReadableMatrix4;
import taiga.code.registration.NamedObject;
import taiga.gpvm.registry.TileRenderingRegistry;

/**
 * A renderer for a {@link Region} in the {@link GameMap}.
 * 
 * @author russell
 */
public final class RegionRenderer extends NamedObject implements RegionListener {
  /**
   * The prefix for the name of the {@link RegionRenderer}.
   */
  public static final String NAME_PREFIX = "region-";
  
  /**
   * Flag that indicates whether a grid should be rendered around the {@link Region}.
   */
  public boolean outline;
  
  /**
   * Creates a new {@link RegionRenderer} for the target region.
   * 
   * @param target The {@link Region} that will be rendered.
   */
  public RegionRenderer(Region target) {
    super(NAME_PREFIX + target.getLocation());
    
    entries = new HashMap<>();
    rendindex = new HashMap<>();
    instances = new ConcurrentHashMap<>();
    dirtyents = new HashMap<>();
    
    reg = target;
    reg.addListener(this);
    dirty = true;
  }
  
  /**
   * Clears all rendering data and regenerates it from the map.
   */
  public void scanRegion() {
    
    //clear all of the data
    entries.clear();
    rendindex.clear();
    
    //construct the various entity infos.
    for(byte i = 0; i < Region.REGION_SIZE; i++) {
      for(byte j = 0; j < Region.REGION_SIZE; j++) {
        for(byte k = 0; k < Region.REGION_SIZE; k++) {
          updateTile(i, j, k);
        }
      }
    }
    
    dirty = false;
  }
  
  /**
   * Update the rendering information for a single entity in the {@link Region}.
   * 
   * @param coor The {@link Coordinate} or the {@link Tile} to update.
   */
  public void updateTile(Coordinate coor) {
    coor = coor.getRelativeCoordinate();
    
    updateTile(coor.x, coor.y, coor.z);
  }
  
  /**
   * Updates rendering information for a single entity in the {@link Region}.
   * 
   * @param x The x coordinate of the {@link Tile} in the {@link Region}.
   * @param y The y coordinate of the {@link Tile} in the {@link Region}.
   * @param z The z coordinate of the {@link Tile} in the {@link Region}.
   */
  public void updateTile(int x, int y, int z) {
    TileRenderingRegistry rendreg = (TileRenderingRegistry) getObject(HardcodedValues.TILE_RENDERING_REGISTRY_NAME);
    
    //first check to see if the entity needs rendering
    Tile tar = reg.getTile(x, y, z);
    if(tar == null || tar.type == null) return;
    
    //now check if the entity is actually visible
    Coordinate loc = new Coordinate(x, y, z);
    Tile[] ngbrs = reg.getWorld().getNeighborTiles(loc);
    boolean visible = false;
    for (Tile ngbr : ngbrs) {
      if (ngbr == null || ngbr.type == null) {
        visible = true;
        break;
      }
      if (!ngbr.type.opaque) {
        visible = true;
        break;
      }
    }
    if(!visible) return;
    
    //The entity can be rendered and is visible, now collect information on the entity.
    TileInfo info = new TileInfo();
    info.adjacent = ngbrs;
    info.tile = tar;
    info.absposition = reg.getLocation().add(x, y, z, new Coordinate());
    info.rendentry = rendreg.getEntry(tar.type);
    
    //ues the asociated renderer if avalaible otherwise use the default one.
    Class<? extends TileRenderer> renderer;
    if(info.rendentry == null) {
      renderer = HardcodedValues.DEFAULT_RENDERER;
    } else {
      renderer = info.rendentry.renderer;
    }
    
    List<TileInfo> ents = entries.get(renderer);
    TileInfo oldent = rendindex.get(info.absposition);
    TileRenderer newrend = instances.get(renderer);
    
    // create the renderer if needed
    if(newrend == null) {
      newrend = getRenderer(renderer);
      instances.put(renderer, newrend);
    }
    
    //create the list of tileinfo.
    if(ents == null) {
      ents = new CopyOnWriteArrayList<>();
      entries.put(renderer, ents);
    } else {
      ents.remove(oldent);
    }
    
    // Update the previous renderer if needed
    if(oldent != null) {
    
      //ues the asociated renderer if avalaible otherwise use the default one.
      Class<? extends TileRenderer> oldrendclass;
      if(oldent.rendentry == null) {
        oldrendclass = HardcodedValues.DEFAULT_RENDERER;
      } else {
        oldrendclass = oldent.rendentry.renderer;
      }
      
      TileRenderer oldrend = instances.get(oldrendclass);
      if(oldrend != newrend){
        List<TileInfo> oldents = entries.get(oldrendclass);

        oldents.remove(oldent);
        dirtyents.put(oldrendclass, true);
      }
    }
    
    ents.add(info);
    rendindex.put(info.absposition, info);
    dirtyents.put(newrend.getClass(), true);
  }

  @Override
  public void regionUnloading(Region reg) {
  }

  @Override
  public void regionUpdated(Region reg) {
  }

  /**
   * Returns the {@link Coordinate} for the bottom souther western {@link Tile}
   * of the {@link Region}.
   * 
   * @return The corner of the {@link Region}.
   */
  public Coordinate getLocation() {
    return reg.getLocation();
  }

  protected void update() {    
    if(dirty)
      scanRegion();
    
    for(Map.Entry<Class<? extends TileRenderer>, Boolean> ent : dirtyents.entrySet()) {
      if(ent.getValue()) {
        ent.setValue(false);
        TileRenderer rend = instances.get(ent.getKey());
        List<TileInfo> tiles = entries.get(ent.getKey());
        
        rend.compile(tiles);
      }
    }
    
    dirtyents.clear();
  }

  protected void render(int pass, ReadableMatrix4 proj, ReadableMatrix4 modelview) {
    for(TileRenderer rend : instances.values())
      rend.render(pass, proj, modelview);
  }
  
  private boolean dirty;
  private final Region reg;
  
  private TileRenderer getRenderer(Class<? extends TileRenderer> clazz) {
    TileRenderer rend = null;
    
    Constructor<TileRenderer> cons;
    //try for a constructor with a regionrenderer argument
    try {
      cons = (Constructor<TileRenderer>) clazz.getConstructor(RegionRenderer.class);
      rend = cons.newInstance(this);
    } catch (
      InstantiationException |
      NoSuchMethodException e) {
      //try a no argument constructor if the first does not work
      try {
        rend = clazz.newInstance();
      } catch (InstantiationException | IllegalAccessException ex) {
        throw new RuntimeException("Could not create tile renderer.", ex);
      }
    } catch (
      SecurityException |
      IllegalAccessException | 
      IllegalArgumentException | 
      InvocationTargetException ex) {
      throw new RuntimeException("Could not create tile renderer.", ex);
    }
      
    return rend;
  }
  
  //cached data for static rendering.
  private final Map<Coordinate, TileInfo> rendindex;
  private final Map<Class<? extends TileRenderer>, List<TileInfo>> entries;
  private final Map<Class<? extends TileRenderer>, Boolean> dirtyents;
  private final Map<Class<? extends TileRenderer>, TileRenderer> instances;
  
  private static final String locprefix = RegionRenderer.class.getName().toLowerCase();
  
  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
