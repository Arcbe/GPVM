package placeholder.map;

import com.sun.istack.internal.logging.Logger;
import java.util.HashMap;
import java.util.Set;
import placeholder.util.Settings;

/**
 * Represents a single unit of the game map.  Each tile has some tile type
 * that corresponds to a list of available tiles, along with an optional set of 
 * data fields.  A damage value is also stored in each tile.
 * 
 * @author russell
 */
public final class Tile {
  long type;
  long damage;
  TileData data;
  
  public enum DataType {
    INTEGER,
    FLOAT,
    STRING
  }
  
  /**
   * Contains an arbitrary set of named fields for a tile.
   */
  public static final class TileData {
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
    public Set<String> getValueNames() {
      return values.keySet();
    }
    
    /**
     * Returns the data type of a given field.
     * 
     * @param key The field to get the DataType from.
     * @return The data type of the given field.
     */
    public DataType getType(String key) {
      assert values.containsKey(key);
      return values.get(key).type;
    }
    
    /**
     * Returns the value of an float field in the TileData.  The key
     * must correspond to a valid float field.
     * 
     * @param key The name of an float field
     * @return The value of the given field
     */
    public float getFloatValue(String key) {
      assert values.containsKey(key);
      
      //check to make sure that there is actually a float here
      DataValue<?> val = values.get(key);
      assert val.type == DataType.FLOAT;
      
      return ((Float)val.data).floatValue();
    }
    
    /**
     * Returns the value of an integer field in the TileData.  The key
     * must correspond to a valid integer field.
     * 
     * @param key The name of an integer field
     * @return The value of the given field
     */
    public int getInteger(String key) {
      assert values.containsKey(key);
      
      DataValue<?> val = values.get(key);
      assert val.type == DataType.INTEGER;
      
      return ((Integer)val.data).intValue();
    }
    
    private HashMap<String, DataValue<?>> values;
    
    /**
     * A field within a TileData
     * @param <T> The type of the field
     */
    private static final class DataValue<T> {
      public DataType type;
      public T data;
      
      /**
       * Returns a DataValue object with a type corresponding to the given
       * DataType
       * 
       * @param type The type for the new DataValue
       * @return A DataValue with the given type or null for unknown types.
       */
      public static DataValue<?> getDataValue(DataType type) {
        switch(type) {
          default:
            Logger.getLogger(DataValue.class).severe(Settings.getLocalString("err_unknown_data_type"));
            return null;
          
          case INTEGER: return new DataValue<Integer>();
          case FLOAT:   return new DataValue<Float>();
          case STRING:  return new DataValue<String>();
        }
      }
    }
  }
}
