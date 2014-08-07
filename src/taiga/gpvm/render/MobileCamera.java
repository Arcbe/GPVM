/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.render;

import java.util.logging.Logger;
import taiga.code.math.Vector3;
import taiga.code.util.Updateable;

/**
 * A simple {@link Camera} that can be given a velocity which allows it to move in a direction
 * relative to the direction the {@link Camera} is facing.
 * @author russell
 */
public class MobileCamera extends StationaryCamera implements Updateable {
  /**
   * The distance that the {@link Camera} will move each update.  This is relative
   * to the direction the {@link MobileCamera} is facing, so the x direction is
   * parallel to the facing, z is perpendicular in the plane of the facing and
   * and up direction, and y is perpendicular to all the other {@link Vector3f}s.
   */
  public final Vector3 velocity;

  /**
   * Creates a new {@link MobileCamera} with no velocity and default parameters.
   */
  public MobileCamera() {
    this.velocity = new Vector3();
  }

  /**
   * Creates a new {@link MobileCamera} with the given velocity.
   * 
   * @param velocity The velocity for the camera.
   */
  public MobileCamera(Vector3 velocity) {
    this.velocity = velocity;
  }

  /**
   * Creates a new {@link MobileCamera} with the given parameters.
   * 
   * @param velocity The velocity of the {@link MobileCamera}.
   * @param up The up direction.
   * @param direction The direction the {@link StationaryCamera} is looking.
   * @param position The position of the {@link StationaryCamera}.
   * @param fov The field of view
   * @param near The near plane of the viewing frustum.
   * @param far The far plane of the viewing frustum.
   */
  public MobileCamera(Vector3 velocity, Vector3 up, Vector3 direction, Vector3 position, float fov, float near, float far) {
    super(up, direction, position, fov, near, far);
    this.velocity = velocity;
  }
  
  /**
   * Returns the component of {@link StationaryCamera#up} perpendicular to 
   * {@link StationaryCamera#direction}.
   * 
   * @return The {@link Vector3f} of the upward direction of the screen.
   */
  public Vector3 getScreenUp() {
    Vector3 result = direction.getClone();
    
    //normalize the direction
    float comp = direction.dot(up) / direction.lenSquare3D();
    
    result.scale(-comp);
    
    return result.add(up);
  }
  
  /**
   * Gets the direction for the right of the screen.
   * 
   * @return The right direction relative to the facing of the {@link MobileCamera}.
   */
  public Vector3 getRightDir() {
    Vector3 result = direction.getClone();
    
    return result.cross(up);
  }
  
  @Override
  public void update() {
    //TODO: change this to use the view matrix.
    Vector3 zvec = getScreenUp();
    Vector3 yvec = getRightDir();
    Vector3 xvec = direction.getClone();
    
    zvec.normalize();
    yvec.normalize();
    xvec.normalize();
    
    zvec.scale(velocity.getX());
    yvec.scale(velocity.getY());
    xvec.scale(velocity.getZ());
    
    position.add(zvec);
    position.add(yvec);
    position.add(xvec);
  }

  private static final String locprefix = MobileCamera.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
