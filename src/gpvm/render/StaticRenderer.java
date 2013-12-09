/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.render;

import gpvm.map.Tile;

/**
 * Interface for all renders that render static tiles.  For most cases this
 * means tiles that do not change appearance, however limited changing is supported
 * through the use of a {@link RenderingState} added to {@link RawBatch} that is
 * generated.  One example is using a {@link RenderingState} that transforms
 * texture coordinates as time progresses.
 * 
 * A single {@link StaticRenderer} may be used to render multiple {@link Tile}s.
 * 
 * @author russell
 */
public interface StaticRenderer extends TileRenderer {
  /**
   * Renders a single {@link Tile}.  This method creates {@link RawBatch} that
   * can be used to render the given tile.
   * @param info The information for the tile to be rendered.
   * @return A {@link RawBatch} that can be used to render the given {@link Tile}
   */
  public RawBatch render(TileInfo info);
}
