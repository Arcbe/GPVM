/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.io;

import gpvm.util.Settings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

/**
 * A {@link DataFileLoader} for YAML files.
 * 
 * @author russell
 */
public class YAMLLoader implements DataFileLoader{

  /**
   * Creates a new loader for YAML files.  A loader is required for each
   * {@link Thread} that wants to load YAML files.
   */
  public YAMLLoader() {
    parser = new Yaml(new SafeConstructor());
  }
  
  @Override
  public DataNode loadFile(File file) {
    Map<String, Object> data = null;
    try(InputStream in = new FileInputStream(file)) {
     data = (Map<String, Object>) parser.load(in);
    } catch (IOException ex) {
      Logger.getLogger(YAMLLoader.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    //if this an empty file then return an empty DataNode and a warning
    if(data == null) {
      String msg = String.format(Settings.getLocalString("warn_empty_data_file"), file.toString());
      Logger.getLogger(YAMLLoader.class.getCanonicalName()).log(Level.WARNING, msg);
      return new DataNode(null);
    }
    
    return createNode(data);
  }

  @Override
  public boolean canLoad(File file) {
    try (FileInputStream in = new FileInputStream(file)) {
      parser.load(in);
    } catch(Exception ex) {
      return false;
    }
    
    return true;
  }
  
  private Yaml parser;
  
  private DataNode createNode(Map<String, Object> data) {
    //replace any maps with DataNodes to create the data tree
    for(Map.Entry<String, Object> entry : data.entrySet()) {
      if(entry.getValue() instanceof Map) {
        DataNode node = createNode((Map<String, Object>) entry.getValue());
        entry.setValue(node);
      }
    }
    
    return new DataNode(data);
  }
}
