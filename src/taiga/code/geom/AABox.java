/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.geom;

import java.util.logging.Logger;
import org.lwjgl.util.vector.Vector3f;

/**
 * Represents an axis-aligned 3d rectangular prism.
 * 
 * @author russell
 */
public class AABox implements BoundingVolume {

  /**
   * Creates a 1x1x1 {@link AABox} with a corner at the origin.
   */
  public AABox() {
    blfcorner = new Vector3f();
    trbcorner = new Vector3f(1, 1, 1);
  }

  /**
   * Creates a new {@link AABox} with the smallest size and the given points as corners.
   * 
   * @param p1 The first corner for the box, this can be any corner.
   * @param p2 Te second corner for the box, this can be any other corner.
   */
  public AABox(Vector3f p1, Vector3f p2) {
    blfcorner = new Vector3f(
      Math.min(p1.x, p2.x),
      Math.min(p1.y, p2.y),
      Math.min(p1.z, p2.z));
   
    trbcorner = new Vector3f(
      Math.max(p1.x, p2.x),
      Math.max(p1.y, p2.y),
      Math.max(p1.z, p2.z));
  }
  
  /**
   * Returns the size of the {@link AABox} in the z direction.
   * 
   * @return The height of the {@link AABox}.
   */
  public float getHeight() {
    return trbcorner.z - blfcorner.z;
  }

  @Override
  public AABox getBounds() {
    return this;
  }
  
  public boolean collides(AABox box) {
    return 
      blfcorner.x <= box.trbcorner.x &&
      box.blfcorner.x <= trbcorner.x &&
      blfcorner.y <= box.trbcorner.y &&
      box.blfcorner.y <= trbcorner.y &&
      blfcorner.z <= box.trbcorner.z &&
      box.blfcorner.z <= trbcorner.z;
  }
  
  private final Vector3f blfcorner;
  private final Vector3f trbcorner;
  
  private static final String locprefix = AABox.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
