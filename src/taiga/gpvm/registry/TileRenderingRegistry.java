package taiga.gpvm.registry;

import taiga.gpvm.HardcodedValues;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.io.DataFileManager;
import taiga.code.util.DataNode;
import taiga.code.registration.MissingObjectException;
import taiga.code.registration.NamedObject;
import taiga.gpvm.map.Tile;
import taiga.gpvm.render.TileRenderer;

/**
 * A {@link Registry} of information for rendering {@link Tile}s.  In additional
 * to the index provided by the super class an index by the {@link TileEntry} 
 * for each {@link TileRenderingEntry} will also be maintained.  The
 * {@link Thread#getContextClassLoader() } will be used to load {@link Class}es
 * for the {@link TileRenderingEntry}.
 * 
 * @author russell
 */
public class TileRenderingRegistry extends Registry<TileRenderingEntry>{
  /**
   * Creates a new empty {@link RenderingRegistry}.
   */
  public TileRenderingRegistry() {
    super(HardcodedValues.TILE_RENDERING_REGISTRY_NAME);
    
    tileindex = new HashMap<>();
  }

  @Override
  public void addEntry(TileRenderingEntry ent) {
    super.addEntry(ent);
    
    tileindex.put(ent.tile, ent);
  }
  
  /**
   * Provides a way to access {@link TileRenderingEntry} indexed by {@link TileEntry}
   * instead of by id.
   * 
   * @param tile The {@link TileEntry} for the desired {@link TileRenderingEntry}
   * @return The desired {@link TileRenderingEntry} or null if none are found.
   */
  public TileRenderingEntry getEntry(TileEntry tile) {
    return tileindex.get(tile);
  }
  
  /**
   * Loads a data file of {@link TileRenderingEntry}s into this {@link TileRenderingRegistry}.
   * 
   * @param in The name of the {@link File} to read in.
   * @param namespace The name space that the {@link TileRenderingEntry} should be added to
   *  to agree with the {@link TileRegistry} and prevent name conflicts.
   * 
   * @throws Exception Thrown if there is a problem loading the
   *  {@link Class} for a {@link TileRenderer}, or the {@link TileRegistry} cannot
   *  be found.
   */
  public void loadRenderingRegistryData(String in, String namespace) throws Exception {
    DataFileManager dfio = (DataFileManager) getObject(DataFileManager.DATAFILEMANAGER_NAME);
    
    if(dfio == null) {
      throw new MissingObjectException();
    }
    
    DataNode data = dfio.readFile(in);
    
    loadRenderingRegistryData(data, namespace);
    
    log.log(Level.INFO, LOADED_FILE, in);
  }
  
  /**
   * Loads a data file of {@link TileRenderingEntry}s into this {@link TileRenderingRegistry}.
   * 
   * @param in The {@link File} to read in.
   * @param namespace The namespace that the {@link TileRenderingEntry} should be added to
   *  to agree with the {@link TileRegistry} and prevent name conflicts.
   * 
   * @throws Exception Thrown if there is a problem loading the
   *  {@link Class} for a {@link TileRenderer}, or the {@link TileRegistry} cannot
   *  be found.
   */
  public void loadRenderingRegistryData(URI in, String namespace) throws Exception {
    DataFileManager dfio = (DataFileManager) getObject(DataFileManager.DATAFILEMANAGER_NAME);
    
    if(dfio == null) {
      throw new MissingObjectException();
    }
    
    DataNode data = dfio.readFile(in);
    
    loadRenderingRegistryData(data, namespace);
    
    log.log(Level.INFO, LOADED_FILE, in);
  }
  
  /**
   * Loads a {@link DataNode} of {@link TileRenderingEntry}s into this {@link TileRenderingRegistry}.
   * 
   * @param data The {@link DataNode} with the rendering information.
   * @param namespace The name space that the {@link TileRenderingEntry} should be added to
   *  to agree with the {@link TileRegistry} and prevent name conflicts.
   * 
   * @throws Exception Thrown if there is a problem loading the
   *  {@link Class} for a {@link TileRenderer}, or the {@link TileRegistry} cannot
   *  be found.
   */
  public void loadRenderingRegistryData(DataNode data, String namespace) throws Exception {
    TileRegistry tiles = getObject(HardcodedValues.TILE_REGISTRY_NAME);
    
    if(tiles == null)
      throw new MissingObjectException(HardcodedValues.TILE_REGISTRY_NAME);
    
    for(NamedObject val : data) {
      if(!(val instanceof DataNode)) continue;
      DataNode cur = (DataNode) val;
      
      TileEntry tile = tiles.getEntry(namespace + HardcodedValues.NAMESPACE_SEPERATOR + cur.name);
      if(tile == null)
        tile = tiles.getEntry(cur.name);
      if(tile == null) {
        log.log(Level.WARNING, NO_TILE_ENTRY, new Object[]{cur.name, namespace});
        continue;
      }
      
      
      TileRenderingEntry entry = new TileRenderingEntry(tile, cur);
      
      addEntry(entry);
    }
  }
  
  private Map<TileEntry, TileRenderingEntry> tileindex;
  
  private static final String locprefix = TileRenderingRegistry.class.getName().toLowerCase();
  
  private static final String LOADED_FILE = locprefix + ".loaded_file";
  private static final String NO_TILE_ENTRY = locprefix + ".no_tile_entry";
  
  private static final Logger log = Logger.getLogger(locprefix, 
    System.getProperty("taiga.code.logging.text"));
}
