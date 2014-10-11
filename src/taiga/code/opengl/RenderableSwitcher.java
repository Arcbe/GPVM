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

package taiga.code.opengl;

import taiga.code.math.Matrix4;

/**
 * Used to switch between {@link Renderable} so that at most one child {@link Renderable}
 * is visible at a time.  For example the {@link Renderable}s could be various
 * screens in an application, and this class will allow for switching between them.
 * The displayed {@link Renderable} can be changed either through this class or
 * through the {@link Renderable#setVisible(boolean)} method.
 * 
 * @author russell
 */
public class RenderableSwitcher extends Renderable {

  /**
   * Creates a new {@link RenderableSwitcher} with the given name.
   * 
   * @param name The name for the {@link RenderableSwitcher}
   */
  public RenderableSwitcher(String name) {
    super(name);
  }

  @Override
  protected void updateRenderable() {}

  @Override
  protected void renderSelf(int pass, Matrix4 proj) {}
  
}
