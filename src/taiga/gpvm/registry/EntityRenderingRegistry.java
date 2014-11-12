/*
 * Copyright (C) 2014 Russell Smith.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package taiga.gpvm.registry;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.io.DataFileManager;
import taiga.code.util.DataNode;
import taiga.code.registration.MissingObjectException;
import taiga.code.registration.NamedObject;
import taiga.gpvm.HardcodedValues;
import static taiga.gpvm.HardcodedValues.NAMESPACE_SEPERATOR;
import taiga.gpvm.render.EntityRenderer;
import taiga.gpvm.render.TileRenderer;

public class EntityRenderingRegistry extends Registry<EntityRenderingEntry> {

  public EntityRenderingRegistry() {
    super(HardcodedValues.NAME_ENTITY_RENDERING_REGISTRY);
    
    entindex = new HashMap<>();
  }

  @Override
  public void addEntry(EntityRenderingEntry ent) {
    super.addEntry(ent);
    
    entindex.put(ent.entity, ent);
  }
  
  /**
   * Provides a way to access {@link EntityRenderingEntry} indexed by {@link EntityType}
   * instead of by id.
   * 
   * @param tile The {@link EntityType} for the desired {@link EntityRenderingEntry}
   * @return The desired {@link EntityRenderingEntry} or null if none are found.
   */
  public EntityRenderingEntry getEntry(EntityType tile) {
    return entindex.get(tile);
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
  public void loadRenderingRegistryData(String in, String namespace) throws IOException,  ReflectiveOperationException, MissingObjectException {
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
   * @param in The {@link URI} to read in.
   * @param namespace The namespace that the {@link EntityRenderingEntry} should be added to
   *  to agree with the {@link EntityRegistry} and prevent name conflicts.
   * 
   * @throws IOException Thrown if the file could not be read successfully.
   * @throws ReflectiveOperationException Thrown if there is a problem loading the
   *  {@link Class} for a {@link EntityRenderer}.
   * @throws MissingObjectException If there is no {@link DataFileManager} in the
   *  registration tree or it could not be found.
   */
  public void loadRenderingRegistryData(URL in, String namespace) throws IOException,  ReflectiveOperationException, MissingObjectException {
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
   * @param namespace The namespace that the {@link EntityRenderingEntry} should be added to
   *  to agree with the {@link TileRegistry} and prevent name conflicts.
   * 
   * @throws ReflectiveOperationException Thrown if there is a problem loading the
   *  {@link Class} for a {@link EntityRenderer}.
   * @throws MissingObjectException If there is no {@link DataFileManager} in the
   *  registration tree or it could not be found.
   */
  public void loadRenderingRegistryData(DataNode data, String namespace) throws ReflectiveOperationException, MissingObjectException {
    EntityRegistry entities = getObject(HardcodedValues.NAME_ENTITY_REGISTRY);
    
    if(entities == null)
      throw new MissingObjectException();
    
    for(NamedObject val : data) {
      if(!(val instanceof DataNode)) continue;
      DataNode cur = (DataNode) val;
      
      EntityType type = entities.getEntry(namespace + NAMESPACE_SEPERATOR + val.name);
      EntityRenderingEntry entry = new EntityRenderingEntry(type, cur);
      addEntry(entry);
    }
  }
  
  private final Map<EntityType, EntityRenderingEntry> entindex;

  private static final String locprefix = EntityRenderingRegistry.class.getName().toLowerCase();
  
  private static final String LOADED_FILE = locprefix + ".loaded_file";
  private static final String INVALID_RENDERING_ENTRY = locprefix + ".invalid_rendering_entry";

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
