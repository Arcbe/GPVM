/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.io;

import gpvm.map.TileDefinition;
import gpvm.util.Settings;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author russell
 */
public class RegistryDataReader {
  public static final String CANON_NAME_FIELD = "canonicalname";
  public static final String METADATA_FIELD = "metadata";
  public static final String OPAQUE_FIELD = "opaque";
  public static final String NAMESPACE_SEPERATOR = ".";
  
  public void loadTileRegistryData(File in, String namespace) throws FileNotFoundException, InvalidDataFileException {
    DataNode data = DataLoader.loadFile(in);
    
    for(String val : data.getValues()) {
      if(!data.isType(val, DataNode.class)) {
        log.log(Level.WARNING, Settings.getLocalString("err_unknown_tile_data"), val);
      }
      
      DataNode cur = data.getValue(val);
      
      String canonname = namespace + NAMESPACE_SEPERATOR;
      if(cur.isType(CANON_NAME_FIELD, String.class)) canonname += cur.getValue(CANON_NAME_FIELD);
      else canonname += val; //use the tiles display name as canonical
      
      int metadata = 0;
      if(cur.isType(METADATA_FIELD, Integer.class)) metadata = cur.getValue(METADATA_FIELD);
      
      TileDefinition def = new TileDefinition(val, canonname, metadata, true)
    }
  }
  
  private static final Logger log = Logger.getLogger(RegistryDataReader.class.getName());
}
