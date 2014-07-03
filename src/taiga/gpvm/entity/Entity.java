/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.entity;

import java.util.logging.Logger;
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector3f;
import taiga.code.geom.AABox;
import taiga.code.util.DataNode;
import taiga.code.registration.MissingObjectException;
import taiga.code.text.TextLocalizer;
import taiga.code.util.Updateable;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.registry.EntityEntry;
import taiga.gpvm.registry.EntityRenderingEntry;
import taiga.gpvm.registry.EntityRenderingRegistry;
import taiga.gpvm.render.ColorEntityRenderer;
import taiga.gpvm.render.EntityRenderer;

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
  public final EntityEntry type;
  public final EntityManager manager;

  protected Entity(long id, EntityEntry type, EntityManager manager) {
    this.id = id;
    this.type = type;
    this.manager = manager;
    
    bounds = new AABox();
    velocity = new Vector3f();
    damage = 0;
  }
  
  public void render(int pass){
    checkRenderer();
    
    renderer.render(this, pass);
  }
  
  public AABox getBounds() {
    return bounds;
  }

  @Override
  public void update() {
  }
  
  private final AABox bounds;
  private final Vector3f velocity;
  private final long damage;
  
  private EntityRenderer renderer;
  
  private void checkRenderer() {
    if(renderer != null) return;
    
    EntityRenderingRegistry rend = manager.getObject(HardcodedValues.ENTITY_RENDERING_REGISTRY_NAME);
    if(rend == null) {
      throw new MissingObjectException(TextLocalizer.localize(MISSING_RENDERING_REGISTRY));
    }
    
    EntityRenderingEntry entry = rend.getEntry(type);
    if(entry == null || entry.renderer == null) {
      //lets use the default renderer
      renderer = new ColorEntityRenderer();
    } else {
      renderer = entry.renderer;
    }
  }

  private static final String locprefix = Entity.class.getName().toLowerCase();
  
  private static final String MISSING_RENDERING_REGISTRY = locprefix + ".missing_rendering_registry";

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
