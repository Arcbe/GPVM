package gpvm.map;

import com.sun.istack.internal.logging.Logger;
import gpvm.util.Settings;
import gpvm.util.error.UnknownDataType;
import java.util.HashMap;
import java.util.Set;

/**
 * Contains an arbitrary set of named fields for a tile.
 */
public final class TileData {
  /**
   * A read only instance of TileData.
   */
  public class ReadOnlyTileData {
    /**
     * @param key 
     * @return 
     * @see TileData#isPresent(int)  
     */
    public boolean isPresent(int key) {
      return TileData.this.isPresent(key);
    }
    
    /**
     * @return 
     * @see TileData#getValueID()  
     */
    public Set<Integer> getValueIDs() {
      return TileData.this.getValueIDs();
    }
    
    /**
     * @param key 
     * @return 
     * @see TileData#getType(int)  
     */
    public DataType getType(int key) {
      return TileData.this.getType(key);
    }
    
    /**
     * @param key 
     * @return 
     * @see TileData#getFloatValue(int) 
     */
    public float getFloatValue(int key) {
      return TileData.this.getFloatValue(key);
    }
    
    /**
     * @param key 
     * @return 
     * @see TileData#getIntValue(int) 
     */
    public int getIntValue(int key) {
      return TileData.this.getIntValue(key);
    }
    
    /**
     * @param key 
     * @return 
     * @see TileData#getStringValue(int) 
     */
    public String getStringValue(int key) {
      return TileData.this.getStringValue(key);
    }
  }
  
  /**
   * Enumerates the available data types for the fields in the tile data.
   */
  public enum DataType {
    /**
     *
     */
    Integer((byte)0),
    /**
     *
     */
    Float((byte)1),
    /**
     *
     */
    String((byte)2);
    
    final byte value;

    private DataType(byte value) {
      this.value = value;
    }
    
    /**
     * Returns the class associated with the given DataType.  The values
     * for the enum correspond with the name of the associate class.
     * 
     * @param type The enum that the class is associated with.
     * @return The associated class.
     */
    public static Class<?> getClass(DataType type) {
      switch(type) {
        case Integer: return Integer.class;
        case Float: return Float.class;
        case String: return String.class;
        default:
          Logger.getLogger(DataType.class).severe(Settings.getLocalString("err_unknown_data_type"));
          throw new UnknownDataType();
      }
    }
    
    /**
     * Returns a DataType for a given object.  The object must be of a class
     * corresponding to an enum otherwise this method will throw an
     * IllegalArgumentException.
     * 
     * @param obj The Object to determine the type of.
     * @return The type of the given object.
     */
    public static DataType getType(Object obj) {
      return DataType.valueOf(obj.getClass().getSimpleName());
    }
  } //end DataType

  /**
   * Constructs an empty TileData.
   */
  public TileData() {
    values = new HashMap<>();
  }

  /**
   * Returns a list of all field names contained in the TileData.
   *
   * @return A list of all field names.
   */
  public Set<Integer> getValueIDs() {
    return values.keySet();
  }
  
  /**
   * Returns whether a key exists in this TileData.
   * 
   * @param key The key to check
   * @return Whether the given key exists
   */
  public boolean isPresent(int key) {
    return values.containsKey(key);
  }

  /**
   * Returns the data type of a given field.
   *
   * @param key The field to get the DataType from.
   * @return The data type of the given field.
   */
  public DataType getType(int key) {
    assert isPresent(key);
    
    return DataType.getType(values.get(key));
  }

  /**
   * Returns the value of an float field in the TileData.  The key
   * must correspond to a valid float field.
   *
   * @param key The name of an float field
   * @return The value of the given field
   */
  public float getFloatValue(int key) {
    assert isPresent(key);
    
    Object value = values.get(key);
    assert value instanceof Float;
    return (Float) value;
  }

  /**
   * Returns the value of an integer field in the TileData.  The key
   * must correspond to a valid integer field.
   *
   * @param key The id of an integer field
   * @return The value of the given field
   */
  public int getIntValue(int key) {
    assert isPresent(key);
    
    Object value = values.get(key);
    assert value instanceof Integer;
    return (Integer)value;
  }
  
  /**
   * Returns the value of a String field in the TileData.  The key
   * must correspond to a valid String field.
   *
   * @param key The id of an String field
   * @return The value of the given field
   */
  public String getStringValue(int key) {
    assert isPresent(key);
    
    Object value = values.get(key);
    assert value instanceof String;
    return (String)value;
  }
  
  /**
   * Sets a value for a string field.  This method cannot be used to create
   * a new field.
   * 
   * @param key The id of the field
   * @param value The value to assign the field.
   */
  public void setString(int key, String value) {
    assert isPresent(key);
    assert values.get(key) instanceof String;
    
    values.put(key, value);
  }
  
  /**
   * Sets a value for a float field.  This method cannot be used to create
   * a new field.
   * 
   * @param key The id of the field
   * @param value The value to assign the field.
   */
  public void setFloat(int key, float value) {
    assert isPresent(key);
    assert values.get(key) instanceof Float;
    
    values.put(key, value);
  }
  
  /**
   * Sets a value for a integer field.  This method cannot be used to create
   * a new field.
   * 
   * @param key The id of the field
   * @param value The value to assign the field.
   */
  public void setInteger(int key, int value) {
    assert isPresent(key);
    assert values.get(key) instanceof Integer;
    
    values.put(key, value);
  }
  
  private HashMap<Integer, Object> values;
}
