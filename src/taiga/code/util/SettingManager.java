/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.io.DataFileManager;
import taiga.code.io.DataNode;
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
  
  /**
   * Loads settings from the {@link File} with the given path.
   * 
   * @param file The file to load {@link Setting}s from.
   */
  public void loadSettings(String file) {
    RegisteredObject obj = getObject(DataFileManager.DATAFILEMANAGER_NAME);
    if(obj == null || !(obj instanceof DataFileManager)) {
      log.log(Level.WARNING, NO_DFMANAGER);
      return;
    }
    
    try {
      DataNode node = ((DataFileManager)obj).readFile(file);
      
      if(node == null) {
        log.log(Level.SEVERE, NO_DATA, file);
        
        return;
      }
      
      for(RegisteredObject cur : node) {
        if(cur == null || !(cur instanceof DataNode)) continue;
        
        addChild(addData((DataNode)cur));
      }
    } catch(IOException ex) {
      log.log(Level.SEVERE, IO_EXCEPTION, ex);
    }
  }
  
  /**
   * Convenience method for getting a {@link Setting}.  If the {@link Setting}
   * is missing then this will return null.
   * 
   * @param name The name of the {@link Setting}
   * @return The {@link Setting} or null.
   */
  public Setting getSetting(String name) {
    RegisteredObject set = getObject(name);
    if(set == null || !(set instanceof Setting)) return null;
    
    return (Setting) set;
  }

  @Override
  protected void resetObject() {
    removeAllChildren();
  }
  
  private Setting addData(DataNode node) {
    Setting result = new Setting(node.name, node.data);
    
    for(RegisteredObject obj : node) {
      if(obj != null && obj instanceof DataNode) {
        Setting child = addData((DataNode) obj);
        
        result.addChild(child);
      }
    }
    
    return result;
  }
  
  private static final String locprefix = SettingManager.class.getName().toLowerCase();
  
  private static final String NO_DFMANAGER = locprefix + ".no_dfmanager";
  private static final String IO_EXCEPTION = locprefix + ".io_exception";
  private static final String NO_DATA = locprefix + ".no_data";
  
  private static final Logger log = Logger.getLogger(SettingManager.class.getName(),
    System.getProperty("taiga.code.logging.text"));
}
