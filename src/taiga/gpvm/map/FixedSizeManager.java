package taiga.gpvm.map;

import taiga.gpvm.util.geom.Coordinate;

/**
 * This {@link RegionManager} will load {@link Regions} in a fixed area.  All
 * {@link Region}s in the area will be loaded immediately.
 * 
 * @author russell
 */
public class FixedSizeManager extends RegionManager {

  /**
   * Creates a new {@link FixedSizeManager} for the rectangular area defined by
   * the given {@link Coordinate} and size.  If the distances or {@link Coordinate}
   * do not perfectly align with {@link Region} boundaries then the actual loaded
   * area will be larger than defined one.
   * 
   * @param corner The corner {@link Coordinate} of the area to load.
   * @param length The distance in the x direction for the area.
   * @param width The distance in the y direction for the area.
   * @param height The distance in the z direction for the area.
   */
  public FixedSizeManager(Coordinate corner, int length, int width, int height) {
    start = corner.getRegionCoordinate();
    end = corner.add(length, width, height, new Coordinate());
  }

  @Override
  protected void mapAttached(World map) {
    Coordinate target = new Coordinate();
    
    for(int i = start.x; i < end.x; i += Region.REGION_SIZE) {
      for(int j = start.y; j < end.y; j += Region.REGION_SIZE) {
        for(int k = start.z; k < end.z; k += Region.REGION_SIZE) {
          target.x = i;
          target.y = j;
          target.z = k;
          
          map.loadRegion(target);
        }
      }
    }
  }
  
  private final Coordinate start;
  private final Coordinate end;
}
