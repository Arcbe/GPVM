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

package taiga.gpvm.registry;

import taiga.code.util.DataNode;

/**
 * Indicates that a class can be used to store rendering data for a specific
 * {@link Renderer} or {@link EntityRenderer}.  The implementing class must 
 * have a constructor that takes in a {@link DataNode} as an argument.  This 
 * {@link DataNode} will contain the value of the rendering-info field for a 
 * {@link RenderingEntry}, or null if no data is provided for the a given {@link RenderingEntry}
 * 
 * @author russell
 */
public interface RenderingInfo {
}
