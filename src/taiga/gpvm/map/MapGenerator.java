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
