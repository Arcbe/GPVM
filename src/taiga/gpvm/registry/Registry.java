/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.registration.RegisteredSystem;
import taiga.code.text.TextLocalizer;

/**
 *
 * @author russell
 */
public class Registry<T extends RegistryEntry> extends RegisteredSystem {

  public Registry(String name) {
    super(name);
    
    entries = new HashMap<>();
  }
  
  /**
   * Generates IDs for all contained {@link RegistryEntry}s.
   */
  public void generateIDs() {
    log.log(Level.INFO, "registrar.generating_ids");
    
    int curid = 0;
    
    entries = new HashMap<>();
    
    for(T ent : entrynames.values()) {
      entries.put(curid, ent);
      
      curid++;
    }
    
    log.log(Level.FINE, "registrar.generated_ids");
  }
  
  /**
   * Assigns IDs for all of the contained {@link RegistryEntry}s.  Any entry that
   * is not present in the assignment will not be given an id.  An error will be
   * thrown is there is an assignment for a non-existent entry.
   * 
   * @param assignment The ID assignments for the entries.
   */
  public void assignIDs(Map<String, Integer> assignment) {
    entries = new HashMap<>();
    
    for(String name : assignment.keySet()) {
      T entry = getEntry(name);
      if(entry == null) throw new MissingEntryException(name);
      
      entries.put(assignment.get(name), entry);
    }
  }
  
  /**
   * Retrieves the {@link RegistryEntry} with the given name.  If there is no entry
   * with the name it will return null.
   * 
   * @param name The name of the requested {@link RegistryEntry}
   * @return The requested {@link RegistryEntry}
   */
  public T getEntry(String name) {
    return entrynames.get(name);
  }
  
  /**
   * Retrieves the {@link RegistryEntry} with the given id.  If there is no entry
   * with the id it will return null.
   * 
   * @param id The id of the requested {@link RegistryEntry}
   * @return The requested {@link RegistryEntry}
   */
  public T getEntry(int id) {
    if(entries == null) return null;
    
    return entries.get(id);
  }
  
  /**
   * Adds a new entry to this {@link Registry}.  This entry will not be assigned
   * an id until the next time either {@link Registry#generateIDs()} or {@link Registry#assignIDs(java.util.Map)}
   * is called.  This cannot be called once the system is running.
   * 
   * @param ent The {@link RegistryEntry} to add.
   */
  public void addEntry(T ent) {
    if(isRunning()) {
      throw new IllegalStateException(TextLocalizer.localize(ALREADY_RUNNING));
    }
    
    entrynames.put(ent.name, ent);
    
    log.log(Level.FINE, ENTRY_ADDED, ent.name);
  }
  
  private Map<String, T> entrynames;
  private Map<Integer, T> entries;
  
  private static final String PREFIX = Registry.class.getName().toLowerCase();
  
  private static final String ENTRY_ADDED = PREFIX + ".added_entry";
  private static final String ALREADY_RUNNING = PREFIX + ".already_running";
  
  private Logger log = Logger.getLogger(Registry.class.getName(), 
    System.getProperty("taiga.logging.text"));

  @Override
  protected void startSystem() {}

  @Override
  protected void stopSystem() {}

  @Override
  protected void resetObject() {
    entries = null;
    entrynames.clear();
  }
}
