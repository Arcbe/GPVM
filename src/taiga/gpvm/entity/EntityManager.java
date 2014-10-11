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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import taiga.code.geom.AABox;
import taiga.code.registration.NamedObject;
import taiga.code.util.Updateable;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.map.Region;
import taiga.gpvm.registry.EntityType;
import taiga.gpvm.util.geom.Coordinate;

public class EntityManager extends NamedObject implements Updateable {

  public EntityManager() {
    super(HardcodedValues.ENTITY_MANAGER_NAME);
    
    index = new TreeMap<>();
    entlocs = new HashMap<>();
    
    nextid = 0;
  }
  
  public Entity createEntity(EntityType type, Coordinate location) {
    Entity nent = new Entity(getNextID(), type, location);
    
    index.put(nent.id, nent);
    addEntityLoc(nent);
    
    return nent;
  }
  
  public Collection<Entity> getEntitiesAtRegion(Coordinate coor) {
    return null;
//    coor = coor.getRegionCoordinate();
//    
//    Collection<Entity> result = entlocs.get(coor);
//    if(result == null) return null;
//    else return Collections.unmodifiableCollection(result);
  }

  @Override
  public void update() {
    
    for(Entity ent : index.values()) {
      ent.update();
    }
  }
  
  private final Map<Long, Entity> index;
  private final Map<Coordinate, Collection<Entity>> entlocs;
  private int nextid;
  
  private int getNextID() {
    return nextid++;
  }
  
  protected void updateLocation(Entity ent, AABox prevbounds) {
    if(prevbounds != null) {
      //TODO: implement this
    }
    
    addEntityLoc(ent);
  }
  
  private void removeEntityLoc(Entity ent, AABox bounds) {
    int top = (int) bounds.top() / Region.REGION_SIZE;
    int right = (int) bounds.right() / Region.REGION_SIZE;
    int back = (int) bounds.back() / Region.REGION_SIZE;
    
    Coordinate loc = new Coordinate();
    for(int i = (int) bounds.left() / Region.REGION_SIZE; i <= top; i++) {
      loc.x = i;
      for(int j = (int) bounds.front() / Region.REGION_SIZE; i <= top; i++) {
        loc.y = j;
        for(int k = (int) bounds.bottom() / Region.REGION_SIZE; i <= top; i++) {
          loc.z = k;
          
          Collection<Entity> ents = entlocs.get(loc);
          if(ents == null) {
            ents = new HashSet<>();
            entlocs.put(loc, ents);
          }
          
          ents.remove(ent);
        }
      }
    } 
  }
    
  private void addEntityLoc(Entity ent) {
    AABox bounds = ent.getBounds();
    
    int top = (int) bounds.top() / Region.REGION_SIZE;
    int right = (int) bounds.right() / Region.REGION_SIZE;
    int back = (int) bounds.back() / Region.REGION_SIZE;
    
    Coordinate loc = new Coordinate();
    for(int i = (int) bounds.left() / Region.REGION_SIZE; i <= top; i++) {
      loc.x = i;
      for(int j = (int) bounds.front() / Region.REGION_SIZE; i <= top; i++) {
        loc.y = j;
        for(int k = (int) bounds.bottom() / Region.REGION_SIZE; i <= top; i++) {
          loc.z = k;
          
          Collection<Entity> ents = entlocs.get(loc);
          if(ents == null) {
            ents = new HashSet<>();
            entlocs.put(loc, ents);
          }
          
          ents.add(ent);
        }
      }
    }     
  }

  private static final String locprefix = EntityManager.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
