/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.map;

import gpvm.util.geometry.Coordinate;

/**
 * An interface for classes that can generate Regions for the game map.
 * 
 * @author russell
 */
public interface MapGenerator {
  /**
   * Generates a single region.  The methods takes in a coordinate for the
   * the bottom southwestmost tile of the region, and a list of adjacent regions.
   * 
   * @param coor The coordinate of the region
   * @param neighbors any neighboring regions, indexed by direction.
   * @return The generated region.
   */
  public Region generateRegion(Coordinate coor, Region[] neighbors);
}
