package artic.text;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Handles the localization of {@link String}s.  This class can load multiple properties
 * files for localization.
 * 
 * @author russell
 */
public class TextLocalizer {
  public TextLocalizer() {
    strings = new HashMap<>();
  }
  
  /**
   * Reads in a {@link ResourceBundle} with the given name and loads the contents
   * into the {@link TextLocalizer}.
   * 
   * @param filename The name of the {@link ResourceBundle} to load.
   */
  public void loadFile(String filename) {
    ResourceBundle bun = ResourceBundle.getBundle(filename);
    
    loadResource(bun);
  }
  
  /**
   * Loads the contents of a {@link ResourceBundle} into this {@link TextLocalizer}.
   * 
   * @param bundle The bundle to load.
   */
  public void loadResource(ResourceBundle bundle) {
    for(String key : bundle.keySet())
      addValue(key, bundle.getString(key));
  }
  
  /**
   * Adds a new {@link String} to the {@link TextLocalizer}.  If there is already a
   * {@link String} for the given key then this method will not do anything.
   * 
   * @param key The key that the value will be assigned to.
   * @param value The value to assign to the key.
   */
  public void addValue(String key, String value) {
    if(strings.containsKey(key)) return;
    
    strings.put(key, value);
  }
  
  /**
   * Checks whether there is a localized {@link String} for the given key.
   * 
   * @param key The key to check.
   * @return Whether there is a localized {@link String} assigned to the key.
   */
  public boolean exists(String key) {
    return strings.containsKey(key);
  }
  
  /**
   * Returns the localized version of the given {@link String}.  If there is no localized
   * version available then this will simply return the given {@link String}.
   * 
   * @param key The string to localize
   * @return Either the localized {@link String} or given {@link String}
   */
  public String localize(String key) {
    if(strings.containsKey(key)) return strings.get(key);
    
    return key;
  }
  
  private Map<String, String> strings;
}
