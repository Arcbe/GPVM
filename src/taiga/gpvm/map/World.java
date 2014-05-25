/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.map;

import gpvm.util.geometry.Coordinate;
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
 *
 * @author russell
 */
public class World extends NetworkedObject {
  
  public World(String name) {
    super(name);
    
    listeners = new ArrayList<>();
    regions = new HashMap<>();
    regionlock = new ReentrantReadWriteLock();
  }
  
  public void addListener(WorldListener list) {
    listeners.add(new WeakReference<>(list));
  }
  
  public void removeListener(WorldListener list) {
    for(WeakReference<WorldListener> listener : listeners)
      if(listener.get() == list) {
        listeners.remove(listener);
        return;
      }
  }
  
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
      
      Region nreg = ((MapGenerator)obj).generateRegion(coor);
      
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
  
  public boolean isLoaded(Coordinate coor) {
    try {
      regionlock.readLock().lock();
      return regions.containsKey(coor);
    } finally {
      regionlock.readLock().unlock();
    }
  }

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
