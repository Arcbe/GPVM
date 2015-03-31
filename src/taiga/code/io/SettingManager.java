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

package taiga.code.io;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.registration.NamedObject;
import taiga.code.registration.ReusableObject;
import taiga.code.util.DataNode;
import taiga.code.util.Setting;

/**
 * Manages a collection of {@link Setting}s.  This class can be added to a tree
 * of {@link NamedObject}s and also has methods for loading and saving the
 * {@link Setting}s.
 * 
 * @author russell
 */
public class SettingManager extends ReusableObject {
  private static final long serialVersionUID = 1L;
  
  /**
   * Default name for the {@link SettingManager} within the registration tree.
   */
  public static final String SETTINGMANAGER_NAME = "settings";

  /**
   * Creates a new {@link SettingManager}
   */
  public SettingManager() {
    super(SETTINGMANAGER_NAME);
  }
  
  /**
   * Loads settings from the {@link File} with the given path.
   * 
   * @param file The file to load {@link Setting}s from.
   */
  public void loadSettings(String file) {
    log.log(Level.FINEST, "loadSettings({0})", file);
    
    NamedObject obj = getObject(DataFileManager.DATAFILEMANAGER_NAME);
    if(obj == null || !(obj instanceof DataFileManager)) {
      log.log(Level.WARNING, "Unable to load settings, no data file manager found.");
      return;
    }
    
    try {
      DataNode node = ((DataFileManager)obj).readFile(file);
      
      if(node == null) {
        log.log(Level.SEVERE, "Unable to load settings, no valid data in file {0}.", file);
        
        return;
      }
      
      for(NamedObject cur : node) {
        if(cur == null || !(cur instanceof DataNode)) continue;
        
        addChild(addData((DataNode)cur));
      }
    } catch(IOException ex) {
      log.log(Level.SEVERE, "Exception while loading settings.", ex);
    }
    
    log.log(Level.CONFIG, "Settings file {0} loaded.", file);
  }
  
  /**
   * Convenience method for getting a {@link Setting}.  If the {@link Setting}
   * is missing then this will return null.
   * 
   * @param name The name of the {@link Setting}
   * @return The {@link Setting} or null.
   */
  public Setting getSetting(String name) {
    NamedObject set = getObject(name);
    if(set == null || !(set instanceof Setting)) return null;
    
    return (Setting) set;
  }

  @Override
  protected void resetObject() {
    removeAllChildren();
  }
  
  private Setting addData(DataNode node) {
    Setting result = new Setting(node.name, node.data);
    
    for(NamedObject obj : node) {
      if(obj != null && obj instanceof DataNode) {
        Setting child = addData((DataNode) obj);
        
        result.addChild(child);
      }
    }
    
    return result;
  }
  
  private static final String locprefix = SettingManager.class.getName().toLowerCase();
  
  private static final Logger log = Logger.getLogger(SettingManager.class.getName(),
    System.getProperty("taiga.code.logging.text"));
}
