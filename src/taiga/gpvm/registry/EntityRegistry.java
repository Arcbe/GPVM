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

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.io.DataFileManager;
import taiga.code.util.DataNode;
import taiga.code.registration.NamedObject;
import taiga.gpvm.HardcodedValues;

public class EntityRegistry extends NetworkRegistry<EntityType> {

  public EntityRegistry() {
    super(HardcodedValues.NAME_ENTITY_REGISTRY);
  }
  
  /**
   * Loads a {@link File} of {@link EntityType}s into this {@link EntityRegistry}.
   * 
   * @param in The name of the {@link File} containing the tile definitions.
   * @param modname The name of the mod that this data file is loaded by.
   * @throws IOException Thrown if the data file cannot be read.
   */
  public void loadFile(String in, String modname) throws IOException {
    DataFileManager dfio = (DataFileManager) getObject(DataFileManager.DATAFILEMANAGER_NAME);
    if(dfio == null) {
      log.log(Level.WARNING, NO_DFIO);
      return;
    }
    
    DataNode data = dfio.readFile(in);
    loadData(data, modname);
    
    log.log(Level.INFO, LOADED_FILE, in);
  }
  
  /**
   * Loads a {@link File} of {@link EntityType}s into this {@link EntityRegistry}.
   * 
   * @param in The {@link File} containing the tile definitions.
   * @param modname The name of the mod that this data file is loaded by.
   * @throws IOException Thrown if the data file cannot be read.
   */
  public void loadFile(URL in, String modname) throws IOException {
    DataFileManager dfio = (DataFileManager) getObject(DataFileManager.DATAFILEMANAGER_NAME);
    if(dfio == null) {
      log.log(Level.WARNING, NO_DFIO);
      return;
    }
    
    DataNode data = dfio.readFile(in);
    loadData(data, modname);
    
    log.log(Level.INFO, LOADED_FILE, in);
  }
  
  /**
   * Loads a {@link DataNode} of {@link EntityType}s into this {@link EntityRegistry}.
   * 
   * @param data The {@link DataNode} containing the tile definitions.
   * @param modname The name of the mod that this data file is loaded by.
   */
  public void loadData(DataNode data, String modname){
    
    if(data == null) return;
    
    for(NamedObject val : data) {
      if(!(val instanceof DataNode)) continue;
      DataNode cur = (DataNode) val;
      
      EntityType entry = new EntityType(modname, cur.name);
      addEntry(entry);
    }
  }

  private static final String locprefix = EntityRegistry.class.getName().toLowerCase();
  
  private static final String NO_DFIO = locprefix + ".no_dfio";
  private static final String LOADED_FILE = locprefix + ".loaded_file";

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
