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

import taiga.code.util.DataNode;
import taiga.gpvm.registry.RenderingInfo;

/**
 * Provides information for the {@link ColorRenderer}.
 * 
 * @author russell
 */
public class ColorInfo implements RenderingInfo {
  /**
   * Field name for the color attribute in rendering information data files.
   */
  public static final String COLOR_FIELD = "color";
  
  /**
   * The color to render {@link Tile}s with.  This is in the ARGB format.
   */
  public int color;

  /**
   * Creates a new {@link ColorInfo} using the data in a {@link DataNode}.
   * 
   * @param data The {@link DataNode} to create this {@link ColorInfo} from.
   */
  public ColorInfo(DataNode data) {
    data = (DataNode) data.getObject(COLOR_FIELD);
    color = ((Number)data.data).intValue();
  }
}
