/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

import java.util.logging.Logger;
import taiga.gpvm.HardcodedValues;

public class EntityRenderingRegistry extends Registry<EntityRenderingEntry> {

  public EntityRenderingRegistry(String name) {
    super(HardcodedValues.ENTITY_RENDERING_REGISTRY_NAME);
  }

  private static final String locprefix = EntityRenderingRegistry.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
