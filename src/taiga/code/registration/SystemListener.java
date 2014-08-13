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

package taiga.code.registration;

/**
 * A listener for {@link NamedSystem} events.  This listener will be notified
 * of the starting and stopping of the {@link NamedSystem} it is listening to.
 * 
 * @author russell
 */
public interface SystemListener {
  /**
   * Called when the {@link NamedSystem} starts.
   * 
   * @param sys The {@link NamedSystem} that started.
   */
  public void systemStarted(NamedSystem sys);
  
  /**
   * Called when the {@link NamedSystem} stops.
   * 
   * @param sys The {@link NamedSystem} that stopped.
   */
  public void systemStopped(NamedSystem sys);
}
