/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.registration;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A special type of {@link Object} that can be reset to its original state
 * immediately after construction.  This is in order to prevent excess memory
 * allocations by allowing the same {@link Object} to used multiple times.
 * @author russell
 */
public abstract class ReusableObject extends RegisteredObject {

  public ReusableObject(String name) {
    super(name);
  }
  
  /**
   * Resets this {@link Object} as well as all of its children recursively to
   * its state immediately after construction.
   */
  public final void reset() {
    //reset all of the children
    for(RegisteredObject obj : this)
      if(obj != null && obj instanceof ReusableObject)
        ((ReusableObject)obj).reset();
    
    //and this object
    resetObject();
    
    log.log(Level.INFO, RESET, getFullName());
  }
  
  /**
   * Resets this {@link Object} back to its state immediately after construction.
   */
  protected abstract void resetObject();
  
  private static final String RESET = ReusableObject.class.getName().toLowerCase() + ".reset";
  
  private static final Logger log = Logger.getLogger(ReusableObject.class.getName(),
    System.getProperty("taiga.code.logging.text"));
}
