/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm.render;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;
import taiga.code.math.Matrix4;
import taiga.code.math.Vector3;
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
    viewables = new HashSet<>();
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
    entreg = getObject(HardcodedValues.ENTITY_RENDERING_REGISTRY_NAME);
    entmng = getObject(HardcodedValues.ENTITY_MANAGER_NAME);
  }

  @Override
  protected void updateRenderable() {
    super.updateRenderable();
    
    updateRendFrustrum();
    collectEntities();
    
    for(RegionRenderer reg : renderers.values())
      reg.update();
  }

  @Override
  protected void renderSelf(int pass, Matrix4 proj) {
    
    for(Map.Entry<EntityRenderer, Collection<Entity>> val : entrend.entrySet()) {
      val.getKey().render(val.getValue(), pass);
    }
    
    Matrix4 global = new Matrix4(getGlobalTransform());
    Vector3 offset = new Vector3(
      global.getValue(0, 3),
      global.getValue(1, 3),
      global.getValue(2, 3));
    for(RegionRenderer reg : renderers.values()) {
      Coordinate coor = reg.getLocation();
      Vector4 loc = new Vector4(coor.x, coor.y, coor.z, 0f);
      
      global.transform(loc, loc);
      global.setValue(0, 3, loc.getX() + offset.getX());
      global.setValue(1, 3, loc.getY() + offset.getY());
      global.setValue(2, 3, loc.getZ() + offset.getZ());
      
      reg.render(pass, proj.asReadOnly(), global.asReadOnly());
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
    reg.updateTile(change.location);
  }
  
  private void updateRendFrustrum() {
    viewables.clear();
    
    for(Coordinate coor : renderers.keySet())
      viewables.add(coor);
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
      
      Collection<Entity> list = entrend.get(rend);
      if(list == null) {
        list = new HashSet<>();
        entrend.put(rend, list);
      }
      
      list.add(ent);
    }
  }
  
  private static final String locprefix = WorldRenderer.class.getName().toLowerCase();
  
  private static final String NO_SKY_REGISTRY = locprefix + ".no_sky_registry";
  private static final String NO_SKY_FOUND = locprefix + ".no_sky_found";
  private static final String SKY_ADDED = locprefix + ".sky_added";
  private static final String SKY_CREATION_EX = locprefix + ".sky_creation_ex";
  
  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
