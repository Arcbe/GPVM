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

package taiga.gpvm.map;

import taiga.gpvm.util.geom.Coordinate;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.registry.TileEntry;
import taiga.gpvm.registry.TileRegistry;

/**
 *
 * @author russell
 */
public final class Region {
  /**
   * Each region will be a cube with edges of length REGION_SIZE.  This
   * must be a power of 2, because simplicity.
   */
  public static final byte REGION_SIZE = 32;
  private final Coordinate location;
  private final World map;
  
  /**
   * Constructs a region with only empty {@link Tile}.
   * 
   * @param loc The location in the map where this region lies.
   * @param parent The {@link World} that contains this {@link Region}.
   */
  public Region(Coordinate loc, World parent) {
    map = parent;
    
    tiles = new Tile[REGION_SIZE * REGION_SIZE * REGION_SIZE];
    location = loc;
    
    for(int i = tiles.length - 1; i >= 0; i--)
      tiles[i] = new Tile();
    
    listeners = new ConcurrentLinkedQueue<>();
  }
  
  /**
   * Constructs a region with the given {@link Tile} data.
   * 
   * @param data The data to use when creating the {@link Region}, the data should be
   * laid out using the function z * REGION_SIZE ^ 2 + x * REGION_SIZE + y
   * @param loc The location of the region in the map.
   * @param parent The {@link World} that contains this {@link Region}.
   */
  public Region(Tile[] data, Coordinate loc, World parent) {
    map = parent;
    assert data.length == REGION_SIZE * REGION_SIZE * REGION_SIZE;
    
    for(int i = 0; i < data.length; i++) {
      if(data[i] == null)
        data[i] = new Tile();
    }
    
    tiles = data;
    location = loc;
    listeners = new ConcurrentLinkedQueue<>();
  }
  
  /**
   * Adds a listener for events occurring in the {@link Region}.
   * 
   * @param list The listener to attach to the {@link Region}
   */
  public void addListener(RegionListener list) {
    listeners.add(list);
  }
  
  /**
   * Returns the location of the {@link Region} in the map.  This is the
   * same {@link Coordinate} that was given in the constructor.
   * 
   * @return The location of the {@link Region}
   */
  public Coordinate getLocation() {
    return location;
  }
  
  /**
   * Returns the parent {@link World} containing this {@link Region}.
   * 
   * @return The parent {@link World}.
   */
  public World getWorld() {
    return map;
  }
  
  /**
   * Retrieves the tile at the given point.  The location is relative to the
   * origin of the region and not the origin of the map.
   * 
   * @param x The x coordinate of the tile.
   * @param y The y coordinate of the tile.
   * @param z The z coordinate of the tile.
   * @return The tile at the given point.
   */
  public final Tile getTile(int x, int y, int z) {
    assert x < REGION_SIZE;
    assert y < REGION_SIZE;
    assert z < REGION_SIZE;
    assert x > 0;
    assert y > 0;
    assert z > 0;
    
    return tiles[(z * REGION_SIZE * REGION_SIZE) | (x * REGION_SIZE) | y];
  }
  
  /**
   * Informs the {@link Region} that is being unloaded.
   */
  public void unload() {
    for(RegionListener list : listeners)
      list.regionUnloading(this);
  }
  
  protected final void setTile(int x, int y, int z, int type) {
    if(treg == null)
      treg = map.getObject(HardcodedValues.TILE_REGISTRY_NAME);
    
    setTile(x, y, z, treg.getEntry(type));
  }
  
  protected final void setTile(int x, int y, int z, TileEntry type) {
    tiles[(z * REGION_SIZE * REGION_SIZE) | (x * REGION_SIZE) | y].type = type;
  }
  
  private TileRegistry treg;
  private Collection<RegionListener> listeners;
  private Tile[] tiles;
}
