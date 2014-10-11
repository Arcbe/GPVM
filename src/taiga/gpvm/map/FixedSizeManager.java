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
