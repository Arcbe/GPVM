package taiga.gpvm.render;

import org.lwjgl.opengl.Display;
import taiga.code.math.Matrix4;
import taiga.code.math.Matrix4Utils;
import taiga.code.math.Vector3;
import taiga.code.opengl.PerspectiveCamera;

/**
 * A simple camera that just stores the acts as a storage for the various
 * rendering parameters.
 * 
 * @author russell
 */
public class StationaryCamera extends PerspectiveCamera {

  /**
   * The up direction of the {@link StationaryCamera}.
   */
  public final Vector3 up;
  /**
   * The direction the {@link StationaryCamera} is looking.
   */
  public final Vector3 direction;
  /**
   * The position of the {@link StationaryCamera}.
   */
  public final Vector3 position;

  /**
   * Creates a new {@link StationaryCamera} with a pi/3 viewing angle and
   * 1 and 100 for the near and far planes respectively.
   */
  public StationaryCamera() {
    setFOV((float) Math.PI / 3);
    setFar(1);
    setFar(100);
    setAspect((float) Display.getWidth() / (float) Display.getHeight());
    
    up = new Vector3();
    direction = new Vector3();
    position = new Vector3();
    view = new Matrix4();
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
  public StationaryCamera(Vector3 up, Vector3 direction, Vector3 position, float fov, float near, float far) {
    super(fov, near, far, (float) Display.getWidth() / (float) Display.getHeight());
    
    this.up = up;
    this.direction = direction;
    this.position = position;
    view = new Matrix4();
  }

  @Override
  public Matrix4 getViewMatrix() {
    return Matrix4Utils.lookAt(position, up, direction, view);
  }
  
  private final Matrix4 view;
}
