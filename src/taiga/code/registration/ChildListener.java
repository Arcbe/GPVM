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
 * A listener for children of {@link RegisteredObject}s. This can be added to a
 * {@link RegisteredObject} to be notified when a child is added or removed, or
 * alternatively to be notified when it is added or removed from a parent.
 *
 * @author russell
 */
public interface ChildListener {

  /**
   * Called when a child is added to the {@link RegisteredObject}.
   *
   * @param parent The parent that the child is added to.
   * @param child The child that was added.
   */
  public void childAdded(RegisteredObject parent, RegisteredObject child);

  /**
   * Called when a child is removed from a {@link RegisteredObject}.
   *
   * @param parent The {@link RegisteredObject} that had a child removed.
   * @param child The {@link RegisteredObject} that was removed.
   */
  public void childRemoved(RegisteredObject parent, RegisteredObject child);
}
