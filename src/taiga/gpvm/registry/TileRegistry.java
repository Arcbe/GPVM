/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

import taiga.gpvm.HardcodedValues;
import static taiga.gpvm.HardcodedValues.CANON_NAME_FIELD;
import static taiga.gpvm.HardcodedValues.OPAQUE_FIELD;
import static taiga.gpvm.HardcodedValues.SOLID_FIELD;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.io.DataFileManager;
import taiga.code.io.DataNode;
import taiga.code.registration.RegisteredObject;

/**
 *
 * @author russell
 */
public class TileRegistry extends NetworkRegistry<TileEntry>{

  public TileRegistry() {
    super(HardcodedValues.TILE_REGISTRY_NAME);
  }
  
  public void loadFile(File in, String modname) throws IOException {
    DataFileManager dfio = (DataFileManager) getObject(DataFileManager.DATAFILEMANAGER_NAME);
    if(dfio == null) {
      log.log(Level.WARNING, NO_DFIO);
      return;
    }
    
    DataNode data = dfio.readFile(in);
    
    for(RegisteredObject val : data) {
      if(!(val instanceof DataNode)) continue;
      DataNode cur = (DataNode) val;

      if(!(data.data instanceof DataNode)) {
        log.log(Level.WARNING, INVALID_TILE_DATA, new Object[]{val, in});
        continue;
      }

      String canonname = null;
      boolean opaque = true;
      boolean solid = true;
      
      for(RegisteredObject obj : (RegisteredObject)data.data) {
        if(!(obj instanceof DataNode)) continue;
        DataNode field = (DataNode) obj;
        
        switch(field.name) {
          //Tile name field
          case CANON_NAME_FIELD:
            if(!(field.data instanceof String)) {
              log.log(Level.WARNING, INVALID_FIELD_VALUE, new Object[]{CANON_NAME_FIELD, field.data});
              continue;
            }
            
            canonname = (String) field.data;
            break;
            
          //Tile solidity field
          case SOLID_FIELD:
            if(!(field.data instanceof Boolean)) {
              log.log(Level.WARNING, INVALID_FIELD_VALUE, new Object[]{SOLID_FIELD, field.data});
              continue;
            }
              
            solid = (Boolean)field.data;
            break;
            
          //Tile opacity field
          case OPAQUE_FIELD:
            if(!(field.data instanceof Boolean)) {
              log.log(Level.WARNING, INVALID_FIELD_VALUE, new Object[]{OPAQUE_FIELD, field.data});
              continue;
            }
            
            opaque = (Boolean) field.data;
            break;
        }
      }
      
      //All tiles must have a name.
      if(canonname == null) {
        log.log(Level.WARNING, NO_TILE_NAME, cur);
        continue;
      }
      
      TileEntry entry = new TileEntry(modname, name, opaque, solid);
      addEntry(entry);
    }
  }
  
  private static final String locprefix = TileRegistry.class.getName().toLowerCase();
  
  private static final String NO_DFIO = locprefix + ".no_datafile_manager";
  private static final String INVALID_TILE_DATA = locprefix + ".invalid_tile_data";
  private static final String INVALID_FIELD_VALUE = locprefix + ".invalid_field_value";
  private static final String NO_TILE_NAME = locprefix + ".no_tile_name";
  
  private static final Logger log = Logger.getLogger(locprefix, System.getProperty("taiga.code.logging.text"));
}
