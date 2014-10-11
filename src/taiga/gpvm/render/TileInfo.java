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

import taiga.gpvm.map.Tile;
import taiga.gpvm.util.geom.Coordinate;
import taiga.gpvm.util.geom.Direction;
import taiga.gpvm.registry.TileRenderingEntry;

/**
 * A descriptor of basic information about a {@link Tile} for use in rendering.
 * 
 * @author russell
 */
public final class TileInfo {
  /**
   * The position in the world of the {@link Tile} associated with this {@link TileInfo}.
   * When being rendered however, the tile should be positioned relative to the
   * origin of the chunk it is contained in.
   */
  public Coordinate absposition;
  
  /**
   * The {@link Tile} associated with this {@link TileInfo}
   */
  public Tile tile;
  
  /**
   * Adjacent {@link Tile}s to the associated {@link Tile}.  Some or all may be
   * null indicating that the adjacent {@link Tile} is not currently known.  The
   * order of {@link Tile}s in the array is the same as the value for elements
   * of the {@link Direction} enumeration.
   */
  public Tile[] adjacent;
  
  /**
   * The {@link TileRenderingEntry} for the {@link Tile} described by this {@link TileInfo}.
   */
  public TileRenderingEntry rendentry;
}
