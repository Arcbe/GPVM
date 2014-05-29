/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.render;

import taiga.code.io.DataNode;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.registry.RenderingInfo;

/**
 *
 * @author russell
 */
public class ColorInfo implements RenderingInfo {
  public static final String COLOR_FIELD = "color";
  
  public int color;

  public ColorInfo(DataNode data) {
    data = (DataNode) data.getObject(COLOR_FIELD);
    color = ((Number)data.data).intValue();
  }
}
