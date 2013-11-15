
package gpvm.io;

import gpvm.util.Settings;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A node in a tree representing a data file.  Each node contains a named
 * {@link Map} of values including other {@link DataNode}s that can be accessed individually.
 * 
 * @author russell
 */
public final class DataNode {

  /**
   * Constructs a new {@link DataNode} with the given values.  Once a node
   * is created it should not be modified.
   * 
   * @param values 
   */
  public DataNode(Map<String, Object> values) {
    this.values = values;
  }

  /**
   * Checks whether a value is present within this {@link DataNode}
   * 
   * @param name The name of the value to check for.
   * @return Whether the value exists.
   */
  public boolean contains(String name) {
    if(values == null) return false;
    return values.containsKey(name);
  }
  
  /**
   * Gets the {@link Class} of the value with the given name.
   * 
   * @param name The name of the value to get the {@link Class} of.
   * @return The {@link Class} of the value with the given name.
   */
  public Class<?> valueType(String name) {
    if(!contains(name)) {
      String msg = String.format(Settings.getLocalString("err_no_data_value"), name);
      throw new NoDataValueException(msg);
    }
    
    return values.get(name).getClass();
  }
  
  public boolean isType(String name, Class type) {
    return contains(name) && values.get(name).getClass().equals(type);
  }
  
  /**
   * Returns the value associated with the given name.  If either the value
   * does not exist or is the wrong data type a warning is logged, and null is returned
   * 
   * @param name The name of the value to get.
   * @return The value associated with the given name or null.
   */
  public <T> T getValue(String name) {
    if(!contains(name)) {
      String msg = String.format(Settings.getLocalString("err_no_data_value"), name);
      throw new NoDataValueException(msg);
    }
    
    return (T) values.get(name);
  }

  /**
   * Returns a {@link Collection} of all the field names that have data
   * associated with them.
   * 
   * @return A {@link Collection} of all the field names.
   */
  public Collection<String> getValues() {
    return values.keySet();
  }
  
  private Map<String, Object> values;
}
