/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.modding;

import gpvm.util.Settings;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles finding and loading mods.  Mods require a modinfo file in
 * order to be loaded.  These files will be searched for in the directories and
 * subdirectories recursively from the working directory.  They will also be
 * search for in the root of any zip or jar file found during the search.
 * 
 * @author russell
 */
public class ModManager {
  public static ModManager getInstance() {
    return instance;
  }
  
  public Mod.ModIdentifier[] getFoundMods() {
    Mod.ModIdentifier[] result = new Mod.ModIdentifier[mods.size()];
    
    int index = 0;
    for(Mod m : mods.values())
      result[index++] = m.getIdentifier();
    
    return result;
  }

  public Mod getMod(Mod.ModIdentifier id) {
    return getMod(id.name);
  }
  
  public Mod getMod(String id) {
    return mods.get(id);
  }
  
  public void findMods() {
    //create matchers for the various types of mod files
    FileSystem files = FileSystems.getDefault();
    PathMatcher modinfos = files.getPathMatcher("glob:modinfo{,.*}");
    PathMatcher jars = files.getPathMatcher("glob:*.{jar, zip}");
 
    //search for all of the modinfos
    ModVisitor mvisit = new ModVisitor(jars, modinfos);
    try {
      Files.walkFileTree(files.getPath("."), mvisit);
    } catch (IOException ex) {
      //this error should not happen.
      log.log(Level.SEVERE, null, ex);
    }
    
    //go through all of the possible mod infos in the file system.
    for(Path path : mvisit.mods) {
      try {
        Mod m = Mod.createMod(path);
        
        Mod.ModIdentifier id = m.getIdentifier();
        if(mods.containsKey(id.name)) {
          
        }
        
        mods.put(m.getIdentifier().name, m);
      } catch (FileNotFoundException | MalformedURLException ex) {
        log.log(Level.SEVERE, null, ex);
      }
    }
  }
  
  private Map<String, Mod> mods;
  private static final Logger log = Logger.getLogger(ModManager.class.getName());
  
  private ModManager() {
    mods = new HashMap<>();
  }
  
  private static ModManager instance = new ModManager();
  
  private static class ModVisitor implements FileVisitor<Path> {
    public PathMatcher jar;
    public PathMatcher mod;
    public ArrayList<Path> jars;
    public ArrayList<Path> mods;

    public ModVisitor(PathMatcher jar, PathMatcher mod) {
      this.jar = jar;
      this.mod = mod;
      jars = new ArrayList<>();
      mods = new ArrayList<>();
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      if(jar.matches(file.getFileName()) && attrs.isRegularFile()) jars.add(file);
      else if(mod.matches(file.getFileName()) && attrs.isRegularFile()) mods.add(file);
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
      log.log(Level.WARNING, Settings.getLocalString("warn_invalid_file"), file);
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
      return FileVisitResult.CONTINUE;
    }
    
  }
}
