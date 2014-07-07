package taiga.code.util;

/**
 * Represents an object that can be loaded and unloaded.
 * 
 * @author russell
 */
public interface Resource {
  /**
   * Loads this {@link Resource} so that it can be used.
   */
  public void load();
  
  /**
   * Unloads this {@link Resource} after it is no longer needed.
   */
  public void unload();
}
