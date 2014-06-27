package taiga.gpvm.registry;

import taiga.gpvm.HardcodedValues;
import static taiga.gpvm.HardcodedValues.NAMESPACE_SEPERATOR;
import static taiga.gpvm.HardcodedValues.RENDERER_CLASS_FIELD;
import static taiga.gpvm.HardcodedValues.RENDERING_INFO_FIELD;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.io.DataFileManager;
import taiga.code.io.DataNode;
import taiga.code.registration.MissingObjectException;
import taiga.code.registration.RegisteredObject;
import taiga.gpvm.map.Tile;
import taiga.gpvm.render.Renderer;

/**
 * A {@link Registry} of information for rendering {@link Tile}s.  In additional
 * to the index provided by the super class an index by the {@link
 * TileEntry} for each {@link TileRenderingEntry} will also be maintained.
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
   * @param namespace The namespace that the {@link TileRenderingEntry} should be added to
   *  to agree with the {@link TileRegistry} and prevent name conflicts.
   * 
   * @throws IOException Thrown if the file could not be read successfully.
   * @throws ReflectiveOperationException Thrown if there is a problem loading the
   *  {@link Class} for a {@link Renderer}.
   * @throws MissingObjectException If there is no {@link DataFileManager} in the
   *  registration tree or it could not be found.
   */
  public void loadRenderingRegistryData(String in, String namespace) throws IOException,  ReflectiveOperationException, MissingObjectException {
    loadRenderingRegistryData(in, namespace, getClass().getClassLoader());
  }
  
  /**
   * Loads a data file of {@link TileRenderingEntry}s into this {@link TileRenderingRegistry}.
   * 
   * @param in The {@link File} to read in.
   * @param namespace The namespace that the {@link TileRenderingEntry} should be added to
   *  to agree with the {@link TileRegistry} and prevent name conflicts.
   * 
   * @throws IOException Thrown if the file could not be read successfully.
   * @throws ReflectiveOperationException Thrown if there is a problem loading the
   *  {@link Class} for a {@link Renderer}.
   * @throws MissingObjectException If there is no {@link DataFileManager} in the
   *  registration tree or it could not be found.
   */
  public void loadRenderingRegistryData(File in, String namespace) throws IOException,  ReflectiveOperationException, MissingObjectException {
    loadRenderingRegistryData(in, namespace, getClass().getClassLoader());
  }
  
  /**
   * Loads a {@link DataNode} of {@link TileRenderingEntry}s into this {@link TileRenderingRegistry}.
   * 
   * @param data The {@link DataNode} with the rendering information.
   * @param namespace The namespace that the {@link TileRenderingEntry} should be added to
   *  to agree with the {@link TileRegistry} and prevent name conflicts.
   * 
   * @throws ReflectiveOperationException Thrown if there is a problem loading the
   *  {@link Class} for a {@link Renderer}.
   * @throws MissingObjectException If there is no {@link DataFileManager} in the
   *  registration tree or it could not be found.
   */
  public void loadRenderingRegistryData(DataNode data, String namespace) throws ReflectiveOperationException, MissingObjectException {
    loadRenderingRegistryData(data, namespace, getClass().getClassLoader());
  }
  
  /**
   * Loads a data file of {@link TileRenderingEntry}s into this {@link TileRenderingRegistry}.
   * 
   * @param in The name of the {@link File} to read in.
   * @param namespace The namespace that the {@link TileRenderingEntry} should be added to
   *  to agree with the {@link TileRegistry} and prevent name conflicts.
   * @param loader The {@link ClassLoader} to load the {@link Renderer} {@link Class} from.
   * 
   * @throws IOException Thrown if the file could not be read successfully.
   * @throws ReflectiveOperationException Thrown if there is a problem loading the
   *  {@link Class} for a {@link Renderer}.
   * @throws MissingObjectException If there is no {@link DataFileManager} in the
   *  registration tree or it could not be found.
   */
  public void loadRenderingRegistryData(String in, String namespace, ClassLoader loader) throws IOException,  ReflectiveOperationException, MissingObjectException {
    DataFileManager dfio = (DataFileManager) getObject(DataFileManager.DATAFILEMANAGER_NAME);
    
    if(dfio == null) {
      throw new MissingObjectException();
    }
    
    DataNode data = dfio.readFile(in);
    
    loadRenderingRegistryData(data, namespace, loader);
    
    log.log(Level.INFO, LOADED_FILE, in);
  }
  
  /**
   * Loads a data file of {@link TileRenderingEntry}s into this {@link TileRenderingRegistry}.
   * 
   * @param in The {@link File} to read in.
   * @param namespace The namespace that the {@link TileRenderingEntry} should be added to
   *  to agree with the {@link TileRegistry} and prevent name conflicts.
   * @param loader The {@link ClassLoader} to load the {@link Renderer} from.
   * 
   * @throws IOException Thrown if the file could not be read successfully.
   * @throws ReflectiveOperationException Thrown if there is a problem loading the
   *  {@link Class} for a {@link Renderer}.
   * @throws MissingObjectException If there is no {@link DataFileManager} in the
   *  registration tree or it could not be found.
   */
  public void loadRenderingRegistryData(File in, String namespace, ClassLoader loader) throws IOException,  ReflectiveOperationException, MissingObjectException {
    DataFileManager dfio = (DataFileManager) getObject(DataFileManager.DATAFILEMANAGER_NAME);
    
    if(dfio == null) {
      throw new MissingObjectException();
    }
    
    DataNode data = dfio.readFile(in);
    
    loadRenderingRegistryData(data, namespace, loader);
    
    log.log(Level.INFO, LOADED_FILE, in);
  }
  
  /**
   * Loads a {@link DataNode} of {@link TileRenderingEntry}s into this {@link TileRenderingRegistry}.
   * 
   * @param data The {@link DataNode} with the rendering information.
   * @param namespace The namespace that the {@link TileRenderingEntry} should be added to
   *  to agree with the {@link TileRegistry} and prevent name conflicts.
   * @param loader The {@link ClassLoader} to load the {@link Renderer} from.
   * 
   * @throws ReflectiveOperationException Thrown if there is a problem loading the
   *  {@link Class} for a {@link Renderer}.
   * @throws MissingObjectException If there is no {@link DataFileManager} in the
   *  registration tree or it could not be found.
   */
  public void loadRenderingRegistryData(DataNode data, String namespace, ClassLoader loader) throws ReflectiveOperationException, MissingObjectException {
    TileRegistry tiles = (TileRegistry) getObject(HardcodedValues.TILE_REGISTRY_NAME);
    
    if(tiles == null)
      throw new MissingObjectException();
    
    for(RegisteredObject val : data) {
      if(!(val instanceof DataNode)) continue;
      DataNode cur = (DataNode) val;

      if(cur.data != null) {
        log.log(Level.WARNING, INVALID_RENDERING_ENTRY, new Object[] {val});
        continue;
      }

      DataNode renddata = null;
      Class<? extends Renderer> rendclass = null;

      for(RegisteredObject obj : cur) {
        if(!(obj instanceof DataNode)) continue;
        DataNode dataval = (DataNode)obj;

        switch(dataval.name) {
          case RENDERER_CLASS_FIELD:
            if(dataval.data instanceof String) {
              rendclass = (Class<? extends Renderer>) loader.loadClass((String) dataval.data);
            } else {
              log.log(Level.WARNING, INVALID_RENDERING_ENTRY, new Object[]{val});
            }
            break;
          case RENDERING_INFO_FIELD:
            if(dataval.data == null) {
              renddata = dataval;
            } else {
              log.log(Level.WARNING, INVALID_RENDERING_ENTRY, new Object[] {val});
            }
            break;
        }
      }

      if(rendclass == null) continue;

      Renderer temp = rendclass.newInstance();
      Class<? extends RenderingInfo> infoclass = temp.getInfoClass();

      RenderingInfo info = null;
      if(infoclass != null) {
        Constructor<? extends RenderingInfo> con = infoclass.getConstructor(DataNode.class);
        info = con.newInstance(renddata);
      }

      TileRenderingEntry entry = new TileRenderingEntry(rendclass, info, tiles.getEntry(namespace + NAMESPACE_SEPERATOR + val.name));
      addEntry(entry);
    }
  }
  
  private Map<TileEntry, TileRenderingEntry> tileindex;
  
  private static final String locprefix = TileRenderingRegistry.class.getName().toLowerCase();
  
  private static final String INVALID_RENDERING_ENTRY = locprefix + ".invalid_rendering_entry";
  private static final String LOADED_FILE = locprefix + ".loaded_file";
  
  private static final Logger log = Logger.getLogger(locprefix, 
    System.getProperty("taiga.code.logging.text"));
}
