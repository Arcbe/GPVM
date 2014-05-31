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
  /**
   * The name of the {@link RegistryEntry}.
   */
  public final String name;
  /**
   * The id of the {@link RegistryEntry} assigned by the {@link Registry}
   */
  protected int id;

  /**
   * Creates a new {@link RegistryEntry} with the given name.
   * 
   * @param name The name for the nw {@link RegistryEntry}.
   */
  public RegistryEntry(String name) {
    this.name = name;
    
    id = -1;
  }
  
  /**
   * Returns the id assigned to this {@link RegistryEntry}.  If it has not been
   * added to a registry, or ids have not yet been assigned then this will return -1.
   * @return 
   */
  public int getID() {
    return id;
  }
}
