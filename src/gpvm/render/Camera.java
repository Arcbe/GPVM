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

  public Camera() {
    position = new Vector3f(0,0,0);
    direction = new Vector3f(1,0,0);
    up = new Vector3f(0,0,1);
    fov = 60;
    near = 1;
    far = 100;
  }

  public Camera(Vector3f position, Vector3f direction, Vector3f up, float fov) {
    this.position = position;
    this.direction = direction;
    this.up = up;
    this.fov = fov;
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