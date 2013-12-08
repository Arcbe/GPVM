package gpvm;

import gpvm.editor.panels.RenderRegistryPanel;
import gpvm.editor.panels.TileRegistryPanel;
import gpvm.map.TileDefinition;
import gpvm.map.TileRegistry;
import gpvm.map.TileRegistryListener;
import gpvm.render.RenderRegistry;
import gpvm.render.RenderRegistryListener;
import gpvm.util.StringManager;

/**
 * Contains all registries used by the current game.  Along with convenience
 * methods for adding entries from files.
 * 
 * @author russell
 */
public class Registrar {
  
  /**
   * Returns the current instance of the Registrar.
   * @return The current registrar
   */
  public static Registrar getInstance() {
    return instance;
  }
  
  /**
   * Returns a read only copy of the {@link TileRegistry} used by the
   * registrar.
   * 
   * @return A read only copy of the {@link TileRegistry}
   */
  public TileRegistry.ReadOnlyTileRegistry getTileRegistry() {
    return tiles.new ReadOnlyTileRegistry();
  }
  
  /**
   * Returns a read only copy of the {@link RenderRegsitry} used by the
   * registrar.
   * 
   * @return A read only copy of the {@link RenderRegistry}
   */
  public RenderRegistry.ReadOnlyRenderRegistry getRenderRegistry() {
    return render.new ReadOnlyRenderRegistry();
  }
  
  /**
   * Adds an entry to the {@link TileRegistry} used by the registrar.
   * This method can only be called while the write lock from the {@link ThreadingManager}
   * is locked by the current thread.
   * 
   * @param def The {@link TileDefinition} to add to the registry.
   * @return The TileID assigned to the new entry.
   * @throws IllegalStateException If the write lock is not locked by the current
   *    thread.
   */
  public long addTileEntry(TileDefinition def) {
    //check to make sure that writing is allowed
    if(!ThreadingManager.getInstance().canWrite())
      throw new IllegalStateException(StringManager.getLocalString("err_writing_not_allowed"));
    
    return tiles.addDefition(def);
  }
  
  /**
   * Adds an entry to the {@link RenderRegistry} used by the registrar.
   * This method can only be called while the write lock from the {@link ThreadingManager}
   * is locked by the current thread.
   * 
   * @param entry The entry to add to the registry.
   * @param tileid The id for the tile to assign the entry.
   * @throws IllegalStateException If the write lock is not locked by the current
   *    thread.
   */
  public void addRenderingEntry(RenderRegistry.RendererEntry entry, long tileid) {
    //check to make sure that writing is allowed
    if(!ThreadingManager.getInstance().canWrite())
      throw new IllegalStateException(StringManager.getLocalString("err_writing_not_allowed"));
    
    render.addEntry(entry, tileid);
  }

  /**
   * Adds a listeners to the TileRegistry contained within this {@link Registrar}.
   * 
   * @param list The listener to add.
   * @see TileRegistry#addListener(gpvm.map.TileRegistryListener) 
   */
  public void addTileRegistryListener(TileRegistryListener list) {
    tiles.addListener(list);
  }

  /**
   * Adds a listener to the RenderRegistry contained within this {@link Registrar}.
   * 
   * @param list The listener to add.
   * @see RenderRegistry#addListener(gpvm.render.RenderRegistryListener) 
   */
  public void addRenderRegistryListener(RenderRegistryListener list) {
    render.addListener(list);
  }
  
  private TileRegistry tiles;
  private RenderRegistry render;
  
  private Registrar() {
    tiles = new TileRegistry();
    render = new RenderRegistry();
  }
  
  private static Registrar instance = new Registrar();
}
