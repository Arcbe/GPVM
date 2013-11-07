package gpvm.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import static gpvm.map.TileData.DataType;

/**
 * Maintains a list of all the fields available for tile data.  Fields must
 * be registered before they can be added and used in TileData.  Once a field has
 * been registered it cannot be unregistered.  Each field will be assigned an id
 * that can be used to access the field in TileData.
 * 
 * @author russell
 */
public final class FieldRegistry {
  /**
   * Gets the id associated with a given field name.
   * 
   * @param field The name of the field.
   * @return The id for the queried field.
   */
  public int getFieldId(String field) {
    assert fieldids.containsKey(field);
    
    return fieldids.get(field);
  }
  
  /**
   * Gets an array of ids for an array of field names.  The indices match
   * between the id array and the name array.
   * 
   * @param fields An array of field names to look up.
   * @return The ids of the fields.
   */
  public int[] getFieldIds(String[] fields) {
    int[] result = new int[fields.length];
    
    for(int i = 0; i < result.length; i++)
      result[i] = getFieldId(fields[i]);
    
    return result;
  }
  
  /**
   * Gets an array of ids for a collection of field names.  The ids are
   * placed into the array in the order of the collections iterator.
   * 
   * @param fields The names of the fields to look up an id for
   * @return The ids of the queried fields.
   */
  public int[] getFieldIds(Collection<String> fields) {
    int[] result = new int[fields.size()];
    Iterator<String> it = fields.iterator();
    
    for(int i = 0; i < result.length; i++) {
      result[i] = getFieldId(it.next());
    }
    
    return result;
  }
  
  /**
   * Creates a {@link TileData} that contains all of the given fields.
   * 
   * @param fields The names of the fields that should be present in the {@link TileData}
   * @return The requested {@link TileData}
   */
  public TileData createData(String ... fields) {
    return createData(getFieldIds(fields));
  }
  
  /**
   * Creates a {@link TileData} that contains all of the given fields.
   * 
   * @param fields The names of the fields that should be present in the {@link TileData}
   * @return The requested {@link TileData}
   */
  public TileData createData(Collection<String> fields) {
    return createData(getFieldIds(fields));
  }
  
  /**
   * Creates a {@link TileData} that contains all of the given fields.
   * 
   * @param fields The ids of the  fields that should be present in the {@link TileData}
   * @return The requested {@link TileData}
   */
  public TileData createData(int ... fields) {
    return null;
  }
  
  private ArrayList<FieldEntry> fields;
  private HashMap<String, Integer> fieldids;
  
  private class FieldEntry {
    public DataType type;
    public String name;
  }
}
