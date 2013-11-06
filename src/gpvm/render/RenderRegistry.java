package gpvm.render;

import gpvm.util.IntMap;

/**
 *
 * @author russell
 */
public final class RenderRegistry {
  public final class ReadOnlyRenderRegistry {
    public RendererEntry getEntry(long tileid) {
      return RenderRegistry.this.getEntry(tileid);
    }
  }
  
  public static final class RendererEntry {
    public final Class<? extends TileRenderer> renderer;
    public final RenderInfo info;

    public RendererEntry(Class<? extends TileRenderer> renderer, RenderInfo info) {
      this.renderer = renderer;
      this.info = info;
    }
  }
  
  public RendererEntry getEntry(long tileid) {
    return data.getEntry(tileid);
  }
  
  public void addEntry(RendererEntry entry, long tileid) {
    data.insert(tileid, entry);
  }
  
  private IntMap<RendererEntry> data;
}
