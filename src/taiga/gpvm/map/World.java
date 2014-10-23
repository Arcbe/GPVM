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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.networking.Packet;
import taiga.code.registration.NamedObject;
import taiga.code.registration.ReusableObject;
import taiga.code.util.ByteUtils;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.util.geom.Direction;

/**
 * A uniform 3D grid of tiles.  This class can load and unloaded individual
 * {@link Region}.
 * 
 * @author russell
 */
public final class World extends ReusableObject {
  
  /**
   * Creates a new {@link World} with the given name.
   * 
   * @param name The name of hte new {@link World}.
   */
  public World(String name) {
    super(name);
    
    listeners = new HashSet<>();
    regions = new HashMap<>();
    regionlock = new ReentrantReadWriteLock();
  }
  
  /**
   * Returns a region of the map. Depending on loading mode this method may
   * return null if the region is not currently loaded.
   * 
   * @param coor The coordinate of a block inside the region
   * @return The region of the map containing the given coordinate or null.
   */
  public Region getRegion(Coordinate coor) {
    Coordinate rc = coor.getRegionCoordinate();
    return regions.get(rc);
  }
  
  public Collection<Region> getRegions() {
    return Collections.unmodifiableCollection(regions.values());
  }
  
  /**
   * Returns the {@link Tile} at the given coordinate.  If the tile is 
   * no in a loaded chunk then this may return null depending on the loading mode.
   * 
   * @param coor The coordinate of the requested {@link Tile}
   * @return The {@link Tile} at the given coordinate.
   */
  public Tile getTile(Coordinate coor) {
    Region reg = getRegion(coor);
    
    if(reg == null) return null;
    
    byte x = (byte) (coor.x % Region.REGION_SIZE);
    byte y = (byte) (coor.y % Region.REGION_SIZE);
    byte z = (byte) (coor.z % Region.REGION_SIZE);
    
    //make sure that they are not negative
    if(x < 0) x += Region.REGION_SIZE;
    if(y < 0) y += Region.REGION_SIZE;
    if(z < 0) z += Region.REGION_SIZE;
    
    return reg.getTile(x, y, z);
  }
  
  /**
   * Returns an array of all of the neighboring {@link Tile}s of a
   * {@link Coordinate}.  The indices of the {@link Tile}s are the values from
   * the {@link Direction} enum.
   * 
   * @param coor The coordinate to look up the neighbors for.
   * @return The neighboring {@link Tile}s of the given coordinate.
   * @see Direction#getIndex()
   */
  public Tile[] getNeighborTiles(Coordinate coor) {
    Tile[] tiles = new Tile[6];
    Coordinate tar = new Coordinate();
    
    tar.x = coor.x + 1;
    tar.y = coor.y;
    tar.z = coor.z;
    tiles[Direction.East.getIndex()] = getTile(tar);
    
    tar.x = coor.x - 1;
    tar.y = coor.y;
    tar.z = coor.z;
    tiles[Direction.West.getIndex()] = getTile(tar);
    
    tar.x = coor.x;
    tar.y = coor.y + 1;
    tar.z = coor.z;
    tiles[Direction.North.getIndex()] = getTile(tar);
    
    tar.x = coor.x;
    tar.y = coor.y - 1;
    tar.z = coor.z;
    tiles[Direction.South.getIndex()] = getTile(tar);
    
    tar.x = coor.x;
    tar.y = coor.y;
    tar.z = coor.z + 1;
    tiles[Direction.Up.getIndex()] = getTile(tar);
    
    tar.x = coor.x;
    tar.y = coor.y;
    tar.z = coor.z - 1;
    tiles[Direction.Down.getIndex()] = getTile(tar);
    
    return tiles;
  }
  
  /**
   * Adds a {@link WorldListener} to this {@link World}.
   * @param list The {@link WorldListener} to add.
   */
  public void addListener(WorldListener list) {
    listeners.add(list);
  }
  
  /**
   * Removes a previously added {@link WorldListener} from this {@link World}.
   * @param list The {@link WorldListener} to remove.
   */
  public void removeListener(WorldListener list) {
    listeners.remove(list);
  }
  
  /**
   * Loads the {@link Region} that contains the given {@link Coordinate}.
   * 
   * @param coor A {@link Coordinate} within the {@link Region} to load.
   */
  public void loadRegion(Coordinate coor) {
    coor = coor.getRegionCoordinate();
    if(isLoaded(coor)) return;
    
    if(isServer()) {
      //try loading from a file first.
      if(loadRegionFile(coor)) {
        log.log(Level.FINE, REGION_FILE_LOADED, new Object[]{getFullName(), coor});
        return;
      }
      
      //otherwise get the generator and generate the region.
      NamedObject obj = getObject(HardcodedValues.NAME_MAP_GENERATOR);
      
      if(obj == null || !(obj instanceof MapGenerator)) {
        log.log(Level.WARNING, NO_GENERATOR, getFullName());
        return;
      }
      
      Region nreg = ((MapGenerator)obj).generateRegion(coor, this);
      
      addRegion(nreg);
      
      log.log(Level.FINE, REGION_GENERATED, new Object[]{getFullName(), coor});
      fireRegionLoaded(nreg);
    } else {
      sendRegionRequest(coor);
    }
  }
  
  /**
   * Checks to see if the {@link Region} containing the given {@link Coordinate}
   * is loaded.
   * 
   * @param coor The {@link Coordinate} to check.
   * @return Whether the {@link Region} is loaded.
   */
  public boolean isLoaded(Coordinate coor) {
    regionlock.readLock().lock();
    try {
      return regions.containsKey(coor);
    } finally {
      regionlock.readLock().unlock();
    }
  }
  
  /**
   * Returns the id for this {@link World}.  This id is intended for network usage
   * and may change as a result of network activity.
   * 
   * @return The current id for this {@link World}.
   */
  public short getWorldID() {
    return worldid;
  }

  @Override
  public int hashCode() {
    return worldid;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final World other = (World) obj;
    if (this.worldid != other.worldid) {
      return false;
    }
    return true;
  }
  
  protected WorldMutator mutator;

  @Override
  protected void resetObject() {
    try {
      regionlock.writeLock().lock();
      
      regions.clear();
      listeners.clear();
    } finally {
      regionlock.writeLock().unlock();
    }
  }
  
  protected void setID(short id) {
    worldid = id;
  }
  
  protected void addRegion(Region reg) {
    regionlock.writeLock().lock();
    try {
      regions.put(reg.getLocation(), reg);
    } finally {
      regionlock.writeLock().unlock();
    }
  }
  
  private boolean loadRegionFile(Coordinate coor) {
    return false;
  }

  private void sendRegionRequest(Coordinate coor) {
    Packet pack = new Packet();
    
    pack.data = new byte[15];
    pack.data[0] = Universe.Comms.REG_REQ;
    
    ByteUtils.toBytes(coor.x, 1, pack.data);
    ByteUtils.toBytes(coor.y, 5, pack.data);
    ByteUtils.toBytes(coor.z, 9, pack.data);
    ByteUtils.toBytes(getWorldID(), 13, pack.data);
    
    Universe.Comms comms = getObject(HardcodedValues.NAME_COMMS);
    comms.sendMessage(pack);
  }

  private boolean isServer() {
    Universe.Comms comms = getObject(HardcodedValues.NAME_COMMS);
    
    return comms == null || 
      comms.getNetworkManager() == null ||
      !comms.getNetworkManager().isConnected() ||
      comms.getNetworkManager().isServer();
  }
  
  private void fireRegionLoaded(Region reg) {
    for(WorldListener list : listeners)
      list.regionLoaded(reg);
  }
  
  private void fireRegionUnloaded(Region reg) {
    for(WorldListener list : listeners)
      list.regionUnloaded(reg);
  }
  
  private final Map<Coordinate, Region> regions;
  private final ReadWriteLock regionlock;
  private final Collection<WorldListener> listeners;
  private short worldid;
  
  private static final String locprefix = World.class.getName().toLowerCase();
  
  private static final String NO_GENERATOR = locprefix + ".no_generator";
  private static final String REGION_GENERATED = locprefix + ".region_generated";
  private static final String REGION_FILE_LOADED = locprefix + ".region_file_loaded";
  
  private static final Logger log = Logger.getLogger(locprefix, 
    System.getProperty("taiga.code.logging.text"));
}
