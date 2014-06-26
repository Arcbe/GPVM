/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

import java.util.logging.Logger;
import taiga.code.io.DataNode;

public class EntityEntry extends RegistryEntry {

  public final 
  
  public EntityEntry(String name) {
    super(name);
    
    
  }

  private static final String locprefix = EntityEntry.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
