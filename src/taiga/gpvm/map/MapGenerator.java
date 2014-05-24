/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm.map;

import taiga.gpvm.HardcodedValues;
import taiga.gpvm.map.Region;
import gpvm.util.geometry.Coordinate;
import taiga.code.registration.RegisteredObject;

/**
 * An interface for classes that can generate Regions for the game map.
 * 
 * @author russell
 */
public abstract class MapGenerator extends RegisteredObject {

  public MapGenerator() {
    super(HardcodedValues.MAP_GENERATOR_NAME);
  }
  /**
   * Generates a single region.  The methods takes in a coordinate for the
   * the bottom southwestmost tile of the region, and a list of adjacent regions.
   * 
   * @param coor The coordinate of the region
   * @param neighbors any neighboring regions, indexed by direction.
   * @return The generated region.
   */
  public abstract Region generateRegion(Coordinate coor);
}
