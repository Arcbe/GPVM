/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.render;

import taiga.code.io.DataNode;
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
