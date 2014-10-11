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
 * An {@link Exception} thrown when a {@link NamedObject} cannot be found in
 * a naming tree.
 * 
 * @author russell
 */
public class MissingObjectException extends RuntimeException {
  /**
   * Creates a new {@link MissingObjectException} without a message.
   */
  public MissingObjectException() {
    super();
  }

  /**
   * Creates a new {@link MissingObjectException} with the given message.
   * 
   * @param message The message for this {@link MissingObjectException}
   */
  public MissingObjectException(String message) {
    super(message);
  }

  /**
   * Creates a new {@link MissingObjectException} with a given message and cause.
   * 
   * @param message The message for this {@link MissingObjectException}
   * @param cause The cause for this {@link MissingObjectException}.
   */
  public MissingObjectException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new {@link MissingObjectException} with a default message and cause.
   * 
   * @param cause The cause for this {@link MissingObjectException}.
   */
  public MissingObjectException(Throwable cause) {
    super(cause);
  }
}
