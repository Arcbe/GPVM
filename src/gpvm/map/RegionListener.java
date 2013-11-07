/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.map;

/**
 * A listener for events that occur to a {@link Region} in the {@link GameMap}
 * 
 * @author russell
 */
public interface RegionListener {
  /**
   * Called when the {@link Region} that this listener is attached to
   * is being unloaded from the map.
   * 
   * @param reg A reference to the {@link Region} being unloaded.
   */
  public void regionUnloading(Region reg);
  
  /**
   * Called when a region has been updated.  This will not be called for
   * individual {@link Tile} updates, but for changes to the internal data of
   * the {@link Region}.
   * 
   * @param reg The {@link Region} that was updated.
   */
  public void regionUpdated(Region reg);
}
