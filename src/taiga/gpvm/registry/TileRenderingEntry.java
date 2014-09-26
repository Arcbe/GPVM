/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.text.TextLocalizer;
import taiga.code.util.DataNode;
import taiga.gpvm.render.EntityRenderer;
import taiga.gpvm.render.TileRenderer;

/**
 * An entry that contains all of the information needed to render a given type
 * of {@link Tile}.
 * 
 * @author russell
 */
public class TileRenderingEntry extends RegistryEntry {
  
  public static final String FIELD_NAME_INFO_CLASS = "INFO_CLASS";
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
   * @throws Exception Thrown if the {@link EntityRenderer} cannot be instantiated.
   */
  public TileRenderingEntry(TileEntry entry, DataNode data) {
    super(entry.name);
    tile = entry;
    
    try {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      String rend = data.getValueByName(FIELD_NAME_RENDERER);
      renderer = (Class<? extends TileRenderer>) loader.loadClass(rend);
    } catch (SecurityException | ClassNotFoundException ex) {
      throw new RuntimeException("Unable to create tile rendering entry.", ex);
    }
    
    Class<? extends RenderingInfo> ic = null;
    try {
      Field iclass = renderer.getField(FIELD_NAME_INFO_CLASS);
      ic = (Class<? extends RenderingInfo>) iclass.get(null);
    } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
    }
    
    RenderingInfo i = null;
    try {
      i = ic.getConstructor(DataNode.class).newInstance(data);
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
    }
    
    info = i;
  }
  
  private static final String locprefix = TileRenderingEntry.class.getSimpleName().toLowerCase();
}
