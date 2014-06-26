/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.entity;

import java.util.logging.Logger;
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector3f;
import tagia.code.geom.AABox;
import taiga.code.io.DataNode;
import taiga.gpvm.registry.EntityEntry;

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
public abstract class Entity {
  public final long id;
  public final EntityEntry type;

  public Entity(long id, EntityEntry type) {
    this.id = id;
    this.type = type;
    location = new Vector3f();
    velocity = new Vector3f();
    damage = 0;
  }
  
  public ReadableVector3f getLocation() {
    return new Vector3f(location);
  }
  
  public abstract void render(int pass);
  public abstract AABox getBounds();
  
  private final Vector3f location;
  private final Vector3f velocity;
  private final long damage;

  private static final String locprefix = Entity.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
