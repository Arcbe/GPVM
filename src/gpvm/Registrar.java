package gpvm;

import gpvm.map.TileDefinition;
import gpvm.map.TileRegistry;
import gpvm.render.RenderRegistry;
import gpvm.util.Settings;

/**
 * Contains all registries used by the current game.  Along with convenience
 * methods for adding entries from files.
 * 
 * @author russell
 */
public class Registrar {
  
  public static Registrar getInstance() {
    return instance;
  }
  
  public TileRegistry.ReadOnlyTileRegistry getTileRegistry() {
    return tiles.new ReadOnlyTileRegistry();
  }
  
  public RenderRegistry.ReadOnlyRenderRegistry getRenderRegistry() {
    return render.new ReadOnlyRenderRegistry();
  }
  
  public long addTileEntry(TileDefinition def) {
    //check to make sure that writing is allowed
    if(!ThreadingManager.getInstance().canWrite())
      throw new IllegalStateException(Settings.getLocalString("err_writing_not_allowed"));
    
    return tiles.addDefition(def);
  }
  
  public void addRenderingEntry(RenderRegistry.RendererEntry entry, int tileid) {
    //check to make sure that writing is allowed
    if(!ThreadingManager.getInstance().canWrite())
      throw new IllegalStateException(Settings.getLocalString("err_writing_not_allowed"));
    
    render.addEntry(entry, tileid);
  }
  
  private TileRegistry tiles;
  private RenderRegistry render;
  
  private Registrar() {
    tiles = new TileRegistry();
    render = new RenderRegistry();
  }
  
  private static Registrar instance = new Registrar();
}
