/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gpvm.registry;

/**
 *
 * @author russell
 */
public abstract class RegisteredObject {
  protected int id;
  
  public void assignID(int id) {
    this.id = id;
  }
  
  public int getID() {
    return id;
  }
  
  public abstract String getName();
}
