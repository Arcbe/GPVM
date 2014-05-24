/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperion;

import taiga.gpvm.HardcodedValues;
import taiga.gpvm.map.MapGenerator;
import taiga.gpvm.map.Region;
import taiga.gpvm.map.Tile;
import gpvm.util.geometry.Coordinate;
import taiga.gpvm.map.GameMap;
import taiga.gpvm.registry.TileEntry;

/**
 *
 * @author russell
 */
public class Generator extends MapGenerator {
  @Override
  public Region generateRegion(Coordinate coor) {
    initvariables();
    
    //create the ground
    Tile[] data = new Tile[Region.REGION_SIZE * Region.REGION_SIZE * Region.REGION_SIZE];
    if((coor.z / Region.REGION_SIZE + coor.x / Region.REGION_SIZE + coor.y / Region.REGION_SIZE) % 2 == 0) {
      for(int i = 0; i < Region.REGION_SIZE; i++) {
        for(int j = 0; j < Region.REGION_SIZE; j++) {
          Tile t = new Tile();
          switch((i + j) % 4) {
            case 0:
              t.type = grass;
              break;
            case 1:
              t.type = stone;
              break;
            case 2:
              t.type = water;
              break;
            case 3:
              t.type = lava;
              break;
          }
          data[i * Region.REGION_SIZE * Region.REGION_SIZE + j * Region.REGION_SIZE + i] = t;
        }
      }
    //create the below ground
    } else if (coor.z < 0) {
      
    }
    
    return new Region(data, coor, (GameMap) getObject(HardcodedValues.GAME_MAP_NAME));
  }
  
  private void initvariables() {
    if(init) return;
    
    init = true;
  }
  
  private static boolean init = false;
  private static TileEntry grass;
  private static TileEntry lava;
  private static TileEntry water;
  private static TileEntry stone;
}
