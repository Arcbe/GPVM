/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.util;

import taiga.code.registration.RegisteredObject;
import taiga.code.registration.ReusableObject;

/**
 * Manages a collection of {@link Setting}s.  This class can be added to a tree
 * of {@link RegisteredObject}s and also has methods for loading and saving the
 * {@link Setting}s.
 * 
 * @author russell
 */
public class SettingManager extends ReusableObject {
  
  public static final String SETTINGMANAGER_NAME = "settings";

  public SettingManager() {
    super(SETTINGMANAGER_NAME);
  }

  @Override
  protected void resetObject() {
    removeALlChildren();
  }
  
}
