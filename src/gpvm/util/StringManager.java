/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.DisplayMode;

/**
 * Handles {@link String} localization and formatting.
 * 
 * @author russell
 */
public class StringManager {
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
    if(bundle == null) {
      return "NO RESOURCE BUNDLE HAS BEEN REGISTERED";
    }
    
    try {
      return bundle.getString(key);
    } catch(MissingResourceException ex) {
      log.log(Level.SEVERE, "Missing entry for localized string {0}", key);
      return "";
    }
  }
  
  public static String getLocalString(String key, Object ... params) {
    String base = getLocalString(key);
    
    return MessageFormat.format(base, params);
  }

  public static DisplayMode getDisplayMode() {
    return dispmod;
  }
  
  private static final Logger log = Logger.getLogger(StringManager.class.getName());
}
