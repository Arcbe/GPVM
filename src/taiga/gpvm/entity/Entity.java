/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.entity;

import java.util.logging.Logger;
import tagia.code.geom.BoundingVolume;
import taiga.code.io.DataNode;

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
public abstract class Entity{
  public final long id;

  public Entity(long id) {
    this.id = id;
  }
  
  public abstract void render(int pass);
  public abstract BoundingVolume getBounds();

  private static final String locprefix = Entity.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
