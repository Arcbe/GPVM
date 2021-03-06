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

package taiga.gpvm.entity;

import java.util.logging.Logger;
import taiga.code.geom.AABox;
import taiga.code.math.ReadableVector3;
import taiga.code.math.Vector3;
import taiga.code.util.DataNode;
import taiga.code.util.Updateable;
import taiga.gpvm.map.World;
import taiga.gpvm.registry.EntityType;
import taiga.gpvm.util.geom.Coordinate;

/**
 * Base class for all {@link Entity}s in the game.  This class provides a
 * simple interface for collision detection and rendering.  Each {@link Entity}
 * will be given an id and each subclass should forward the id given to them to
 * the constructor for this class.  Each entity will be expected to have a
 * constructor that has a long id, and {@link DataNode} as parameters.  The
 * {@link DataNode} may be null if there is no additional information for the
 * given {@link Entity} being created.
 * 
 * @author russell
 */
public class Entity implements Updateable {
  public final long id;
  public final EntityType type;

  protected Entity(long id, EntityType type, Coordinate location) {
    this.id = id;
    this.type = type;
    
    bounds = new AABox();
    position = new Vector3(location.x, location.y, location.z);
    momentum = new Vector3();
    world = location.world;
    damage = 0;
  }
  
  public AABox getBounds() {
    return bounds;
  }
  
  public ReadableVector3 getPosition() {
    return position;
  }

  @Override
  public void update() {
  }
  
  public void teleport(Coordinate coor) {
    position.setX(coor.x);
    position.setY(coor.y);
    position.setZ(coor.z);
  }
  
  private final AABox bounds;
  private final long damage;
  private final Vector3 position;
  private final Vector3 momentum;
  private World world;

  private static final String locprefix = Entity.class.getName().toLowerCase();
  
  private static final String MISSING_RENDERING_REGISTRY = locprefix + ".missing_rendering_registry";

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
