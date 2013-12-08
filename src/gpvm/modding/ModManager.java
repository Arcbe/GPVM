/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.modding;

import gpvm.io.InvalidDataFileException;
import gpvm.util.StringManager;
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
import java.util.List;
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
  /**
   * An enum representing the various states that the {@link ModManager} can be
   * in during the course of the Game.
   */
  public enum State {
    /**
     * In this state no mods are loaded, this may be before any have been loaded
     * or after all mods have been unloaded.
     */
    Unloaded,
    
    /**
     * This state indicates that mods are currently being loaded by the manager.
     */
    Loading,
    /**
     * In this state all active mods have been loaded, however any inactive mods
     * are not loaded at this time.
     */
    Loaded,
    
    /**
     * This state indicates that the mod manager is currently unloading all mods.
     */
    Unloading
  }
  
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
  
  public void addActiveMod(String modname) {
    if(curstate != State.Unloaded) {
      log.log(Level.WARNING, StringManager.getLocalString("warn_act_mod_while_loaded"), modname);
      return;
    }
    
    Mod addition = getMod(modname);
    
    if(addition == null) {
      log.log(Level.WARNING, StringManager.getLocalString("warn_act_unknown_mod"));
      return;
    }
    
    activemods.add(addition);
    
    log.log(Level.INFO, StringManager.getLocalString("info_activate_mod"), modname);
  }
  
  public void setActiveMods(String ... modnames) {
    log.log(Level.INFO, StringManager.getLocalString("info_clear_act_mods"));
    
    activemods.clear();
    
    for(int i = 0; i < modnames.length; i++)
      addActiveMod(modnames[i]);
  }
  
  public void setActiveMods(Mod.ModIdentifier ... ids) {
    String[] names = new String[ids.length];
    
    for(int i = 0; i < ids.length; i++)
      names[i] = ids[i].name;
    
    setActiveMods(names);
  }

  public void loadMods() {
    //check to mae sure everything is in order
    if(curstate != State.Unloaded) {
      log.log(Level.WARNING, StringManager.getLocalString("warn_mods_already_loaded"));
      return;
    }
    
    if(activemods.isEmpty()) {
      log.log(Level.WARNING, StringManager.getLocalString("warn_no_active_mods"));
      return;
    }
    
    //set the state
    curstate = State.Loading;
    
    //begin the loading
    //all of the mods should be initialized.
    for(Mod mod : activemods) {
      mod.preload();
    }
    
    log.log(Level.INFO, StringManager.getLocalString("info_mods_preloaded"));
    
    for(Mod mod : activemods) {
      mod.load();
    }
    
    log.log(Level.INFO, StringManager.getLocalString("info_mods_loaded"));
    
    for(Mod mod : activemods) {
      mod.postload();
    }
    
    log.log(Level.INFO, StringManager.getLocalString("info_mods_postloaded"));
    
    //set the state
    curstate = State.Loaded;
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
        
        addMod(m);
      } catch (FileNotFoundException | MalformedURLException | InvalidDataFileException ex) {
        log.log(Level.SEVERE, null, ex);
      }
    }
  }
  
  public void addMod(Mod add) {
    add.intialize();
    
    mods.put(add.getIdentifier().name, add);
  }
  
  private Map<String, Mod> mods;
  private List<Mod> activemods;
  private State curstate;
  
  private static final Logger log = Logger.getLogger(ModManager.class.getName());
  
  private ModManager() {
    mods = new HashMap<>();
    activemods = new ArrayList<>();
    curstate = State.Unloaded;
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
      log.log(Level.WARNING, StringManager.getLocalString("warn_invalid_file"), file);
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
      return FileVisitResult.CONTINUE;
    }
    
  }
}
