package taiga.code.io;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.io.DataFileManager;
import taiga.code.util.DataNode;
import taiga.code.registration.NamedObject;
import taiga.code.registration.ReusableObject;
import taiga.code.util.Setting;

/**
 * Manages a collection of {@link Setting}s.  This class can be added to a tree
 * of {@link NamedObject}s and also has methods for loading and saving the
 * {@link Setting}s.
 * 
 * @author russell
 */
public class SettingManager extends ReusableObject {
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
    NamedObject obj = getObject(DataFileManager.DATAFILEMANAGER_NAME);
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
      
      for(NamedObject cur : node) {
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
  public <V> Setting<V> getSetting(String name) {
    NamedObject set = getObject(name);
    if(set == null || !(set instanceof Setting)) return null;
    
    return (Setting) set;
  }

  @Override
  protected void resetObject() {
    removeAllChildren();
  }
  
  private <V> Setting<V> addData(DataNode node) {
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
  
  private static final String NO_DFMANAGER = locprefix + ".no_dfmanager";
  private static final String IO_EXCEPTION = locprefix + ".io_exception";
  private static final String NO_DATA = locprefix + ".no_data";
  
  private static final Logger log = Logger.getLogger(SettingManager.class.getName(),
    System.getProperty("taiga.code.logging.text"));
}
