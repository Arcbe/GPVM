package taiga.gpvm.render;

import org.lwjgl.util.vector.Vector3f;
import taiga.code.graphics.Camera;

/**
 * A simple camera that just stores the acts as a storage for the various
 * rendering parameters.
 * 
 * @author russell
 */
public class StationaryCamera extends Camera {

  /**
   * The up direction of the {@link StationaryCamera}.
   */
  public final Vector3f up;
  /**
   * The direction the {@link StationaryCamera} is looking.
   */
  public final Vector3f direction;
  /**
   * The position of the {@link StationaryCamera}.
   */
  public final Vector3f position;
  /**
   * The field of view.
   */
  public float fov;
  /**
   * The near plane of the viewing frustum.
   */
  public float near;
  /**
   * The far plane of the viewing frustum.
   */
  public float far;

  /**
   * Creates a new {@link StationaryCamera} with a 60 degree viewing angle and
   * 1 and 100 for the near and far planes respectively.
   */
  public StationaryCamera() {
    fov = 60;
    near = 1;
    far = 100;
    
    up = new Vector3f();
    direction = new Vector3f();
    position = new Vector3f();
  }

  /**
   * Creates a new {@link StationaryCamera} from the given values.
   * 
   * @param up The up direction.
   * @param direction The direction the {@link StationaryCamera} is looking.
   * @param position The position of the {@link StationaryCamera}.
   * @param fov The field of view
   * @param near The near plane of the viewing frustum.
   * @param far The far plane of the viewing frustum.
   */
  public StationaryCamera(Vector3f up, Vector3f direction, Vector3f position, float fov, float near, float far) {
    this.up = up;
    this.direction = direction;
    this.position = position;
    this.fov = fov;
    this.near = near;
    this.far = far;
  }
  
  @Override
  public Vector3f getUpVector() {
    if(up == null) {
      return new Vector3f(0,0,1);
    } else {
      return up;
    }
  }

  @Override
  public Vector3f getPosition() {
    if(position == null) {
      return new Vector3f();
    } else {
      return position;
    }
  }

  @Override
  public Vector3f getDirection() {
    if(direction == null) {
      return new Vector3f(1,0,0);
    } else {
      return direction;
    }
  }

  @Override
  public float getFOV() {
    return fov;
  }

  @Override
  public float getNearPlane() {
    return near;
  }

  @Override
  public float getFarPlane() {
    return far;
  }
  
}
