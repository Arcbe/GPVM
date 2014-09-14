/*
 * Copyright (c) 2014, Russell Smith, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
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
public abstract class ReusableObject extends NamedObject {

  /**
   * Creates a new {@link ReusableObject} with the given name.
   * 
   * @param name The name for the new {@link ReusableObject}.
   */
  public ReusableObject(String name) {
    super(name);
  }
  
  /**
   * Resets this {@link Object} as well as all of its children recursively to
   * its state immediately after construction.
   */
  public final void reset() {
    //reset all of the children
    for(NamedObject obj : this)
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
