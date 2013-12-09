/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.render;

import java.util.Collection;

/**
 * Super interface for class that can render tiles.  This interface is
 * meant only as a marker for class that can render tiles.
 * 
 * @author russell
 */
public interface TileRenderer {
  /**
   * Returns the class that will be used to stored rending information for
   * tiles that use this renderer.  A null value indicates that there is no
   * addition rendering information for the tiles.
   * 
   * @return The class of the RenderInfo used for this renderer.
   */
  public Class<? extends RenderInfo> getRenderInfo();
}
