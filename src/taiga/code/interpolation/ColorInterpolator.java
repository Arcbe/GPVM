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

package taiga.code.interpolation;

import java.util.logging.Logger;
import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;

/**
 * Provides interpolation functions for {@link Color}s.
 * 
 * @author russell
 */
public class ColorInterpolator {
  
  /**
   * Interpolates between the given {@link Color}s according to the given
   * amount.  The amount should be between 0 and 1, all other numbers are produce
   * undefined results.
   * 
   * @param col1 The first {@link Color} in the interpolation.
   * @param col2 The second {@link Color} in the interpolation.
   * @param amt The amount to interpolate.
   * @return The interpolated {@link Color}.
   */
  public static Color interpolate(ReadableColor col1, ReadableColor col2, float amt) {
    return interpolate(col1, col2, amt, new Color());
  }
  
  /**
   * Interpolates between the given {@link Color}s according to the given
   * amount.  The amount should be between 0 and 1, all other numbers are produce
   * undefined results.
   * 
   * @param col1 The first {@link Color} in the interpolation.
   * @param col2 The second {@link Color} in the interpolation.
   * @param amt The amount to interpolate.
   * @param out The {@link Color} to store the output in.
   * @return The interpolated {@link Color}.
   */
  public static Color interpolate(ReadableColor col1, ReadableColor col2, float amt, Color out) {
    int num = (int) (col1.getAlpha() * (1 - amt) + col2.getAlpha() * amt);
    out.setAlpha(num);
    
    num = (int) (col1.getRed()* (1 - amt) + col2.getRed() * amt);
    out.setRed(num);
    
    num = (int) (col1.getGreen()* (1 - amt) + col2.getGreen() * amt);
    out.setGreen(num);
    
    num = (int) (col1.getBlue()* (1 - amt) + col2.getBlue() * amt);
    out.setBlue(num);
    
    return out;
  }

  private static final String locprefix = ColorInterpolator.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
