/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm;

/**
 * Contains all registries used by the current game.  Along with convenience
 * methods for adding entries from files.
 * 
 * @author russell
 */
public class Registrar {
  public Registrar getInstance() {
    return instance;
  }
  
  private Registrar instance;
}
