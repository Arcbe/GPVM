package taiga.gpvm.render;

import org.lwjgl.util.vector.Vector3f;
import taiga.code.math.Matrix4;

/**
 * This class creates the viewing matrix for OpenGL rendering.
 * 
 * @author russell
 */
public abstract class Camera {
  
  /**
   * Setups up the OpenGL projection matrix with this camera.
   */
  public Matrix4 getProjection() {
    return null;
  }
  
  /**
   * Returns the {@link Vector3f} in the upward direction of this {@link Camera}.
   * 
   * @return The up {@link Vector3f}.
   */
  public abstract Vector3f getUpVector();
  
  /**
   * Returns the {@link Vector3f} for the position of the {@link Camera}.
   * 
   * @return The position of the {@link Camera}.
   */
  public abstract Vector3f getPosition();
  /**
   * Returns the {@link Vector3f} in the direction that the {@link Camera} is facing.
   * 
   * @return The direction the {@link Camera} is facing.
   */
  public abstract Vector3f getDirection();
  
  /**
   * Returns the field of view for this {@link Camera} in degrees.
   * 
   * @return The field of view.
   */
  public abstract float getFOV();
  
  /**
   * Returns the near plane of the viewing frustum.
   * 
   * @return The near plane of the view frustum.
   */
  public abstract float getNearPlane();
  
  /**
   * Returns the far plane of the viewing frustum.
   * 
   * @return The far plane of the view frustum.
   */
  public abstract float getFarPlane();
}
