package gpvm.render;

import java.util.HashMap;

/**
 * A registry containing {@link TileRenderer} and {@link RenderInfo} for
 * tile ids.
 * 
 * @author russell
 */
public final class RenderRegistry {
  /**
   * An instance of a {@link RenderRegistry} that only contains functions that
   * can read its state.
   */
  public final class ReadOnlyRenderRegistry {
    /**
     * @see RenderRegistry#getEntry(long) 
     * @param tileid
     * @return 
     */
    public RendererEntry getEntry(long tileid) {
      return RenderRegistry.this.getEntry(tileid);
    }
  }
  
  /**
   * A single entry in a {@link RenderRegistry}.
   */
  public static final class RendererEntry {
    //TODO: create a read version of this class.
    
    /**
     * The class of the {@link TileRenderer}used for these tiles.
     */
    public final Class<? extends TileRenderer> renderer;
    
    /**
     * Any information for the rendering of a {@link Tile} using this.
     */
    public final RenderInfo info;

    /**
     * Creates a new entry for the {@link RenderRegistry}.
     * 
     * @param renderer The class of the renderer for this entry.
     * @param info Any addition rendering information for this entry.
     */
    public RendererEntry(Class<? extends TileRenderer> renderer, RenderInfo info) {
      this.renderer = renderer;
      this.info = info;
    }
  }
  
  /**
   * Gets the entry associated with the given TileID.  This id must be the 
   * base id for the tile without any metadata.
   * 
   * @param tileid The id to get the entry for.
   * @return The requested entry r null if there is no associated entry.
   */
  public RendererEntry getEntry(long tileid) {
    return data.get(tileid);
  }
  
  /**
   * Adds an entry to the registry.
   * 
   * @param entry The entry to add.
   * @param tileid The tile id that it is associated with.
   */
  public void addEntry(RendererEntry entry, long tileid) {
    data.put(tileid, entry);
  }
  
  private HashMap<Long, RendererEntry> data;
}
