/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.entity;

import java.util.logging.Logger;

public class EntityChange {
  
  public enum EntityChangeType {
    /**
     * A change in the direction an {@link Entity} is moving.  This requires
     * both the new direction of movement and the current location.
     */
    Movement,
    /**
     * Adds some amount of damage to the targeted {@link Entity}.  The
     * amount of damage can be negative, and will result in healing the {@link Entity}.
     */
    Damage
  }
  
  public final Entity target;
  public final EntityChangeType type;
  
  public final Object first;
  public final Object second;

  public EntityChange(Entity target, EntityChangeType type, Object first, Object second) {
    this.target = target;
    this.type = type;
    this.first = first;
    this.second = second;
  }
  
  

  private static final String locprefix = EntityChange.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
