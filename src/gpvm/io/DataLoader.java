/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the loading of data files.  Data files are loaded into a tree 
 * structure of {@link DataNode}s.  Only data files that have loaders registered
 * can be loaded by this class.
 * 
 * @author russell
 */
public final class DataLoader {
  /**
   * Registers a {@link DataFileLoader} to an extension or set of extensions.
   * When a file is loaded with the registered extensions, it will use the given
   * {@link DataFileLoader}.
   * 
   * @param loader The {@link DataFileLoader} to register.
   * @param extensions The file extensions to register the {@link DataFileLoader} to.
   * These extension should not include a '.'.
   */
  @SafeVarargs
  public static void registerLoader(DataFileLoader loader, String ... extensions) {
    for(String ext : extensions) {
      loaders.put(ext, loader);
    }
  }
  
  /**
   * Loads a data file.  The data file is loaded with the {@link DataFileLoader}
   * registered for the extension of the file.  If the file does not have an
   * extension each registered loader is checked to see if it can load the file.
   * Depending on how the loader is programmed the file may not be loaded regardless
   * of whether one of the loaders could load it.  In this case null is returned.
   * 
   * @param data A {@link File} that contains data to be loaded.
   * @return A {@link DataNode} for the root element of the data file given or null.
   * @throws FileNotFoundException Thrown if the file is either not present or
   * it is not a readable file.
   */
  public static DataNode loadFile(File data) throws FileNotFoundException {
    //check to make sure that the file is actually present;
    if(!data.canRead() || !data.isFile()) {
      throw new FileNotFoundException();
    }
    
    //get the loader for the given files extension if there is a file extension.
    String extension = data.getName();
    int index = extension.lastIndexOf('.');
    if(index != -1) {
      extension = extension.substring(index + 1);
      DataFileLoader loader = loaders.get(extension);

      if(loader != null) return loader.loadFile(data);
    }
    
    //try all of the loaders to see if one can load the file
    for(DataFileLoader l : loaders.values()) {
      if(l.canLoad(data)) {
        return l.loadFile(data);
      }
    }
    
    //if nothing works return null.
    return null;
  }
  
  private static Map<String, DataFileLoader> loaders = new HashMap<>();
}
