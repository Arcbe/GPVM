package taiga.gpvm.map;

import taiga.code.util.geom.Coordinate;
import gpvm.util.geometry.Direction;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.networking.NetworkedObject;
import taiga.code.networking.Packet;
import taiga.code.registration.RegisteredObject;
import taiga.gpvm.HardcodedValues;

/**
 * A uniform 3D grid of tiles.  This class can load and unloaded individual
 * {@link Region}.
 * 
 * @author russell
 */
public final class World extends NetworkedObject {
  
  /**
   * Creates a new {@link World} with the given name.
   * 
   * @param name The name of hte new {@link World}.
   */
  public World(String name) {
    super(name);
    
    listeners = new ArrayList<>();
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
    
    coor.x += 1;
    tiles[Direction.East.getIndex()] = getTile(coor);
    
    coor.x -= 2;
    tiles[Direction.West.getIndex()] = getTile(coor);
    
    coor.x += 1;
    coor.y += 1;
    tiles[Direction.North.getIndex()] = getTile(coor);
    
    coor.y -= 2;
    tiles[Direction.South.getIndex()] = getTile(coor);
    
    coor.y += 1;
    coor.z += 1;
    tiles[Direction.Up.getIndex()] = getTile(coor);
    
    coor.z -= 2;
    tiles[Direction.Down.getIndex()] = getTile(coor);
    coor.z += 1;
    
    return tiles;
  }
  
  /**
   * Adds a {@link WorldListener} to this {@link World}.
   * @param list The {@link WorldListener} to add.
   */
  public void addListener(WorldListener list) {
    listeners.add(new WeakReference<>(list));
  }
  
  /**
   * Removes a previously added {@link WorldListener} from this {@link World}.
   * @param list The {@link WorldListener} to remove.
   */
  public void removeListener(WorldListener list) {
    for(WeakReference<WorldListener> listener : listeners)
      if(listener.get() == list) {
        listeners.remove(listener);
        return;
      }
  }
  
  /**
   * Loads the {@link Region} that contains the given {@link Coordinate}.
   * 
   * @param coor A {@link Coordinate} within the {@link Region} to load.
   */
  public void loadRegion(Coordinate coor) {
    coor = coor.getRegionCoordinate();
    if(isLoaded(coor)) return;
    
    if(getManager() != null &&
      (getManager().isServer() ||
      !getManager().isConnected())) {
      //try loading from a file first.
      if(loadRegionFile(coor)) {
        log.log(Level.FINE, REGION_FILE_LOADED, new Object[]{getFullName(), coor});
        return;
      }
      
      //otherwise get the generator and generate the region.
      RegisteredObject obj = getObject(HardcodedValues.MAP_GENERATOR_NAME);
      
      if(obj == null || !(obj instanceof MapGenerator)) {
        log.log(Level.WARNING, NO_GENERATOR, getFullName());
        return;
      }
      
      Region nreg = ((MapGenerator)obj).generateRegion(coor, this);
      
      try {
        regionlock.writeLock().lock();
        regions.put(coor, nreg);
      } finally {
        regionlock.writeLock().unlock();
      }
      
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
    try {
      regionlock.readLock().lock();
      return regions.containsKey(coor);
    } finally {
      regionlock.readLock().unlock();
    }
  }
  
  protected WorldMutator mutator;

  @Override
  protected void connected() {
  }

  @Override
  protected void messageRecieved(Packet pack) {
  }

  @Override
  protected void managerAttached() {
  }
  
  private List<WeakReference<WorldListener>> listeners;
  
  private boolean loadRegionFile(Coordinate coor) {
    return false;
  }

  private void sendRegionRequest(Coordinate coor) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  private void fireRegionLoaded(Region reg) {
    for(WeakReference<WorldListener> ref : listeners) {
      WorldListener list = ref.get();
      
      if(list != null) {
        list.regionLoaded(reg);
      } else {
        listeners.remove(ref);
      }
    }
  }
  
  private void fireRegionUnoaded(Region reg) {
    for(WeakReference<WorldListener> ref : listeners) {
      WorldListener list = ref.get();
      
      if(list != null) {
        list.regionUnloaded(reg);
      } else {
        listeners.remove(ref);
      }
    }
  }
  
  private Map<Coordinate, Region> regions;
  private ReadWriteLock regionlock;
  
  private static final String locprefix = World.class.getName().toLowerCase();
  
  private static final String NO_GENERATOR = locprefix + ".no_generator";
  private static final String REGION_GENERATED = locprefix + ".region_generated";
  private static final String REGION_FILE_LOADED = locprefix + ".region_file_loaded";
  
  private static final Logger log = Logger.getLogger(locprefix, 
    System.getProperty("taiga.code.logging.text"));
}
