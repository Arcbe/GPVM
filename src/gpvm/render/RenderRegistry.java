package gpvm.render;

import java.util.HashMap;

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
    return data.get(tileid);
  }
  
  public void addEntry(RendererEntry entry, long tileid) {
    data.put(tileid, entry);
  }
  
  private HashMap<Long, RendererEntry> data;
}
