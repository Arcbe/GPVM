/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.io.DataFileManager;
import taiga.code.util.DataNode;
import taiga.code.registration.MissingObjectException;
import taiga.code.registration.RegisteredObject;
import taiga.gpvm.HardcodedValues;
import static taiga.gpvm.HardcodedValues.NAMESPACE_SEPERATOR;
import static taiga.gpvm.HardcodedValues.RENDERER_CLASS_FIELD;
import static taiga.gpvm.HardcodedValues.RENDERING_INFO_FIELD;
import taiga.gpvm.render.EntityRenderer;
import taiga.gpvm.render.TileRenderer;

public class EntityRenderingRegistry extends Registry<EntityRenderingEntry> {

  public EntityRenderingRegistry() {
    super(HardcodedValues.ENTITY_RENDERING_REGISTRY_NAME);
    
    entindex = new HashMap<>();
  }

  @Override
  public void addEntry(EntityRenderingEntry ent) {
    super.addEntry(ent);
    
    entindex.put(ent.entity, ent);
  }
  
  /**
   * Provides a way to access {@link EntityRenderingEntry} indexed by {@link EntityEntry}
   * instead of by id.
   * 
   * @param tile The {@link EntityEntry} for the desired {@link EntityRenderingEntry}
   * @return The desired {@link EntityRenderingEntry} or null if none are found.
   */
  public EntityRenderingEntry getEntry(EntityEntry tile) {
    return entindex.get(tile);
  }
  
  /**
   * Loads a data file of {@link EntityRenderingEntry}s into this {@link EntityRenderingRegistry}.
   * 
   * @param in The name of the {@link File} to read in.
   * @param namespace The namespace that the {@link EntityRenderingEntry} should be added to
   *  to agree with the {@link EntityRegistry} and prevent name conflicts.
   * 
   * @throws IOException Thrown if the file could not be read successfully.
   * @throws ReflectiveOperationException Thrown if there is a problem loading the
   *  {@link Class} for a {@link EntityRenderer}.
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
   * @param namespace The namespace that the {@link EntityRenderingEntry} should be added to
   *  to agree with the {@link EntityRegistry} and prevent name conflicts.
   * 
   * @throws IOException Thrown if the file could not be read successfully.
   * @throws ReflectiveOperationException Thrown if there is a problem loading the
   *  {@link Class} for a {@link EntityRenderer}.
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
   * @param namespace The namespace that the {@link EntityRenderingEntry} should be added to
   *  to agree with the {@link EntityRegistry} and prevent name conflicts.
   * 
   * @throws ReflectiveOperationException Thrown if there is a problem loading the
   *  {@link Class} for a {@link EntityRenderer}.
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
   * @param namespace The namespace that the {@link EntityRenderingEntry} should be added to
   *  to agree with the {@link EntityRegistry} and prevent name conflicts.
   * @param loader The {@link ClassLoader} to load the {@link TileRenderer} {@link Class} from.
   * 
   * @throws IOException Thrown if the file could not be read successfully.
   * @throws ReflectiveOperationException Thrown if there is a problem loading the
   *  {@link Class} for a {@link EntityRenderer}.
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
   * @param namespace The namespace that the {@link EntityRenderingEntry} should be added to
   *  to agree with the {@link EntityRegistry} and prevent name conflicts.
   * @param loader The {@link ClassLoader} to load the {@link TileRenderer} from.
   * 
   * @throws IOException Thrown if the file could not be read successfully.
   * @throws ReflectiveOperationException Thrown if there is a problem loading the
   *  {@link Class} for a {@link EntityRenderer}.
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
   * @param namespace The namespace that the {@link EntityRenderingEntry} should be added to
   *  to agree with the {@link TileRegistry} and prevent name conflicts.
   * @param loader The {@link ClassLoader} to load the {@link TileRenderer} from.
   * 
   * @throws ReflectiveOperationException Thrown if there is a problem loading the
   *  {@link Class} for a {@link EntityRenderer}.
   * @throws MissingObjectException If there is no {@link DataFileManager} in the
   *  registration tree or it could not be found.
   */
  public void loadRenderingRegistryData(DataNode data, String namespace, ClassLoader loader) throws ReflectiveOperationException, MissingObjectException {
    EntityRegistry entities = getObject(HardcodedValues.ENTITY_REGISTRY_NAME);
    
    if(entities == null)
      throw new MissingObjectException();
    
    for(RegisteredObject val : data) {
      if(!(val instanceof DataNode)) continue;
      DataNode cur = (DataNode) val;

      if(cur.data != null) {
        log.log(Level.WARNING, INVALID_RENDERING_ENTRY, new Object[] {val});
        continue;
      }

      DataNode renddata = null;
      Class<? extends EntityRenderer> rendclass = null;

      for(RegisteredObject obj : cur) {
        if(!(obj instanceof DataNode)) continue;
        DataNode dataval = (DataNode)obj;

        switch(dataval.name) {
          case RENDERER_CLASS_FIELD:
            if(dataval.data instanceof String) {
              rendclass = (Class<? extends EntityRenderer>) loader.loadClass((String) dataval.data);
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

      EntityRenderer renderer = rendclass.newInstance();
      renderer.loadData(renddata);

      EntityRenderingEntry entry = new EntityRenderingEntry(renderer, entities.getEntry(namespace + NAMESPACE_SEPERATOR + val.name));
      addEntry(entry);
    }
  }
  
  private final Map<EntityEntry, EntityRenderingEntry> entindex;

  private static final String locprefix = EntityRenderingRegistry.class.getName().toLowerCase();
  
  private static final String LOADED_FILE = locprefix + ".loaded_file";
  private static final String INVALID_RENDERING_ENTRY = locprefix + ".invalid_rendering_entry";

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
