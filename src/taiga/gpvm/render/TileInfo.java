package taiga.gpvm.render;

import taiga.gpvm.map.Tile;
import taiga.gpvm.util.geom.Coordinate;
import taiga.gpvm.util.geom.Direction;
import taiga.gpvm.registry.TileRenderingEntry;

/**
 * A descriptor of basic information about a {@link Tile} for use in rendering.
 * 
 * @author russell
 */
public final class TileInfo {
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
  
  /**
   * The {@link TileRenderingEntry} for the {@link Tile} described by this {@link TileInfo}.
   */
  public TileRenderingEntry rendentry;
}
