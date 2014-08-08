/*
 * Copyright (C) 2014 Russell Smith
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package taiga.code.input;

/**
 * A listener for button presses.  This allows both keyboard buttons and mouse
 * buttons to use the same interface for key bindings.
 * 
 * @author Russell Smith
 */
public interface ButtonListener {
  /**
   * Called when a button is pressed.
   * 
   * @param name The name of the pressed button.
   */
  public void buttonPressed(String name);
  
  /**
   * Called when a button is released from a pressed state.
   * 
   * @param name The name of the released button.
   */
  public void buttonReleased(String name);
}
