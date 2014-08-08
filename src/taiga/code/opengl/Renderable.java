/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.opengl;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import taiga.code.math.Matrix4;
import taiga.code.math.ReadableMatrix4;
import taiga.code.registration.RegisteredObject;

/**
 * Base class for renderable objects.  {@link Renderable}s can be added to a
 * {@link GraphicsSystem} as a child and will be update and rendered on each
 * frame.
 * 
 * @author russell
 */
public abstract class Renderable extends RegisteredObject {
  
  /**
   * Controls whether this {@link Renderable} should render and update itself.
   * This will also stop any children from being rendered or updated if set to
   * false;
   * @param name
   */

  public Renderable(String name) {
    super(name);
    
    enabled = true;
    minpasses = 1;
    initialized = false;
    localtrans = new Matrix4();
    globaltrans = new Matrix4();
  }
  
  /**
   * Updates this {@link Renderable} and all of its children recursively.
   */
  public void update() {
    if(!enabled) return;
    if(!initialized) {
      initializeSelf();
      initialized = true;
    }
    
    updateSelf();
    
    for(RegisteredObject obj : this) {
      if(obj != null && obj instanceof Renderable) {
        ((Renderable)obj).update();
      }
    }
  }
  
  /**
   * Renders this {@link Renderable} and all of its children recursively.  Parents
   * are rendered before children.
   * 
   * @param pass The rendering pass for this call.  This allows for separating
   * {@link Renderable} into categories that will all be rendered in a defined
   * order.
   * @param proj The projection to use for this {@link Renderable}.
   */
  public void render(int pass, Matrix4 proj) {
    if(!enabled) return;
    
    proj = processProjection(proj, pass);
    
    renderSelf(pass, proj);
    
    for(RegisteredObject obj : this) {
      if(obj != null && obj instanceof Renderable) {
        ((Renderable)obj).render(pass, proj);
      }
    }
    
    postRendering(pass);
  }
  
  /**
   * The number of rendering passes this {@link Renderable} needs.  The
   * {@link Renderable#renderSelf(int) } method will called at least that many times
   * so that rendering can be split into multiple stages.
   * 
   * @return The number of rendering passes needed.
   */
  public int getNumberOfPasses() {
    for(RegisteredObject obj : this) {
      if(obj != null && obj instanceof Renderable) {
        int newpasses = ((Renderable)obj).getNumberOfPasses();
        if(newpasses > minpasses) minpasses = newpasses;
      }
    }
    
    return minpasses;
  }
  
  /**
   * Returns the transformation applied to this {@link Renderable}.
   * 
   * @return The local transformation.
   */
  public ReadableMatrix4 getLocalTransform() {
    return localtrans.asReadOnly();
  }
  
  /**
   * Sets the transformation matrix for this {@link Renderable}.
   * 
   * @param trans The new transformation matrix.
   */
  public void setLocalTransformation(ReadableMatrix4 trans) {
    localtrans.setValues(trans);
    updateGlobalTransform();
  }
  
  /**
   * Applies the given transformation matrix to the local transformation
   * of this {@link Renderable}.
   * 
   * @param trans The transformation to apply.
   */
  public void applyLocalTransformation(ReadableMatrix4 trans) {
    localtrans.mulRHS(trans, localtrans);
    updateGlobalTransform();
  }
  
  /**
   * Returns the composite transformation from all of the parents of this
   * {@link Renderable} along with itself.
   * 
   * @return The transformation from world coordinates to eye coordinates for
   * this {@link Renderable}.
   */
  public ReadableMatrix4 getGlobalTransform() {
    return globaltrans.asReadOnly();
  }
  
  /**
   * Sets the OpenGL model-view matrix to the global transformation {@link Matrix4}
   * for this {@link Renderable}.
   */
  public void applyGlobalModelView() {
    setGLMatrix(GL11.GL_MODELVIEW, getGlobalTransform());
  }
  
  /**
   * Sets the OpenGL projection matrix to the given {@link Matrix4}.
   * 
   * @param proj The new projection matrix.
   */
  public void applyProjection(ReadableMatrix4 proj) {
    setGLMatrix(GL11.GL_PROJECTION, proj);
  }
  
  /**
   * Sets the values for an Opengl matrix stack to the given value.  This
   * will not push or pop the stack.
   * 
   * @param matrixmode Which matrix stack to set.
   * @param mat The matrix to set the stack to.
   */
  public void setGLMatrix(int matrixmode, ReadableMatrix4 mat) {
    FloatBuffer buffer = matbuffer.get();
    buffer.rewind();
    mat.store(buffer, false);
    buffer.flip();
    
    GL11.glMatrixMode(matrixmode);
    GL11.glLoadMatrix(buffer);
  }
  
  /**
   * Sets the number of rendering passes this {@link Renderable} will request.
   * 
   * @param passes The number of passes to request.
   */
  protected void setPasses(int passes) {
    assert passes > 0;
    minpasses = passes;
  }
  
  /**
   * Method that allows this {@link Renderable} to update itself.  This is called
   * before rendering to allow {@link Renderable}s to calculate any changes to
   * the scene that might be needed.
   */
  protected abstract void updateSelf();
  
  /**
   * Method that allows this {@link Renderable} to render itself.  This is called
   * after updating and allows the {@link Renderable} to do the actual rendering.
   * Parents are rendered before children.
   * 
   * @param pass The rendering pass for this call.  This allows for separating
   * {@link Renderable} into categories that will all be rendered in a defined
   * order.
   * @param proj The projection that has been passed to this {@link Renderable}.
   */
  protected abstract void renderSelf(int pass, Matrix4 proj);
  
  /**
   * Called after all the children for this {@link Renderable} have be
   * rendered during a given pass.
   * 
   * @param pass The current rendering pass.
   */
  protected void postRendering(int pass) {}
  
  /**
   * Called before the first frame after the {@link GraphicsSystem} has be started
   * or the first frame after this {@link Renderable} has been added.
   */
  protected void initializeSelf() {}
  
  /**
   * Allows this {@link Renderable} to alter the projection it uses.  This
   * will also apply to any children {@link Renderable}s.  This method should
   * create a new {@link Matrix4} instead of using the given {@link Matrix4}.
   * 
   * @param proj The projection {@link Matrix4} given to this {@link Renderable}.
   * @param pass The current pass that is being rendered.
   * @return The {@link Matrix4} that should be used for this {@link Renderable}
   *  and its children.
   */
  protected Matrix4 processProjection(Matrix4 proj, int pass) { return proj; }
  
  /**
   * Called when this {@link Renderable} has been removed from the registration
   * tree.  Any resources created in the {@link #initializeSelf() } method should
   * be freed here.
   */
  protected void uninitializeSelf() {}

  @Override
  protected void dettached(RegisteredObject parent) {
    super.dettached(parent);
    initialized = false;
  }
  
  @Override
  protected void attached(RegisteredObject parent) {
    super.attached(parent);
    updateGlobalTransform();
  }
  
  private void updateGlobalTransform() {
    RegisteredObject obj = getParent();
    if(!(obj instanceof Renderable)) return;
    
    ((Renderable)obj).globaltrans.mul(localtrans, globaltrans);
    
    //now that we are updated update the children.
    for(RegisteredObject o : this)
      if(o instanceof Renderable)
        ((Renderable)o).updateGlobalTransform();
  }
  
  /**
   * A flag for whether this {@link Renderable} should be updated and rendered.
   * If this is false then this along with any children will not be updated or
   * rendered.
   */
  protected boolean enabled;
  private boolean initialized;
  private int minpasses;
  
  private final Matrix4 localtrans;
  private final Matrix4 globaltrans;
  
  /**
   * A {@link ThreadLocal} variable that stores a 16 element {@link FloatBuffer}
   * for use in transferring {@link Matrix4}s to OpenGL calls.
   */
  protected static final ThreadLocal<FloatBuffer> matbuffer = new ThreadLocal<FloatBuffer>() {
    @Override
    protected FloatBuffer initialValue() {
      return BufferUtils.createFloatBuffer(16);
    }
  };
}
