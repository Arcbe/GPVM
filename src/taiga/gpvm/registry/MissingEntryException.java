/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

/**
 * An {@link Exception} thrown when an entry is not present within a {@link Registry}.
 * 
 * @author russell
 */
class MissingEntryException extends RuntimeException {

  /**
   * Constructs a new instances using the given name of the missing {@link RegistryEntry}.
   * 
   * @param name The name of the missing {@link RegistryEntry}
   */
  public MissingEntryException(String name) {
    super(name);
  }
  
  private static final long serialVersionUID = 1;
}
