/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.util;

/**
 * A listener for changes to {@link Setting}s.
 * @author russell
 */
public interface SettingListener {
  /**
   * Called when the {@link Setting} is changed.
   * 
   * @param set The {@link Setting} that was changed.
   */
  public void settingChanged(Setting set);
}
