/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm.map;

import taiga.gpvm.map.RegionListener;
import taiga.gpvm.map.Tile;
import gpvm.util.geometry.Coordinate;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author russell
 */
public final class Region {
  /**
   * Each region will be a cube with edges of length REGION_SIZE.
   */
  public static final byte REGION_SIZE = 32;
  private final Coordinate location;
  private final GameMap map;
  
  /**
   * Constructs a region with only empty {@link Tile}.
   * 
   * @param loc The location in the map where this region lies.
   */
  public Region(Coordinate loc, GameMap parent) {
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
   * @param data The data to use when creating the {@link Region}
   * @param loc The location of the region in the map.
   */
  public Region(Tile[] data, Coordinate loc, GameMap parent) {
    map = parent;
    assert data.length == REGION_SIZE * REGION_SIZE * REGION_SIZE;
    
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
  
  public GameMap getGameMap() {
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
  public Tile getTile(int x, int y, int z) {
    assert x < REGION_SIZE;
    assert y < REGION_SIZE;
    assert z < REGION_SIZE;
    assert x > 0;
    assert y > 0;
    assert z > 0;
    
    return tiles[z * REGION_SIZE * REGION_SIZE + x * REGION_SIZE + y];
  }
  
  /**
   * Informs the {@link Region} that is being unloaded.
   */
  public void unload() {
    Iterator<RegionListener> it = listeners.iterator();
    while(it.hasNext()) {
      it.next().regionUnloading(this);
    }
  }
  
  private Collection<RegionListener> listeners;
  private Tile[] tiles;
}
