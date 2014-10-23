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

package taiga.gpvm.util.geom;

import java.util.Iterator;
import java.util.logging.Logger;
import taiga.code.math.ReadableVector3;
import taiga.code.math.Vector3;

/**
 * Provides a method for iterating over the {@link Tile}s intersecting a 3d line
 * segment.
 * 
 * @author Russell Smith
 */
public class LineIterator implements Iterable<Coordinate>, Iterator<Coordinate> {
  
  private Vector3 start;
  private Vector3 end;
  private Vector3 direction;

  public LineIterator(ReadableVector3 s, ReadableVector3 e) {
    start = new Vector3(s);
    end = new Vector3(e);
    direction = start.getClone();
    direction.scale(-1);
    direction.add(end);
  }

  @Override
  public Iterator<Coordinate> iterator() {
    return this;
  }

  @Override
  public boolean hasNext() {
    return 
      (int) start.getX() != (int) end.getX() ||
      (int) start.getY() != (int) end.getY() ||
      (int) start.getZ() != (int) end.getZ();
  }
  
  @Override
  public Coordinate next() {
    Coordinate result = new Coordinate();
    result.x = (int) Math.floor(start.getX());
    result.y = (int) Math.floor(start.getY());
    result.z = (int) Math.floor(start.getZ());
    
    //find the next intersection in on each of the axes.
    float tx = (float) (start.getX() < end.getX() ? Math.ceil(start.getX()) : Math.floor(start.getX()));
    float ty = (float) (start.getY() < end.getY() ? Math.ceil(start.getY()) : Math.floor(start.getY()));
    float tz = (float) (start.getZ() < end.getZ() ? Math.ceil(start.getZ()) : Math.floor(start.getZ()));
    
    //now find which is the closest.
    float dist = (tx - start.getX()) / direction.getX();
    float temp = (ty - start.getY()) / direction.getY();
    
    if(temp < dist) dist = temp;
    temp = (tz - start.getZ()) / direction.getZ();
    
    if(temp < dist) dist = temp;
    
    //move along the line to the next coordinate.
    direction.scale(dist * 1.01f);
    
    start.add(direction);
    
    return result;
  }

  private static final String locprefix = LineIterator.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
