package gpvm.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * A registry of all {@link TileDefinition} indexed by tile id.  This class
 * also assigns ids to {@link TileDefinition} when they are added and allows
 * ids to be looked by canonical name.
 * 
 * @author russell
 */
public final class TileRegistry {
  
  /**
   * An instance of a TileRegistry with only function that can read its state.
   */
  public class ReadOnlyTileRegistry {
    /**
     * @see TileRegistry#containsTileID(long) 
     * @param id
     * @return 
     */
    public boolean containsTileID(long id) {
      return TileRegistry.this.containsTileID(id);
    }
    
    /**
     * @see TileRegistry#getDefinition(long) 
     * @param tileid
     * @return 
     */
    public TileDefinition getDefinition(long tileid) {
      return TileRegistry.this.getDefinition(tileid);
    }
    
    /**
     * @see TileRegistry#getTileID(java.lang.String) 
     * @param tileid
     * @return 
     */
    public long getTileID(String str) {
      return TileRegistry.this.getTileID(str);
    }
    
    /**
     * @see TileRegistry#getTileIDs(java.lang.String[]) 
     * @param tileid
     * @return 
     */
    public long[] getTileIDs(String ... names) {
      return TileRegistry.this.getTileIDs(names);
    }
    
    /**
     * @see TileRegistry#getTileIDs(java.util.Collection) 
     * @param tileid
     * @return 
     */
    public long[] getTileIDs(Collection<String> names) {
      return TileRegistry.this.getTileIDs(names);
    }
    
    /**
     * @see TileRegistry#getTileIDs() 
     * @param tileid
     * @return 
     */
    public long[] getTileIDs() {
      return TileRegistry.this.getTileIDs();
    }
  }
  
  /**
   * Adds a listener for changes in the structure of the {@link TileRegistry}.
   * 
   * @param list The listener to add.
   */
  public void addListener(TileRegistryListener list) {
    listeners.add(list);
  }

  /**
   * Creates an empty {@link TileRegistry}.
   */
  public TileRegistry() {
    init();
  }
  
  /**
   * Checks whether a certain tile id has a {@link TileDefinition}
   * associate with it.
   * 
   * @param id The id to check.
   * @return Whether there is a {@link TileDefinition}
   */
  public boolean containsTileID(long id) {
    return curindex > id;
  }
  
  /**
   * Removes all entries in this registry.
   */
  public void clear() {
    curindex = 1;
    tileids.clear();
    tiles.clear();
    
    fireRegCleared();
  }
  
  /**
   * Adds a new {@link TileDefinition} to this registry and assigns is 
   * a base Tile id.
   * 
   * @param def The {@link TileDefinition} to add.
   * @return The tile id assigned to the {@link TileDefinition}
   */
  public long addDefition(TileDefinition def) {
    //The tile metadata is store in the id, so 
    long newid = curindex;
    curindex += def.metadata + 1;
    
    //insert the new entry at the end of the array
    //since the new id will always be higher than all previous ids the tiles
    //ArrayList will always be sorted.
    TileEntry newent = new TileEntry();
    newent.TileID = newid;
    newent.def = def;
    tiles.add(newent);
    
    tileids.put(def.canonname, newid);
    fireAddedDefinition(def, newid);
    
    return newid;
  }
  
  /**
   * Returns a definition for a given tile id.
   * 
   * @param id The tile id for the requested tile.
   * @return The definition of the tile.
   */
  public TileDefinition getDefinition(long id) {
    assert curindex > id;
    
    return tiles.get(getArrayIndex(id)).def;
  }
  
  
  
  /**
   * Returns the tile id associated with a given name.
   * 
   * @param name The canonical name of a tile.
   * @return The id of the tile.
   */
  public long getTileID(String name) {
    return tileids.get(name);
  }
  
  /**
   * Returns the tile id associated with a set of tile names.
   * 
   * @param names The canonical name of the tiles
   * @return The ids of the named tiles.
   */
  public long[] getTileIDs(String ... names) {
    long[] result = new long[names.length];
    
    for(int i = 0; i < result.length; i++)
      result[i] = getTileID(names[i]);
    
    return result;
  }
  
  /**
   * Returns the tile ids for a collection of tiles.
   * 
   * @param names The canonical names of the tiles
   * @return The ids for the tiles.
   */
  public long[] getTileIDs(Collection<String> names) {
    long[] result = new long[names.size()];
    Iterator<String> it = names.iterator();
    
    for(int i = 0; i < result.length; i++)
      result[i] = getTileID(it.next());
    
    return result;
  }
  
  /**
   * Returns a list of all registered tile ids.
   * 
   * @return A list of all tile ids.
   */
  public long[] getTileIDs() {
    Collection<Long> keys = tileids.values();
    
    long[] result = new long[keys.size()];
    
    Iterator<Long> it = keys.iterator();
    for(int i = 0; i < result.length; i++) {
      result[i] = it.next();
    }
    
    return result;
  }
  
  private void init() {
    curindex = 1;
    tiles = new ArrayList<>();
    tileids = new HashMap<>();
    listeners = new ArrayList<>();
  }
  
  private void fireRegCleared() {
    for(TileRegistryListener l : listeners)
      l.registryCleared();
  }
  
  private void fireAddedDefinition(TileDefinition def, long id) {
    for(TileRegistryListener l: listeners) {
      l.entryAdded(def, id);
    }
  }
  
  // finds the index in the array of entrys for that id.
  // Basially a binary search that checks for a range of values for each id.
  private int getArrayIndex(long id) {
    int start = 0;
    int end = tiles.size() - 1;
    
    int result;
    while(start != end) {
      result = (start + end) / 2;
      
      long test = tiles.get(result).TileID;
      if(test == id) return result;
      else if(test > id) {
        end = result;
      } else if(tiles.get(result + 1).TileID > id) {
        return result;
      } else {
        start = result;
      }
    }
    
    throw new RuntimeException("Illegal execution path.");
  }
  
  private long curindex;
  private ArrayList<TileEntry> tiles;
  private HashMap<String, Long> tileids;
  private ArrayList<TileRegistryListener> listeners;
  
  private static class TileEntry {
    public long TileID;
    public TileDefinition def;
  }
}
