/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fallingsand.terrain;

import gpvm.map.MapGenerator;
import gpvm.map.Region;
import gpvm.util.geometry.Coordinate;

/**
 *
 * @author russell
 */
public class Generator implements MapGenerator {
  @Override
  public Region generateRegion(Coordinate coor, Region[] neighbors) {
    Region result = new Region();
    
    //create the ground
    if(coor.z == 0) {
      
    //create the below ground
    } else if (coor.z < 0) {
      
    }
    
    return result;
  }
}
