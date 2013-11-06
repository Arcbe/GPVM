/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.render;

import java.util.Collection;

/**
 * Converts a set of tiles into a RawBatch that can be then be rendered.
 * 
 * @author russell
 */
public abstract class TileRenderer {
  
  /**
   * Converts a set of tiles into a set of renderable batches of vertices.
   * 
   * @param info The information for each tile that the batch should render.
   * @return The batches that can be used to render the given tiles.
   */
  public abstract RawBatch[] batchTiles(Collection<TileInfo> info);
  
  /**
   * Returns the class that will be used to stored rending information for
   * tiles that use this renderer.  A null value indicates that there is no
   * addition rendering information for the tiles.
   * 
   * @return The class of the RenderInfo used for this renderer.
   */
  public abstract Class<? extends RenderInfo> getRenderInfo();
}
