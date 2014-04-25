/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.render.renderers;

import taiga.gpvm.render.RenderInfo;
import gpvm.util.StringManager;
import java.awt.Color;

/**
 * A simple instance of {@link RenderInfo} that contains only a color
 * value.
 * @author russell
 */
public class ColorInfo implements RenderInfo {
  /**
   * An integer representing the color for this {@link RenderInfo}.  The
   * integer is in the ARGB format.
   */
  public Long color;
  
  public ColorInfo() {
    color = (long) Color.magenta.getRGB();
  }
  
  public ColorInfo(Color color) {
    this(color.getRGB());
  }
  
  public ColorInfo(long color) {
    this.color = color;
  }
  
  @Override
  public String toString() {
    return StringManager.getLocalString("tostring_color_info", String.format("%8X", color));
  }
}
