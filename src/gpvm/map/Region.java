/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.map;

import gpvm.util.geometry.Coordinate;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 *
 * @author russell
 */
public final class Region {
  /**
   * Each region will be a cube with edges of length REGION_SIZE.
   */
  public static final byte REGION_SIZE = 16;
  private final Coordinate location;
  
  /**
   * Constructs a region with only empty {@link Tile}.
   * 
   * @param loc The location in the map where this region lies.
   */
  public Region(Coordinate loc) {
    tiles = new Tile[REGION_SIZE * REGION_SIZE * REGION_SIZE];
    location = loc;
    
    for(int i = 0; i < tiles.length; i++)
      tiles[i] = new Tile();
    
    listeners = new ConcurrentLinkedDeque<>();
  }
  
  /**
   * Constructs a region with the given {@link Tile} data.
   * 
   * @param data The data to use when creating the {@link Region}
   * @param loc The location of the region in the map.
   */
  public Region(Tile[] data, Coordinate loc) {
    assert data.length == REGION_SIZE * REGION_SIZE * REGION_SIZE;
    tiles = data;
    location = loc;
    listeners = new ConcurrentLinkedDeque<>();
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
   * Retrieves the tile at the given point.  The location is relative to the
   * origin of the region and not the origin of the map.
   * 
   * @param x The x coordinate of the tile.
   * @param y The y coordinate of the tile.
   * @param z The z coordinate of the tile.
   * @return The tile at the given point.
   */
  public Tile getTile(byte x, byte y, byte z) {
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
  
  private ConcurrentLinkedDeque<RegionListener> listeners;
  private Tile[] tiles;
}
