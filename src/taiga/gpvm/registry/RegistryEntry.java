/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

/**
 * A name entry into a registry.
 * 
 * @author russell
 */
public class RegistryEntry {
  public final String name;
  protected int id;

  public RegistryEntry(String name) {
    this.name = name;
    
    id = -1;
  }
  
  public int getID() {
    return id;
  }
}
