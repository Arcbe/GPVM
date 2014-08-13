/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm.map;

import taiga.gpvm.HardcodedValues;
import taiga.code.registration.NamedObject;
import taiga.gpvm.util.geom.Coordinate;

/**
 * An interface for classes that can generate Regions for the game map.
 * 
 * @author russell
 */
public abstract class MapGenerator extends NamedObject {

  /**
   * Creates a new {@link MapGenerator}.
   */
  public MapGenerator() {
    super(HardcodedValues.MAP_GENERATOR_NAME);
  }
  /**
   * Generates a single region.  The methods takes in a coordinate for the
   * the bottom south-west most tile of the region, and a list of adjacent regions.
   * 
   * @param coor The coordinate of the region
   * @param parent The world that the generated {@link Region} should be a part of.
   * @return The generated region.
   */
  public abstract Region generateRegion(Coordinate coor, World parent);
}
