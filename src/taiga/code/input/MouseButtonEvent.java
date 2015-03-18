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

package taiga.code.input;

import java.text.MessageFormat;

/**
 * Contains the details of an event caused by a mouse button.
 * 
 * @author russell
 */
public class MouseButtonEvent {
  /**
   * The movement of the mouse wheel for this {@link MouseButtonEvent}.
   */
  public final int dwheel;
  
  /**
   * The button that caused this {@link MouseButtonEvent}.
   */
  public final int button;
  
  /**
   * Whether the mouse button is pressed or not.
   */
  public final boolean state;
  
  /**
   * The x coordinate of the event location.  The origin is located in the
   * bottom left corner of the window.
   */
  public final int x;
  
  /**
   * The y coordinate of the event location.  The origin is located in the bottom
   * left corner of the window.
   */
  public final int y;
  
  /**
   * The delta x of the event.
   */
  public final int dx;
  
  /**
   * The delta y of the event.
   */
  public final int dy;
  
  /**
   * The time of the event in nanoseconds.  The absolute time for this value
   * is not defined and only the duration between events has a defined meaning.
   */
  public final long time;

  /**
   * Creates a new {@link MouseButtonEvent} with the given values.
   * 
   * @param dwheel Movement of the mouse wheel.
   * @param button The button that caused the event.
   * @param state Whether the button was pressed.
   * @param x The x coordinate of the event location.
   * @param y The y coordinate of the event location.
   * @param dx The movement in the x direction for the event.
   * @param dy The movement in the y direction for the event.
   * @param time The time the event occurred.
   */
  public MouseButtonEvent(int dwheel, int button, boolean state, int x, int y, int dx, int dy, long time) {
    this.dwheel = dwheel;
    this.button = button;
    this.state = state;
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
    this.time = time;
  }
  
  @Override
  public String toString() {
    
    return MessageFormat.format("[button={0}, state={1}, (X,Y)=({2},{3}), (dX,dY)=({4},{5}), dZ={6}]", 
      button,
      state,
      x,
      y,
      dx,
      dy,
      dwheel);
  }
}
