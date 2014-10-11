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

package taiga.code.registration;

/**
 * A listener for children of {@link NamedObject}s. This can be added to a
 * {@link NamedObject} to be notified when a child is added or removed, or
 * alternatively to be notified when it is added or removed from a parent.
 *
 * @author russell
 */
public interface ChildListener {

  /**
   * Called when a child is added to the {@link NamedObject}.
   *
   * @param parent The parent that the child is added to.
   * @param child The child that was added.
   */
  public void childAdded(NamedObject parent, NamedObject child);

  /**
   * Called when a child is removed from a {@link NamedObject}.
   *
   * @param parent The {@link NamedObject} that had a child removed.
   * @param child The {@link NamedObject} that was removed.
   */
  public void childRemoved(NamedObject parent, NamedObject child);
}
