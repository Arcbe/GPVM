package gpvm.render;

import gpvm.util.IntMap;

/**
 *
 * @author russell
 */
public class RenderRegistry {
  public static class RendererEntry {
    public final Class<? extends TileRenderer> renderer;
    public final TileInfo info;

    public RendererEntry(Class<? extends TileRenderer> renderer, TileInfo info) {
      this.renderer = renderer;
      this.info = info;
    }
  }
  
  public RendererEntry getEntry(int tileid) {
    return data.getEntry(tileid);
  }
  
  public void addEntry(RendererEntry entry, int tileid) {
    data.insert(tileid, entry);
  }
  
  private IntMap<RendererEntry> data;
}
