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
  
  public ArrayList<Mod.ModIdentifier> getFoundMods() {
    ArrayList<Mod.ModIdentifier> result = new ArrayList<>();
    
    for(Mod m : mods) {
      result.add(m.getIdentifier());
    }
    
    return result;
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
      Logger.getLogger(ModManager.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    //go through all of the possible mod infos.
    for(Path path : mvisit.mods) {
      try {
        Mod m = Mod.createMod(path);
        mods.add(m);
      } catch (FileNotFoundException ex) {
        Logger.getLogger(ModManager.class.getName()).log(Level.SEVERE, null, ex);
      } catch (MalformedURLException ex) {
        Logger.getLogger(ModManager.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  
  private ArrayList<Mod> mods;
  
  private ModManager() {
    mods = new ArrayList<>();
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
      String msg = String.format(Settings.getLocalString("warn_invalid_file"), file);
      Logger.getLogger(ModManager.class.getCanonicalName()).log(Level.WARNING, msg, exc);
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
      return FileVisitResult.CONTINUE;
    }
    
  }
}
