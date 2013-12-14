/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperion;

import gpvm.Registrar;
import gpvm.map.MapGenerator;
import gpvm.map.Region;
import gpvm.map.Tile;
import gpvm.util.geometry.Coordinate;

/**
 *
 * @author russell
 */
public class Generator implements MapGenerator {
  @Override
  public Region generateRegion(Coordinate coor, Region[] neighbors) {
    initvariables();
    
    //create the ground
    Tile[] data = new Tile[Region.REGION_SIZE * Region.REGION_SIZE * Region.REGION_SIZE];
    if(coor.z == 0) {
      for(int i = 0; i < Region.REGION_SIZE; i++) {
        for(int j = 0; j < Region.REGION_SIZE; j++) {
          Tile t = new Tile();
          if((i + j) % 2 == 0)
            t.type = grass;
          else
            t.type = stone;
          data[j * Region.REGION_SIZE + i] = t;
        }
      }
    //create the below ground
    } else if (coor.z < 0) {
      
    }
    
    return new Region(data, coor);
  }
  
  private void initvariables() {
    if(init) return;
    
    init = true;
    grass = Registrar.getInstance().getTileRegistry().getTileID("hyperion-base.Grass");
    stone = Registrar.getInstance().getTileRegistry().getTileID("hyperion-base.Stone");
  }
  
  private static boolean init = false;
  private static long grass;
  private static long stone;
}
