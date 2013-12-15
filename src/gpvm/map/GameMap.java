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

  /**
   * Creates a new map for the game.
   * 
   * @param gen The generator to use for loading new {@link Region}s of the map.
   */
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
   * Returns a region of the map. Depending on loading mode this method may
   * return null if the region is not currently loaded.
   * 
   * @param coor The coordinate of a block inside the region
   * @return The region of the map containing the given coordinate or null.
   */
  public Region getRegion(Coordinate coor) {
    Coordinate rc = coor.getRegionCoordinate();
    Region result = loadedregions.get(rc);
    if(result != null) return result;
    
    if(lmode == LoadingMode.Immediate)
      return loadRegion(rc);
    else
      //TODO: add loading code for other modes.
      return null;
  }
  
  /**
   * Returns an array of all of the neighboring {@link Region}s of a
   * {@link Coordinate}.  The indices of the {@link Tile}s are the values from
   * the {@link Direction} enum.
   * 
   * @param loc The coordinate to look up the neighbors for.
   * @return The neighboring {@link Region}s of the given coordinate.
   * @see Direction#getIndex()
   */
  public Region[] getNeighborRegions(Coordinate loc) {
    //collect all of the neighboring regions
    Coordinate coor = loc.getRegionCoordinate();
    Region[] nbregions = new Region[6];
    
    coor.x += Region.REGION_SIZE;
    if(loadedregions.containsKey(coor)) {
      nbregions[Direction.West.getIndex()] = loadedregions.get(coor);
    }
    
    coor.x -= 2 * Region.REGION_SIZE;
    if(loadedregions.containsKey(coor)) {
      nbregions[Direction.East.getIndex()] = loadedregions.get(coor);
    }
    
    coor.x += Region.REGION_SIZE;
    coor.y += Region.REGION_SIZE;
    if(loadedregions.containsKey(coor)) {
      nbregions[Direction.North.getIndex()] = loadedregions.get(coor);
    }
    
    coor.y -= 2 * Region.REGION_SIZE;
    if(loadedregions.containsKey(coor)) {
      nbregions[Direction.South.getIndex()] = loadedregions.get(coor);
    }
    
    coor.y += Region.REGION_SIZE;
    coor.z += Region.REGION_SIZE;
    if(loadedregions.containsKey(coor)) {
      nbregions[Direction.Up.getIndex()] = loadedregions.get(coor);
    }
    
    coor.z -= 2 * Region.REGION_SIZE;
    if(loadedregions.containsKey(coor)) {
      nbregions[Direction.West.getIndex()] = loadedregions.get(coor);
    }
    
    return nbregions;
  }

  /**
   * Cause a region to be loaded into memory. depending on loading mode
   * this will either return the new region, or null if a delay loading
   * mode is selected.
   * 
   * @param loc The coordinate of a tile within the region to be loaded.
   * @return The new region or null
   */
  public Region loadRegion(Coordinate loc) {
    assert generator != null;
    assert loc != null;
    
    Coordinate coor = loc.getRegionCoordinate();
    
    //finally generate the region
    Region newregion = generator.generateRegion(coor, getNeighborRegions(coor));
    loadedregions.put(coor, newregion);
    return newregion;
  }
  
  private LoadingMode lmode;
  private MapGenerator generator;
  private HashMap<Coordinate, Region> loadedregions;
}
