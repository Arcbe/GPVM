/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.util;

import java.util.ResourceBundle;
import org.lwjgl.opengl.DisplayMode;

/**
 * Loads and stores the settings for the game.
 * 
 * @author russell
 */
public class Settings {
  private static ResourceBundle bundle;
  private static String bundlename;
  
  //various settings for the game along with their defaults
  private static DisplayMode dispmod = new DisplayMode(800, 600);
  
  /**
   * Loads the resource bundle for strings using the default locale.
   * 
   * @param name The base name for the resource file.
   */
  public static void loadStringBundle(String name) {
    bundle = ResourceBundle.getBundle(name);
    bundlename = name;
  }
  
  public static String getStringBundleName() {
    return bundlename;
  }
  
  /**
   * Returns a localized string for the given key.  Strings are defined in
   * a properties file and must first be loaded with the loadBundle method before
   * this method should be used.
   * 
   * @param key The key for the string that should be returned.
   * @return The string associated with the given key.
   */
  public static String getLocalString(String key) {
    assert bundle != null && bundle.containsKey(key);
    
    if(bundle == null) {
      throw new NullPointerException(java.util.ResourceBundle.getBundle("text").getString("NO RESOURCE BUNDLE HAS BEEN LOADED."));
    }
    
    return bundle.getString(key);
  }

  public static DisplayMode getDisplayMode() {
    return dispmod;
  }
}
