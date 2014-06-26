/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import taiga.code.graphics.Camera;
import taiga.code.graphics.Renderable;
import taiga.code.registration.RegisteredObject;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.util.geom.Coordinate;
import taiga.gpvm.map.Region;
import taiga.gpvm.map.World;
import taiga.gpvm.map.WorldListener;
import taiga.gpvm.registry.SkyEntry;
import taiga.gpvm.registry.SkyRegistry;
import taiga.gpvm.schedule.WorldChange;
import taiga.gpvm.schedule.WorldChangeListener;

/**
 * Handles the rendering of a {@link World}.
 * 
 * @author russell
 */
public final class WorldRenderer extends Renderable implements WorldListener, WorldChangeListener {
  /**
   * Creates a new {@link WorldRenderer} that will render the given {@link World}.
   * 
   * @param world The {@link World} to render.
   */
  public WorldRenderer(World world) {
    super(world.name);
    
    map = world;
    renderers = new HashMap<>();
    
    world.addListener(this);
    for(Region reg : world.getRegions())
      regionLoaded(reg);
  }
  
  public void setCamera(Camera cam) {
    camera = cam;
  }
  
  private static int drawdistance = 4;
  
  private World map;
  private Map<Coordinate, RegionRenderer> renderers;
  private volatile Camera camera;
  
  @Override
  protected void attached(RegisteredObject parent) {
    SkyRegistry skies = getObject(HardcodedValues.SKY_REGISTRY_NAME);
    
    if(skies == null) {
      log.log(Level.WARNING, NO_SKY_REGISTRY);
      return;
    }
    
    SkyEntry entry = skies.getEntry(name);
    if(entry == null) {
      log.log(Level.WARNING, NO_SKY_FOUND, name);
      return;
    }
    
    try {
      SkyBoxRenderer rend = entry.getRenderer();
      addChild(rend);
      
      log.log(Level.FINE, SKY_ADDED, new Object[] {rend, name});
    } catch(ReflectiveOperationException ex) {
      log.log(Level.WARNING, SKY_CREATION_EX, ex);
    }
  }

  @Override
  protected void updateSelf() {
    for(RegionRenderer reg : renderers.values())
      reg.update();
  }

  @Override
  protected void renderSelf(int pass) {
    if(camera == null) return;
    
    camera.setupProjectioMatrix();
    
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glPushMatrix();
    
    for(RegionRenderer reg : renderers.values()) {
      Coordinate coor = reg.getLocation();
      GL11.glLoadIdentity();
      GL11.glTranslatef(coor.x, coor.y, coor.z);
      
      reg.render(pass);
    }
    
    GL11.glPopMatrix();
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
  
  private static final String locprefix = WorldRenderer.class.getName().toLowerCase();
  
  private static final String NO_SKY_REGISTRY = locprefix + ".no_sky_registry";
  private static final String NO_SKY_FOUND = locprefix + ".no_sky_found";
  private static final String SKY_ADDED = locprefix + ".sky_added";
  private static final String SKY_CREATION_EX = locprefix + ".sky_creation_ex";
  
  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
