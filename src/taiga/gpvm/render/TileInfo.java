/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm.render;

import gpvm.map.Tile;
import gpvm.map.TileDefinition;
import gpvm.util.geometry.Coordinate;

/**
 * Contains data for rendering a tile.  This will contain information about
 * the relative and absolute position on the tile, any attached rendering info
 * and a list of adjacent tiles.
 * 
 * @author russell
 */
public class TileInfo {
  /**
   * The absolute position of the tile in the map.  The origin for this is
   * the same as the origin for the map.
   */
  public Coordinate absolutepos;
  
  /**
   * The position that the tile should be rendered relative to the model's origin.
   * Typically this will be the position within the region of the tile.
   */
  public Coordinate relativepos;
  
  /**
   * Defines the various attributes of the tile.
   */
  public TileDefinition definition;
  
  /**
   * A reference to the actual tile within the map.
   */
  public Tile tile;
  
  /**
   * Any additional information for rendering the tile.  This information
   * will be supplied externally and will be of the same type as requested by the
   * TileRenderer associated with the tile.  If this is null then there was no
   * information associated with the tile.
   */
  public RenderInfo info;
  
  /**
   * An array of tiles that are adjacent to the tile being rendered. The
   * indices correspond to the indices in the Direction enum.
   */
  public Tile[] adjacenttiles = new Tile[6];
}
