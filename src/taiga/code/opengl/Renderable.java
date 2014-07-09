/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.opengl;

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
  }
  
  /**
   * Updates this {@link Renderable} and all of its children recursively.
   */
  public void update() {
    if(!enabled) return;
    if(!initialized) {
      initalizeSelf();
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
   */
  public void render(int pass) {
    if(!enabled) return;
    
    renderSelf(pass);
    
    for(RegisteredObject obj : this) {
      if(obj != null && obj instanceof Renderable) {
        ((Renderable)obj).render(pass);
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
   */
  protected abstract void renderSelf(int pass);
  
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
  protected void initalizeSelf() {}
  
  /**
   * Called when this {@link Renderable} has been removed from the registration
   * tree.  Any resources created in the {@link #initalizeSelf() } method should
   * be freed here.
   */
  protected void uninitializeSelf() {}

  @Override
  protected void dettached(RegisteredObject parent) {
    super.dettached(parent);
    initialized = false;
  }
  
  /**
   * A flag for whether this {@link Renderable} should be updated and rendered.
   * If this is false then this along with any children will not be updated or
   * rendered.
   */
  protected boolean enabled;
  private boolean initialized;
  private int minpasses;
}
