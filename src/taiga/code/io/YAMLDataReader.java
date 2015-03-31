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

package taiga.code.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yaml.snakeyaml.Yaml;
import taiga.code.util.DataNode;

/**
 * A {@link DataFileReader} for .yml data files.
 * @author russell
 */
public class YAMLDataReader extends DataFileReader {
  private static final long serialVersionUID = 1L;
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
  
  /**
   * Creates a new {@link YAMLDataReader} with the given name.
   * 
   * @param name The name for this {@link YAMLDataReader}.
   */
  public YAMLDataReader(String name) {
    super(name);
    
    yaml = new Yaml();
  }

  @Override
  @SuppressWarnings("unchecked")
  public DataNode readFile(URL file) throws IOException {
    log.log(Level.FINEST, "readFile({0})", file);
    Map<String, Object> raw;
    
    try (InputStream in = file.openStream()) {
      Object temp = yaml.load(in);
      if(temp instanceof Map) 
        raw = (Map) temp;
      else {
        log.log(Level.WARNING, "Invalid or empty data file {0}.", file);
        return null;
      }
    }
    
    DataNode output = parseNode(raw, DataFileManager.DATANODE_ROOT_NAME);
    log.log(Level.FINER, "Data file {0} read.", file);
    return output;
  }

  @Override
  public boolean canReadFile(URL file) {
    log.log(Level.FINEST, "canReadFile({0})", file);
    
    try (InputStream in = file.openStream()) {
      yaml.load(in);
    } catch(Exception ex) {
      return false;
    }
    
    return true;
  }
  
  private DataNode parseNode(Map<String, Object> data, String name) {
    DataNode result = new DataNode(name);
    
    data.entrySet().stream().forEach((entry) -> {
      if(entry.getValue() instanceof Map) {
        
        assert entry.getValue() instanceof Map;
        @SuppressWarnings("unchecked")
        DataNode subnode = parseNode((Map<String, Object>) entry.getValue(), entry.getKey());
        
        //if this map is a set of attributes then set the data field of the parent.
        if(entry.getKey().equals(FIELD_NAME_ATTRIBUTES))
          result.data = subnode;
        
        //otherwise create a child for the parent.
        else {
          synchronized(result) {
            result.addChild(subnode);
          }
        }
      } else {
        //otherwise just put the data into a node and add it
        DataNode subnode = new DataNode(entry.getKey());
        subnode.data = entry.getValue();
        
        synchronized(result) {
          result.addChild(subnode);
        }
      }
    });
    
    return result;
  }
  
  private final Yaml yaml;
  
  private static final Logger log = Logger.getLogger(YAMLDataReader.class.getName(),
    System.getProperty("taiga.code.logging.text"));
}
