/*
 * Copyright (C) 2014 Russell Smith
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package taiga.code.util;

import java.util.logging.Logger;
import taiga.code.opengl.Renderable;
import taiga.code.registration.NamedObject;

public abstract class UpdateableObject extends NamedObject implements Updateable {
  
  public UpdateableObject() {}
  
  public UpdateableObject(String name) {
    super(name);
  }
  
  /**
   * Updates this {@link Renderable} and all of its children recursively.
   */
  @Override
  public void update() {
    
    updateSelf();
    
    for(NamedObject obj : this) {
      if(obj != null && obj instanceof Renderable) {
        ((Renderable)obj).update();
      }
    }
  }
  
  protected abstract void updateSelf();

  private static final String locprefix = UpdateableObject.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
