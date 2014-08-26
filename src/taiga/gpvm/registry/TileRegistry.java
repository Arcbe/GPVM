/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

import taiga.gpvm.HardcodedValues;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.io.DataFileManager;
import taiga.code.util.DataNode;
import taiga.code.registration.NamedObject;
import taiga.gpvm.map.Tile;

/**
 * A {@link Registry} containing data for {@link TileEntry}s that contain values
 * for game logic with {@link Tile}.
 * 
 * @author russell
 */
public class TileRegistry extends NetworkRegistry<TileEntry>{

  /**
   * Creates a new empty {@link TileRegistry}.
   */
  public TileRegistry() {
    super(HardcodedValues.TILE_REGISTRY_NAME);
  }
  
  /**
   * Loads a {@link File} of {@link TileEntry}s into this {@link TileRegistry}.
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
   * Loads a {@link URI} of {@link TileEntry}s into this {@link TileRegistry}.
   * 
   * @param in The {@link URI} containing the tile definitions.
   * @param modname The name of the mod that this data file is loaded by.
   * @throws IOException Thrown if the data file cannot be read.
   */
  public void loadFile(URI in, String modname) throws IOException {
    DataFileManager dfio = getObject(DataFileManager.DATAFILEMANAGER_NAME);
    if(dfio == null) {
      log.log(Level.WARNING, NO_DFIO);
      return;
    }
    
    DataNode data = dfio.readFile(in);
    loadData(data, modname);
    
    log.log(Level.INFO, LOADED_FILE, in);
  }
  
  /**
   * Loads a {@link DataNode} of {@link TileEntry}s into this {@link TileRegistry}.
   * 
   * @param data The {@link DataNode} containing the tile definitions.
   * @param modname The name of the mod that this data file is loaded by.
   */
  public void loadData(DataNode data, String modname){
    
    if(data == null) return;
    
    for(NamedObject val : data) {
      if(!(val instanceof DataNode)) continue;
      DataNode cur = (DataNode) val;
      
      TileEntry nent = new TileEntry(modname, cur.name, cur);
      
      addEntry(nent);
    }
  }
  
  private static final String locprefix = TileRegistry.class.getName().toLowerCase();
  
  private static final String LOADED_FILE = locprefix + ".loaded_file";
  private static final String NO_DFIO = locprefix + ".no_datafile_manager";
  
  private static final Logger log = Logger.getLogger(locprefix, System.getProperty("taiga.code.logging.text"));
}
