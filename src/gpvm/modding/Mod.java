/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.modding;

import gpvm.io.DataLoader;
import gpvm.io.DataNode;
import gpvm.io.InvalidDataFileException;
import gpvm.map.MapGenerator;
import gpvm.util.Settings;
import java.io.FileNotFoundException;
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
public final class Mod {
  public static final String NAME_FIELD = "name";
  public static final String DEP_FIELD = "dependency";
  public static final String GENERATOR_FIELD = "map-generator";
  
  /**
   * Contains information that can uniquely identify a mod.
   */
  public static class ModIdentifier {
    public String name;
    public String version;

    public ModIdentifier() {
    }

    public ModIdentifier(String name, String version) {
      this.name = name;
      this.version = version;
    }
  
    @Override
    public String toString() {
      if(version == null) return name;
      return String.format("%s %s", name, version);
    }
  }
  
  public boolean validate() {
    boolean result = true;
    
    if(name == null) {
      String msg = String.format(Settings.getLocalString("warn_mod_missing_name"), location);
      Logger.getLogger(Mod.class.getCanonicalName()).log(Level.WARNING, msg);
      result = false;
    }
    
    return result;
  }
  
  public ModIdentifier getIdentifier() {
    return new ModIdentifier(name, version);
  }
  
  @Override
  public String toString() {
    if(version == null) return name;
    return String.format("%s %s", name, version);
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
  public static Mod createMod(Path path) throws FileNotFoundException, MalformedURLException, InvalidDataFileException {
    //first see if we can load the file.
    DataNode root = DataLoader.loadFile(path.toFile());
    if(root == null) {
      String msg = String.format(Settings.getLocalString("warn_invalid_mod"), path);
      Logger.getLogger(Mod.class.getCanonicalName()).log(Level.WARNING, msg);
      return null;
    }
    
    //declare the various mod parameters
    Mod result = new Mod();
    result.loader = URLClassLoader.newInstance(new URL[]{path.toUri().toURL()});
    
    for(String field : root.getValues()) {
      switch(field) {
        //field for the name of the mod
        case NAME_FIELD:
          if(root.isType(field, String.class)) {
            result.name = root.getValue(field);
          } else {
            String msg = String.format(Settings.getLocalString("warn_mod_name_string"), path);
            Logger.getLogger(Mod.class.getName()).log(Level.WARNING, msg);
          }
          break;
          
        //field for the class of the mods map generator.
        case GENERATOR_FIELD:
          try {
            result.mapgenerator = (Class<? extends MapGenerator>) result.loader.loadClass(root.getValue(field).toString());
          } catch(ClassNotFoundException ex) {
            String msg = String.format(Settings.getLocalString("warn_invalid_map_generator"), path);
            Logger.getLogger(Mod.class.getName()).log(Level.WARNING, msg, ex);
          }
          break;
      }
    }
    
    //check to make sure the mod is good.
    if(result.validate()) {
      String msg = String.format(Settings.getLocalString("info_mod_found"), result.name, path);
      Logger.getLogger(Mod.class.getName()).log(Level.INFO, msg);
      return result;
    } else return null;
  }
  
  private URLClassLoader loader;
  
  private Path location;
  private Class<? extends MapGenerator> mapgenerator;
  private String name;
  private String version;
  
  private Mod() {
  }
}
