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

package taiga.gpvm.render;

import java.util.Collection;
import taiga.code.math.ReadableMatrix4;
import taiga.code.util.DataNode;
import taiga.gpvm.entity.Entity;

/**
 * Handles the rendering of a set of {@link Entity}.  A single {@link EntityRenderer}
 * will be used to render multiple {@link Entity}s of the same type.  Also this
 * class should either have a constructor that accepts a {@link DataNode} or
 * one that has no parameters in order to be constructed properly.
 * 
 * @author russell
 */
public interface EntityRenderer {
  /**
   * Renders the given {@link Entity}.
   * 
   * @param ent The {@link Entity} to render.
   * @param pass The current rendering pass.
   */
  public void render(Collection<Entity> ent, int pass, ReadableMatrix4 proj, ReadableMatrix4 modelview);
  
  /**
   * Loads data specific to the type of {@link Entity} that will be rendered
   * with this {@link EntityRenderer}.
   * 
   * @param data The {@link DataNode} containing the data.
   */
  public void loadData(DataNode data);
}
