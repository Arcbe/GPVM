/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.modding;

import gpvm.io.DataLoader;
import gpvm.io.DataNode;
import gpvm.map.MapGenerator;
import gpvm.util.Settings;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author russell
 */
public class Mod {
  public static final String NAME_FIELD = "name";
  public static final String DEP_FIELD = "dependency";
  public static final String GENERATOR_FIELD = "map-generator";
  
  public Mod(String name, Class<? extends MapGenerator> generator) {
    
  }
  
  public Mod(String name, String ... dep) {
    
  }
  
  /**
   * Creates a new mod using the data file from the given {@link Path}.
   * The data file must be a modinfo file optionally with some extension that
   * contains at least a name, and either a dependency list or a map-generator
   * element but not both.
   * 
   * @param path The {@link Path} for the data file to load the mod from.
   * @return The new mod, or null if path was not a valid modinfo file.
   */
  public static Mod createMod(Path path) throws FileNotFoundException, MalformedURLException {
    //first see if we can load the file.
    DataNode root = DataLoader.loadFile(path.toFile());
    if(root == null) {
      String msg = String.format(Settings.getLocalString("warn_invalid_mod"), path);
      Logger.getLogger(Mod.class.getCanonicalName()).log(Level.WARNING, msg);
      return null;
    }
    
    //now check to see if the data has the required fields
    if(!root.contains(NAME_FIELD)) {
      String msg = String.format(Settings.getLocalString("warn_mod_missing_name"), path);
      Logger.getLogger(Mod.class.getCanonicalName()).log(Level.WARNING, msg);
      return null;
    } else if(!(root.contains(DEP_FIELD) ^ root.contains(GENERATOR_FIELD))) {
      String msg = String.format(Settings.getLocalString("warn_mod_missing_dep"), path);
      Logger.getLogger(Mod.class.getCanonicalName()).log(Level.WARNING, msg);
      return null;
    }
    
    //check to see if the fields are the correct type.
    Class<? extends MapGenerator> gen = null;
    URLClassLoader loader = URLClassLoader.newInstance(new URL[]{path.toUri().toURL()});
    //check the name field
    if(!root.valueType(NAME_FIELD).equals(String.class)) {
      String msg = String.format(Settings.getLocalString("warn_mod_name_string"), path);
      Logger.getLogger(Mod.class.getCanonicalName()).log(Level.WARNING, msg);
      return null;
    //if there is a dependency field check that
    } else if(root.contains(DEP_FIELD)) {
      Class<?> dep = root.valueType(DEP_FIELD);
      if(!dep.equals(String.class) && !Array.class.isAssignableFrom(dep)) {
        String msg = String.format(Settings.getLocalString("warn_mod_dep_string"), path);
        Logger.getLogger(Mod.class.getCanonicalName()).log(Level.WARNING, msg);
        return null;
      }
    //otherwise check the generator field.
    } else {
      if(root.valueType(GENERATOR_FIELD).equals(String.class)) {
        try {
          gen = (Class<? extends MapGenerator>) loader.loadClass((String) root.getValue(GENERATOR_FIELD));
        } catch (ClassNotFoundException ex) {
          String msg = String.format(Settings.getLocalString("warn_invalid_map_generator"), path);
          Logger.getLogger(Mod.class.getName()).log(Level.WARNING, msg, ex);
          return null;
        }
      }
    }
    
    //finally time to create the mod
    String msg = String.format(Settings.getLocalString("info_mod_found"), path);
    Logger.getLogger(Mod.class.getCanonicalName()).log(Level.INFO, msg);
    if(gen != null) {
      return new Mod((String) root.getValue(NAME_FIELD), gen);
    } else if(root.valueType(DEP_FIELD).equals(String.class)) {
      return new Mod((String) root.getValue(NAME_FIELD), (String) root.getValue(DEP_FIELD));
    } else {
      return new Mod((String) root.getValue(NAME_FIELD), (String[]) root.getValue(DEP_FIELD));
    }
  }
}
