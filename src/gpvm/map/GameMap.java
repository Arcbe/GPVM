package gpvm.map;

import gpvm.util.geometry.Coordinate;
import gpvm.util.geometry.Direction;
import java.util.HashMap;

/**
 * Contains all of the data for a 3D map used by the game.  This class will
 * also control the loading and unloading or regions of the map.
 * 
 * @author russell
 */
public class GameMap {
  /**
   * Various modes that determine how and when a region is loaded by the game
   * map.
   */
  public enum LoadingMode {
    /**
     * Loads regions immediately upon request.
     */
    Immediate,
    /**
     * Loads regions on a independent thread, returns null for unavailable regions.
     */
    async,
    /**
     * Loads regions on an independent thread but also loads adjacent regions.
     */
    buffered
  }

  public GameMap(MapGenerator gen) {
    assert gen != null;
    generator = gen;
    lmode = LoadingMode.Immediate;
    loadedregions = new HashMap<>();
  }
  
  /**
   * Sets the loading mode that this map will use.
   * 
   * @param mode The loading mode for the map.
   */
  public void setLoadingMode(LoadingMode mode) {
    lmode = mode;
  }
  
  /**
   * Returns a region of the map. Depending on loading mode this method may
   * return null if the region is not currently loaded.
   * 
   * @param coor The coordinate of a block inside the region
   * @return The region of the map containing the given coordinate or null.
   */
  public Region getRegion(Coordinate coor) {
    Coordinate rc = coor.getRegionCoordiante();
    Region result = loadedregions.get(rc);
    if(result != null) return result;
    
    if(lmode == LoadingMode.Immediate)
      return loadRegion(rc);
    else
      //TODO: add loading code for other modes.
      return null;
  }

  /**
   * Cause a region to be loaded into memory. depending on loading mode
   * this will either return the new region, or null if a delay loading
   * mode is selected.
   * 
   * @return The new region or null
   */
  public Region loadRegion(Coordinate coor) {
    assert generator != null;
    assert coor != null;
    
    //collect all of the neighboring regions
    Region[] nbregions = new Region[6];
    
    coor.x += 1;
    if(loadedregions.containsKey(coor)) {
      nbregions[Direction.West.getIndex()] = loadedregions.get(coor);
    }
    
    coor.x -= 2;
    if(loadedregions.containsKey(coor)) {
      nbregions[Direction.East.getIndex()] = loadedregions.get(coor);
    }
    
    coor.x += 1;
    coor.y += 1;
    if(loadedregions.containsKey(coor)) {
      nbregions[Direction.North.getIndex()] = loadedregions.get(coor);
    }
    
    coor.y -= 2;
    if(loadedregions.containsKey(coor)) {
      nbregions[Direction.South.getIndex()] = loadedregions.get(coor);
    }
    
    coor.y += 1;
    coor.z += 1;
    if(loadedregions.containsKey(coor)) {
      nbregions[Direction.Up.getIndex()] = loadedregions.get(coor);
    }
    
    coor.z -= 2;
    if(loadedregions.containsKey(coor)) {
      nbregions[Direction.West.getIndex()] = loadedregions.get(coor);
    }
    
    //finally generate the region
    coor.z += 1;
    Region newregion = generator.generateRegion(coor, nbregions);
    loadedregions.put(coor, newregion);
    return newregion;
  }
  
  private LoadingMode lmode;
  private MapGenerator generator;
  private HashMap<Coordinate, Region> loadedregions;
}
