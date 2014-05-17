package taiga.gpvm.render;

import gpvm.map.Tile;
import gpvm.util.geometry.Coordinate;
import gpvm.util.geometry.Direction;

/**
 * A descriptor of basic information about a {@link Tile} for use in rendering.
 * 
 * @author russell
 */
public class TileInfo {
  /**
   * The position in the world of the {@link Tile} associated with this {@link TileInfo}.
   * When being rendered however, the tile should be positioned relative to the
   * origin of the chunk it is contained in.
   */
  public Coordinate absposition;
  
  /**
   * The {@link Tile} associated with this {@link TileInfo}
   */
  public Tile tile;
  
  /**
   * Adjacent {@link Tile}s to the associated {@link Tile}.  Some or all may be
   * null indicating that the adjacent {@link Tile} is not currently known.  The
   * order of {@link Tile}s in the array is the same as the value for elements
   * of the {@link Direction} enumeration.
   */
  public Tile[] adjacent;
}
