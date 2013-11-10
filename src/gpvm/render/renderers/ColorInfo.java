/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.render.renderers;

import gpvm.render.RenderInfo;
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
  public int color;
  
  public ColorInfo() {
    color = Color.magenta.getRGB();
  }
  
  public ColorInfo(Color color) {
    this(color.getRGB());
  }
  
  public ColorInfo(int color) {
    this.color = color;
  }
}
