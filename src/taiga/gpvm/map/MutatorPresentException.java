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

package taiga.gpvm.map;

/**
 * A {@link RuntimeException} thrown when a {@link WorldMutator} attempts to
 * target a {@link World} that already has a {@link WorldMutator} targeting it.
 * 
 * @author russell
 */
public class MutatorPresentException extends RuntimeException {

  public MutatorPresentException() {
  }

  public MutatorPresentException(String message) {
    super(message);
  }

  public MutatorPresentException(String message, Throwable cause) {
    super(message, cause);
  }

  public MutatorPresentException(Throwable cause) {
    super(cause);
  }

  public MutatorPresentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
