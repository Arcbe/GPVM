/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

import taiga.code.text.TextLocalizer;
import taiga.code.util.DataNode;
import taiga.gpvm.render.TileRenderer;

/**
 * An entry that contains all of the information needed to render a given type
 * of {@link Tile}.
 * 
 * @author russell
 */
public class TileRenderingEntry extends RegistryEntry {
  
  public static final String FIELD_NAME_RENDERER = "renderer";
  
  public static final String FIELD_NAME_RENDERING_INFO = "rendering-info";
  
  /**
   * The {@link Class} of the {@link TileRenderer} that will be used to render the
   * given {@link Tile}s.
   */
  public final Class<? extends TileRenderer> renderer;
  
  /**
   * Additional information used by the {@link TileRenderer}.  THis may be null if
   * no information is available.
   */
  public final RenderingInfo info;
  
  /**
   * The {@link TileEntry} for the tiles that will use this {@link TileRenderingEntry}
   * for rendering.
   */
  public final TileEntry tile;

  /**
   * Creates a new {@link TileRenderingEntry} with the given attributes.
   * 
   * @param rend The class of the {@link TileRenderer} for this {@link RenderingEntry}.
   * @param inf Any additional information for rendering, or null if there is none.
   * @param t The {@link TileEntry} that this {@link RenderingEntry} is for.
   */
  public TileRenderingEntry(Class<? extends TileRenderer> rend, RenderingInfo inf, TileEntry t) {
    super(t.name);
    
    renderer = rend;
    info = inf;
    tile = t;
  }
  
  /**
   * Creates a new {@link TileRenderingEntry} for the given {@link TileEntry}
   * and values from the given {@link DataNode}.
   * 
   * @param entry The {@link TileEntry} for this {@link TileRenderingEntry}.
   * @param data A {@link DataNode} that contains the values for this {@link TileRenderingEntry}.
   */
  public TileRenderingEntry(TileEntry entry, DataNode data) throws Exception {
    super(entry.name);
    tile = entry;
    
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    
    String rend = data.getValueByName(FIELD_NAME_RENDERER);
    try {
      renderer = (Class<? extends TileRenderer>) loader.loadClass(rend);
      
      TileRenderer temp = renderer.newInstance();
      info = temp.
        getInfoClass().
        getConstructor(DataNode.class).
        newInstance(data.getChild(FIELD_NAME_RENDERING_INFO));
      
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException ex) {
      throw new Exception(TextLocalizer.localize(BAD_ENTRY_DATA), ex);
    }
  }
  
  private static final String locprefix = TileRenderingEntry.class.getSimpleName().toLowerCase();
  
  private static final String BAD_ENTRY_DATA = locprefix + ".bad_entry_data";
}
