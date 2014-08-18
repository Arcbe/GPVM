/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.util;

import java.lang.ref.WeakReference;
import java.util.Set;
import taiga.code.registration.NamedObject;

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
