/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.entity;

import java.util.logging.Logger;
import taiga.code.io.DataNode;

/**
 * Base class for {@link Entity} that includes a 
 * @author russell
 */
public abstract class StationaryEntity extends Entity {

  public StationaryEntity(long id, DataNode data) {
    super(id);
  }

  private static final String locprefix = StationaryEntity.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
