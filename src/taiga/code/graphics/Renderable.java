/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.graphics;

import taiga.code.registration.RegisteredObject;

/**
 *
 * @author russell
 */
public abstract class Renderable extends RegisteredObject {
  
  /**
   * Controls whether this {@link Renderable} should render and update itself.
   * This will also stop any children from being rendered or updated if set to
   * false;
   */

  public Renderable(String name) {
    super(name);
    
    enabled = true;
    minpasses = 1;
  }
  
  /**
   * Updates this {@link Renderable} and all of its children recursively.
   */
  public void update() {
    if(!enabled) return;
    
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
  }
  
  public int getNumberOfPasses() {
    int passes = minpasses;
    for(RegisteredObject obj : this) {
      if(obj != null && obj instanceof Renderable) {
        int newpasses = ((Renderable)obj).getNumberOfPasses();
        if(newpasses > minpasses) minpasses = newpasses;
      }
    }
    
    return minpasses;
  }
  
  public void setPasses(int passes) {
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
  
  private boolean enabled;
  private int minpasses;
}
