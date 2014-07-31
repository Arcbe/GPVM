package taiga.gpvm.render;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

/**
 * This class creates the viewing matrix for OpenGL rendering.
 * 
 * @author russell
 */
public abstract class Camera {
  
  /**
   * Setups up the OpenGL projection matrix with this camera.
   */
  public void setupProjectioMatrix() {
    Vector3f position = getPosition();
    Vector3f direction = getDirection();
    Vector3f up = getUpVector();
    
    float fov = getFOV();
    float near = getNearPlane();
    float far = getFarPlane();
    
    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadIdentity();
    GLU.gluPerspective(fov, (float) Display.getHeight()/ (float)Display.getWidth(), near, far);
    GLU.gluLookAt(
            position.x, 
            position.y, 
            position.z, 
            position.x + direction.x,
            position.y + direction.y,
            position.z + direction.z,
            up.x,
            up.y,
            up.z);
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
