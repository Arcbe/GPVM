package placeholder.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * An array list that can handle sparse indices.  Each index can only have 
 * a single entry associated with it.
 * 
 * @author russell
 */
public class IntMap <T> extends ArrayList<IntMap.MapEntry<T>> {

  public IntMap() {
  }
  
  public IntMap(Collection<? extends MapEntry<T>> c) {
    super(c);
  }

  public IntMap(int initialCapacity) {
    super(initialCapacity);
  }
  
  /**
   * Returns an array of all the indices in this map.
   * 
   * @return An array of indices
   */
  public long[] keySet() {
    long[] result = new long[size()];
    
    for(int i = 0; i < result.length; i++)
      result[i] = get(i).index;
    
    return result;
  }
  
  /**
   * Checks whether an index is present in this map.
   * 
   * @param key The index to check
   * @return Whether the index is present.
   */
  public boolean containsKey(long key) {
    MapEntry<T> tar = new MapEntry<>(key, null);
    return Collections.binarySearch(this, tar) >= 0;
  }
  
  /**
   * Inserts a new element with the given index, or updates the entry at a given
   * index if there is already one present.
   * 
   * @param entry The element that will be added to the list
   * @param index The index of the new element.
   */
  public void insert(long index, T entry) {
    MapEntry<T> newentry = new MapEntry<>(index, entry);
    
    //find a spot for the new entry.
    int ind = Collections.binarySearch(this, newentry);
    if(ind >= 0) {
      //there is already an entry update it
      get(ind).entry = entry;
    } else {
      ind = - ind - 1;
      add(ind, newentry);
    }
  }
  
  /**
   * Retrieves the element with the given index, or returns null if no
   * such element exists.
   * 
   * @param index The index of the element to retrieve
   * @return The element from the list, or null.
   */
  public T getEntry(long index) {
    //find the entry in the list.
    MapEntry<T> target = new MapEntry<>(index, null);
    int ind = Collections.binarySearch(this, target);
    
    //make sure that there is actually an entry to return.
    target = get(ind);
    if(target.index != index) return null;
    else return target.entry;
  }
  
  /**
   * Contains the entries in the IntMap.  Each entry has an associated index
   * which is used to compare entries and determine equivalence.
   * @param <T> 
   */
  protected static final class MapEntry <T> implements Comparable<MapEntry<T>>{
    public long index;
    public T entry;

    public MapEntry(long index, T entry) {
      this.index = index;
      this.entry = entry;
    }

    @Override
    public int compareTo(MapEntry<T> t) {
      if(index == t.index) return 0;
      else if(index > t.index) return 1;
      else return -1;
    }
    
    @Override
    public boolean equals(Object o) {
      if(!(o instanceof MapEntry)) return false;
      
      return ((MapEntry)o).index == index;
    }

    @Override
    public int hashCode() {
      int hash = 5;
      hash = (int) (97 * hash + this.index);
      return hash;
    }
  }
}
