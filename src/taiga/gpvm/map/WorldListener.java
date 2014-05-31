/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.map;

/**
 * Listens for the loading and unloading of {@link Region}s in a {@link World}.
 * 
 * @author russell
 */
public interface WorldListener {
  /**
   * Called when a {@link Region} is loaded.
   * 
   * @param reg The {@link Region} that was loaded.
   */
  public void regionLoaded(Region reg);
  /**
   * Called when a {@link Region} is unloaded.
   * 
   * @param reg The {@link Region} that was unloaded.
   */
  public void regionUnloaded(Region reg);
}
