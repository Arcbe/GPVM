/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.yaml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yaml.snakeyaml.Yaml;
import taiga.code.io.DataFileManager;
import taiga.code.io.DataFileReader;
import taiga.code.util.DataNode;

/**
 * A {@link DataFileReader} for .yml data files.
 * @author russell
 */
public class YAMLDataReader extends DataFileReader {
  /**
   * Default name for a {@link YAMLDataReader}.
   */
  public static final String YAMLREADER_NAME = "yaml";
  
  /**
   * A special name used to define attributes of an entry in a yml file.
   * An entry with this name will be used to populate a {@link Map} of attributes
   * instead of becoming a {@link DataNode}.
   */
  public static final String FIELD_NAME_ATTRIBUTES = "attributes";

  /**
   * Creates a new {@link YAMLDataReader}.
   */
  public YAMLDataReader() {
    super(YAMLREADER_NAME);
    
    yaml = new Yaml();
  }

  @Override
  public DataNode readFile(File file) throws IOException {
    Map<String, Object> raw;
    
    try (Reader in = new FileReader(file)) {
      raw = (Map<String, Object>) yaml.load(in);
    }
    
    if(raw == null) {
      log.log(Level.WARNING, NO_DATA, file);
      return null;
    }
    
    return parseNode(raw, DataFileManager.DATANODE_ROOT_NAME);
  }

  @Override
  public boolean canReadFile(File file) {
    try (Reader in = new FileReader(file)) {
      yaml.load(in);
    } catch(Exception ex) {
      return false;
    }
    
    return true;
  }
  
  private DataNode parseNode(Map<String, Object> data, String name) {
    DataNode result = new DataNode(name);
    
    for(Map.Entry<String, Object> entry : data.entrySet()) {
      //check to see if this needs to be converted to another datanode
      if(entry.getValue() instanceof Map) {
        DataNode subnode = parseNode((Map<String, Object>) entry.getValue(), entry.getKey());
        
        //if this map is a set of attributes then set the data field of the parent.
        if(entry.getKey().equals(FIELD_NAME_ATTRIBUTES))
          result.data = subnode;
        //otherwise create a child for the parent.
        else {
          result.addChild(subnode);
        }
      } else {
        //otherwise just put the data into a node and add it
        DataNode subnode = new DataNode(entry.getKey());
        subnode.data = entry.getValue();
        
        result.addChild(subnode);
      }
    }
    
    return result;
  }
  
  private final Yaml yaml;
  
  private static final String NO_DATA = YAMLDataReader.class.getName().toLowerCase() + ".no_data";
  
  private static final Logger log = Logger.getLogger(YAMLDataReader.class.getName(),
    System.getProperty("taiga.code.logging.text"));
}
