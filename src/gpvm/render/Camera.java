/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.render;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

/**
 * A class to store information setting up the camera in OpenGL.
 * 
 * @author russell
 */
public class Camera {
  /**
   * The position of the camera
   */
  public Vector3f position;
  /**
   * The direction that the camera is facing
   */
  public Vector3f direction;
  /**
   * The direction that should be towards the top of the window in world space.
   */
  public Vector3f up;
  
  /**
   * The field of view for the camera in degrees.
   */
  public float fov;
  
  /**
   * The minimum distance an object has to be from the camera to be rendered.
   * Must be strictly greater than 0.
   */
  public float near;
  
  /**
   * The maximum distance that an object can be rendered at.
   */
  public float far;

  /**
   * Creates a camera at the origin facing in the positive x direction with 
   * the positive z direction being the up ward direction and a field of field
   * of 60 degrees.
   */
  public Camera() {
    position = new Vector3f(0,0,0);
    direction = new Vector3f(0,1,0);
    up = new Vector3f(0,0,1);
    fov = 60;
    near = 1;
    far = 1000;
  }

  /**
   * Creates a new camera with the given parameters.
   * 
   * @param position The position of the camera.
   * @param direction The direction that the camera is facing. This does not
   * need to be normalized.
   * @param up The upward direction of the camera.
   * @param fov The field of view for the camera.
   * @param near The distance for the near clipping pane.
   * @param far The distance for the far clipping pane.
   */
  public Camera(Vector3f position, Vector3f direction, Vector3f up, float fov, float near, float far) {
    this.position = position;
    this.direction = direction;
    this.up = up;
    this.fov = fov;
    this.near = near;
    this.far = far;
  }
  
  /**
   * Setups up the OpenGL projection matrix with this camera.
   */
  public void loadCamera() {
    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadIdentity();
    GLU.gluPerspective(fov, (float) Display.getWidth() / (float)Display.getHeight(), near, far);
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
}
