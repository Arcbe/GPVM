/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.entity;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import taiga.code.registration.RegisteredObject;
import taiga.code.util.Updateable;
import taiga.gpvm.HardcodedValues;

public class EntityManager extends RegisteredObject implements Updateable {

  public EntityManager() {
    super(HardcodedValues.ENTITY_MANAGER_NAME);
    
    index = new TreeMap<>();
  }

  @Override
  public void Update() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  private final Map<Integer, Entity> index;

  private static final String locprefix = EntityManager.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
