/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gpvm.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author russell
 */
public class Registrar<T extends RegisteredObject> {

  public Registrar() {
    entries = new HashMap<>();
  }
  
  
  public void generateIDs() {
    log.log(Level.FINE, "registrar.generating_ids");
    
    log.log(Level.INFO, "registrar.generated_ids");
  }
  
  public void assignIDs(Map<String, Integer> assignment) {
    
  }
  
  public T getEntry(String name) {
    return entrynames.get(name);
  }
  
  public T getEntry(int id) {
    if(entries == null) return null;
    
    return entries.get(id);
  }
  
  public void addEntry(T ent) {
    entrynames.put(ent.getName(), ent);
    
    log.log(Level.INFO, "registrar.added_entry", ent.getName());
  }
  
  private Map<String, T> entrynames;
  private Map<Integer, T> entries;
  
  private Logger log = Logger.getLogger(Registrar.class.getName(), System.getProperty("arctic.resources.text"));
}
