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

package taiga.code.opengl;

import taiga.code.util.Resource;

/**
 * An interface for objects that can be drawn to the window.  This is for
 * 2D drawing.
 * 
 * @author russell
 */
public interface Drawable extends Resource {
  
  /**
   * Returns the width of this {@link Drawable}.  The exact interpretation
   * depends on the implementing class, but this should be a width guaranteed
   * not to cause stretching.
   * @return 
   */
  public int getWidth();
  
  /**
   * Returns the height of this {@link Drawable}.  The exact interpretation
   * depends on the implementing class, but this should be a height guaranteed
   * not to cause stretching.
   * 
   * @return The height of this {@link Drawable}.
   */
  public int getHeight();
  
  /**
   * Draws the {@link Drawable} in the given area.  The {@link Drawable}
   * should fill the entire area.
   * 
   * @param x The x coordinate of the left side of the drawing area.
   * @param y The y coordinate of the bottom of the drawing area.
   * @param w The width of the drawing area.
   * @param h The height of the drawing area.
   */
  public void draw(int x, int y, int w, int h);
}
