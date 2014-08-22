/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.render;

import java.util.List;
import taiga.code.math.Matrix4;
import taiga.code.math.ReadableMatrix4;
import taiga.gpvm.registry.RenderingInfo;
import taiga.gpvm.registry.TileRenderingRegistry;

/**
 * This interface provides a way for sets of tiles to be rendered as a batch.
 * A list of all tiles that need rendered is compiled and can then be quickly
 * rendered.
 * 
 * @author russell
 */
public interface TileRenderer {
  /**
   * Compiles a {@link List} of tiles into a renderable form.  This method may
   * be called multiple times with differing tiles, or with a {@link List} with
   * only a few alterations.  The tiles will not change between calls to this
   * method.
   * 
   * @param tiles 
   */
  public void compile(List<TileInfo> tiles);
  
  /**
   * Renders the tiles compiled with this {@link Object}.
   * 
   * @param pass The number of the current rendering pass.
   * @param proj The current projection {@link Matrix4}.
   * @param modelview The current composite of the model and view matrices.
   */
  public void render(int pass, ReadableMatrix4 proj, ReadableMatrix4 modelview);
  
  /**
   * Releases any resources being used by this {@link TileRenderer}.
   */
  public void release();
  
  /**
   * Returns the class that this renderer uses to store information about how to
   * render various tile types.  If this method returns null then no additional
   * information will be associated with the tiles that use this {@link TileRenderer}
   * in the {@link TileRenderingRegistry}.
   * 
   * @return The class that will be used to store additional rendering information
   * for any tiles that use this {@link TileRenderer}.
   */
  public Class<? extends RenderingInfo> getInfoClass();
}
