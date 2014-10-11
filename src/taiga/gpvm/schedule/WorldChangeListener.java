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

package taiga.gpvm.schedule;

/**
 * A listener for changes to a {@link World} caused by a {@link WorldChange}.
 * 
 * @author russell
 */
public interface WorldChangeListener {
  /**
   * Called when the given {@link WorldChange} is applied.
   * 
   * @param change The {@link WorldChange} that was applied.
   * @param prev The previous value for the field changed by the {@link WorldChange}.
   */
  public void worldChanged(WorldChange change, Object prev);
}
