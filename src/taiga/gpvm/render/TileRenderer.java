/*
 * Copyright (C) 2014 Russell Smith.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package taiga.gpvm.render;

import java.util.List;
import taiga.code.math.Matrix4;
import taiga.code.math.ReadableMatrix4;

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
   * //TODO: update this.
   * Returns the class that this renderer uses to store information about how to
   * render various tile types.  If this method returns null then no additional
   * information will be associated with the tiles that use this {@link TileRenderer}
   * in the {@link TileRenderingRegistry}.
   * 
   * @return The class that will be used to store additional rendering information
   * for any tiles that use this {@link TileRenderer}.
   */
}
