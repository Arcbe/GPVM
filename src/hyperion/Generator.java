/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperion;

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
    Region result = new Region(coor);
    
    //create the ground
    if(coor.z == 0) {
      for(int i = 0; i < Region.REGION_SIZE; i++) {
        for(int j = 0; j < Region.REGION_SIZE; j++) {
          result.getTile(i, j, 0).type = 1;
        }
      }
    //create the below ground
    } else if (coor.z < 0) {
      
    }
    
    return result;
  }
}
