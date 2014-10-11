/*
 * Copyright (C) 2014 Russell Smith.
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
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package taiga.code.util;

import java.lang.ref.WeakReference;
import java.util.Set;

/**
 * A single named value.
 * 
 * @author russell
 */
public class Setting extends DataNode {
  /**
   * A text description of this setting.
   */
  public String description;
  
  /**
   * Creates a new {@link Setting} with the given name and null as its initial
   * value.
   * 
   * @param name 
   */
  public Setting(String name) {
    super(name);
  }
  
  /**
   * Creates a new {@link Setting} with the given name and initial value.
   * 
   * @param name The name of the {@link Setting}
   * @param val The initial value.
   */
  public Setting(String name, Object val) {
    super(name);
    
    data = val;
  }
  
  /**
   * Sets the value for this {@link Setting}.
   * 
   * @param val The new value.
   */
  public void setValue(Object val) {
    data = val;
    
    for(SettingListener list : listeners)
      list.settingChanged(this);
  }
  
  /**
   * Adds a {@link SettingListener} to this {@link Setting}.  Listeners are stored
   * as {@link WeakReference} so removing them from this {@link Setting} is not
   * necessary, however a strong reference must exist elsewhere to ensure that
   * the listener is not garbage collected.
   * 
   * @param list The listener to add.
   */
  public void addListener(SettingListener list) {
    listeners.add(list);
  }
  
  /**
   * Removes a {@link SettingListener} from this {@link Setting}, if the listener
   * is not currently added to this {@link Setting} then this method will do nothing.
   * 
   * @param list The listener to remove.
   */
  public void removeListener(SettingListener list) {
    listeners.remove(list);
  }
  
  private Set<SettingListener> listeners;
}
