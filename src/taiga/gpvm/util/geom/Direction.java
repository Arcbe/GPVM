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

package taiga.gpvm.util.geom;

/**
 * Indicates a direction in the map.  Positive and negative x are west and 
 * east, positive and negative y are north and south, and finally positive and
 * negative z are up and down respectively.
 * 
 * @author russell
 */
public enum Direction {
  /**
   * negative x direction
   */
  East(0),
  /**
   * positive x direction
   */
  West(1),
  /**
   * positive y direction
   */
  North(2),
  /**
   * negative y direction
   */
  South(3),
  /**
   * positive z direction
   */
  Up(4),
  /**
   * negative z direction
   */
  Down(5);
  
  private int value;

  private Direction(int value) {
    this.value = value;
  }
  
  public int getIndex() {
    return value;
  }
}
