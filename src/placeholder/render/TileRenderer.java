/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package placeholder.render;

import java.util.Collection;
import java.util.Map;

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
  
  /**
   * Returns a list of all data fields available for rendering.  These are
   * the entries that can be used in the rendering data file for tiles.
   * 
   * @return A set of field names.
   */
  public Collection<String> getVariableNames() {
    return null;
  }
  
  /**
   * Sets the indices for the data fields used by the renderer.  The data
   * fields are assigned to integers in the TileInfo and must be accessed that
   * way.
   * 
   * @param mappings The mappings for the data fields that will be used.
   */
  public void setVariableBindings(Map<String, Integer> mappings) {}
}
