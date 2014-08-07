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

package taiga.code.opengl;

import taiga.code.math.Matrix4;

/**
 * Interface for objects that can supply matrices for 3D graphics.
 * 
 * @author Russell Smith
 */
public interface Camera {
  /**
   * Returns the projection {@link Matrix4} for this {@link Camera}.
   * Typically this will be either a perspective projection or orthogonal projection
   * {@link Matrix4}.
   * 
   * @return The projection {@link Matrix4}.
   */
  public Matrix4 getProjection();
  
  /**
   * Returns the view {@link Matrix4} for this {@link Camera}.  This will
   * 
   * @return 
   */
  public Matrix4 getViewMatrix();
}
