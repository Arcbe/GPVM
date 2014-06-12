/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.map;

import taiga.gpvm.registry.TileEntry;
import taiga.gpvm.util.geom.Coordinate;

/**
 * Generates a {@link World} that consists only of a flat plane of {@link Tile}s
 * of a single type.
 * 
 * @author russell
 */
public class FlatWorldGenerator extends MapGenerator {

  /**
   * Creates a new {@link FlatWorldGenerator} that will create a {@link World}
   * composed of a block of tiles of the given type up to the given height.
   * 
   * @param type The {@link TileEntry} for the {@link Tile}s in the {@link World}.
   * @param height The height for the {@link Tile}s everything at or below this
   * level will be {@link Tiles} of the given type.
   */
  public FlatWorldGenerator(TileEntry type, int height) {
    this.type = type;
    this.height = height;
  }
  
  private final TileEntry type;
  private final int height;

  @Override
  public Region generateRegion(Coordinate coor, World parent) {
    Region result = new Region(coor, parent);
    
    if(coor.z <= height) {
      int h = height - coor.z;
      
      for(int i = 0; i < Region.REGION_SIZE; i++) {
        for(int j = 0; j < Region.REGION_SIZE; j++) {
          for(int k = 0; k <= h && k < Region.REGION_SIZE; k++) {
            result.getTile(i, j, k).type = type;
          }
        }
      }
    }
    
    return result;
  }
}
