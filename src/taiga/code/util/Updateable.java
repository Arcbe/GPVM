/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.code.util;

/**
 * A interface for {@link Object} that require a periodic update.
 * @author russell
 */
public interface Updateable {
  /**
   * Called periodically to allow the {@link Object} to update itself.
   */
  public void update();
}
