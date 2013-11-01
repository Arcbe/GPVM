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
  public int getFieldId(String field) {
    assert fieldids.containsKey(field);
    
    return fieldids.get(field);
  }
  
  public int[] getFieldIds(String[] fields) {
    int[] result = new int[fields.length];
    
    for(int i = 0; i < result.length; i++)
      result[i] = getFieldId(fields[i]);
    
    return result;
  }
  
  public int[] getFieldIds(Collection<String> fields) {
    int[] result = new int[fields.size()];
    Iterator<String> it = fields.iterator();
    
    for(int i = 0; i < result.length; i++) {
      result[i] = getFieldId(it.next());
    }
    
    return result;
  }
  
  public TileData createData(String ... fields) {
    return createData(getFieldIds(fields));
  }
  
  public TileData createData(Collection<String> fields) {
    return createData(getFieldIds(fields));
  }
  
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
