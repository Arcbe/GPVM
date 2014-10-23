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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;
import taiga.code.math.Matrix4;
import taiga.code.math.ReadableMatrix4;
import taiga.code.math.Vector4;
import taiga.code.opengl.SceneRoot;
import taiga.code.registration.NamedObject;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.entity.Entity;
import taiga.gpvm.entity.EntityManager;
import taiga.gpvm.util.geom.Coordinate;
import taiga.gpvm.map.Region;
import taiga.gpvm.map.World;
import taiga.gpvm.map.WorldListener;
import taiga.gpvm.registry.EntityRenderingRegistry;
import taiga.gpvm.schedule.WorldChange;
import taiga.gpvm.schedule.WorldChangeListener;

/**
 * Handles the rendering of a {@link World}.
 * 
 * @author russell
 */
public final class WorldRenderer extends SceneRoot implements WorldListener, WorldChangeListener {
  /**
   * Creates a new {@link WorldRenderer} that will render the given {@link World}.
   * 
   * @param world The {@link World} to render.
   */
  public WorldRenderer(World world) {
    super(world.name);
    
    map = world;
    renderers = new HashMap<>();
    viewables = new ArrayList<>();
    entrend = new HashMap<>();
    
    world.addListener(this);
    for(Region reg : world.getRegions())
      regionLoaded(reg);
  }
  
  private static final int drawdistance = 4;
  
  private final World map;
  private final Collection<Coordinate> viewables;
  private final Map<EntityRenderer, Collection<Entity>> entrend;
  private final Map<Coordinate, RegionRenderer> renderers;
  
  private EntityRenderingRegistry entreg;
  private EntityManager entmng;
  
  @Override
  protected void attached(NamedObject parent) {
    entreg = getObject(HardcodedValues.NAME_ENTITY_RENDERING_REGISTRY);
    entmng = getObject(HardcodedValues.NAME_ENTITY_MANAGER);
  }

  @Override
  protected void updateRenderable() {
    super.updateRenderable();
    
    for(RegionRenderer reg : renderers.values())
      reg.update();
  }

  @Override
  protected void renderSelf(int pass, Matrix4 proj) {
    
    updateRendFrustrum(proj);
    
    for(Map.Entry<EntityRenderer, Collection<Entity>> val : entrend.entrySet()) {
      val.getKey().render(val.getValue(), pass, proj, getGlobalTransform());
    }
    
    Matrix4 global = new Matrix4(getGlobalTransform());
    Matrix4 trans = new Matrix4();
    for(RegionRenderer reg : renderers.values()) {
      Coordinate coor = reg.getLocation();
      
      trans.setIdentity();
      
      trans.setValue(0, 3, coor.x);
      trans.setValue(1, 3, coor.y);
      trans.setValue(2, 3, coor.z);
      
      trans.mulRHS(global, trans);
      
      reg.render(pass, proj.asReadOnly(), trans.asReadOnly());
    }
    
    for(Map.Entry<EntityRenderer,Collection<Entity>> entry : entrend.entrySet()) {
      EntityRenderer rend = entry.getKey();
      Collection<Entity> list = entry.getValue();
      
      if(list != null) rend.render(list, pass, proj, getGlobalTransform());
    }
  }

  @Override
  public void regionLoaded(Region reg) {
    RegionRenderer region = new RegionRenderer(reg);
    renderers.put(region.getLocation(), region);
    addChild(region);
  }

  @Override
  public void regionUnloaded(Region reg) {
  }

  @Override
  public void worldChanged(WorldChange change, Object prev) {
    if(change.world != map) return;
    
    RegionRenderer reg = renderers.get(change.location.getRegionCoordinate());
    if(reg == null) return;
    
    reg.updateTile(change.location);
  }
  
  private void updateRendFrustrum(ReadableMatrix4 proj) {
    Matrix4 inv = new Matrix4(proj);
    
    //create teh rendering matrix
    inv.mul(getGlobalTransform());
    Matrix4 trans = new Matrix4(inv);
    
    //now create the invert the matrix to transform the coordinates of the corner vertices
    inv.invert();
    Vector4[] corners = new Vector4[] {
      new Vector4(-1, -1, 0, 1),
      new Vector4(1, -1, 0, 1),
      new Vector4(1, 1, 0, 1),
      new Vector4(-1, 1, 0, 1),
      new Vector4(-1, 1, 1, 1),
      new Vector4(-1, -1, 1, 1),
      new Vector4(1, -1, 1, 1),
      new Vector4(1, 1, 1, 1)
    };
    
    float xmin = Float.POSITIVE_INFINITY;
    float xmax = Float.NEGATIVE_INFINITY;
    float ymin = Float.POSITIVE_INFINITY;
    float ymax = Float.NEGATIVE_INFINITY;
    float zmin = Float.POSITIVE_INFINITY;
    float zmax = Float.NEGATIVE_INFINITY;
    
    for(int i = 0; i < corners.length; i++) {
      //find the corners in world space
      inv.transform(corners[i], corners[i]);
      corners[i].scale(1f / corners[i].getW());
      
      //and create a bounding box for them.
      if(corners[i].getX() < xmin) xmin = corners[i].getX();
      if(corners[i].getX() > xmax) xmax = corners[i].getX();
      if(corners[i].getX() < ymin) ymin = corners[i].getY();
      if(corners[i].getX() > ymax) ymax = corners[i].getY();
      if(corners[i].getX() < zmin) zmin = corners[i].getZ();
      if(corners[i].getX() > zmax) zmax = corners[i].getZ();
    }
    
    int xm = (int)xmin / Region.REGION_SIZE;
    int ym = (int)ymin / Region.REGION_SIZE;
    int zm = (int)zmin / Region.REGION_SIZE;
    
    int xmm = (int)xmax / Region.REGION_SIZE;
    int ymm = (int)ymax / Region.REGION_SIZE;
    int zmm = (int)zmax / Region.REGION_SIZE;
    
    //check all of the regions intersecting the bounding box, and add them to the
    //rendering queue as needed.
    //TODO: actually do check intersections
    viewables.clear();
    for(int i = xm; i < xmm; i++) {
      for(int j = ym; j < ymm; j++) {
        for(int k = zm; k < zmm; k++) {
          viewables.add(new Coordinate(
            i * Region.REGION_SIZE,
            j * Region.REGION_SIZE,
            k * Region.REGION_SIZE));
        }
      }
    }
    
    collectEntities();
  }
  
  private void collectEntities() {
    HashSet<Entity> ents = new HashSet<>();
    for (Collection<Entity> cols : entrend.values()) {
      cols.clear();
    }
    
    for(Coordinate coor : viewables) {
      Collection<Entity> temp = entmng.getEntitiesAtRegion(coor);
      if(temp != null) ents.addAll(temp);
    }
    
    for(Entity ent : ents) {
      EntityRenderer rend = entreg.getEntry(ent.type).renderer;
      Collection<Entity> temp = entrend.get(rend);
      
      if(temp == null) {
        temp = new HashSet<>();
        entrend.put(rend, temp);
      }
      
      temp.add(ent);
    }
  }
  
  private static final String locprefix = WorldRenderer.class.getName().toLowerCase();
  
  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
