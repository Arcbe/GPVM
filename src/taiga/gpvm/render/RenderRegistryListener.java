package taiga.gpvm.render;

import gpvm.map.Tile;

/**
 * Listener for event within a {@link RenderRegistry}.
 * 
 * @author russell
 */
public interface RenderRegistryListener {
  /**
   * Called when the {@link RenderRegistry} is cleared of all entries.
   */
  public void registryCleared();
  
  /**
   * Called when an entry is added to the {@link RenderRegistry}.
   * 
   * @param tileid The id of the tile that this entry is assigned to.
   * @param renderer The class of the {@link TileRenderer} for the id.
   * @param info Additional information for the {@link TileRenderer} if available.
   *  Otherwise null.
   */
  public void entryAdded(long tileid, RenderRegistry.RendererEntry entry);
}
