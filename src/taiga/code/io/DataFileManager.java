/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.io;

import taiga.code.util.DataNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.registration.RegisteredObject;
import taiga.code.text.TextLocalizer;

/**
 * A manager for reading in data files.  This manager can read any file that it
 * has a {@link DataFileReader} for.  In order to add a {@link DataFileReader}
 * to this manager it must be added as a child.
 * 
 * @author russell
 */
public class DataFileManager extends RegisteredObject {
  /**
   * The name that {@link DataFileManager}s are registered with.
   */
  public static final String DATAFILEMANAGER_NAME = "fileio";
  
  /**
   * The name of the root {@link DataNode} for loading data files.
   */
  public static final String DATANODE_ROOT_NAME = "root";

  /**
   * Creates a new {@link DataFileManager}.
   */
  public DataFileManager() {
    super(DATAFILEMANAGER_NAME);
  }
  
  /**
   * Attempts to read the given file using one of the attached {@link DataFileReader}.
   * If none can read the file this method will simply return null.
   * 
   * @param file The {@link File} to read.
   * @return The data read from the file or null.
   * @throws IOException If there was an exception while reading from the file.
   */
  public DataNode readFile(File file) throws IOException {
    if(!file.exists()) {
      throw new FileNotFoundException(TextLocalizer.localize(FILE_NOT_FOUND, file));
    }
    
    for(RegisteredObject obj : this) {
      if(obj != null && obj instanceof DataFileReader) {
        DataFileReader reader = (DataFileReader)obj;
        
        if(reader.canReadFile(file)) {
          return reader.readFile(file);
        }
      }
    }
    
    log.log(Level.WARNING, NO_READER, file);
    return null;
  }
  
  /**
   * This method will try to find a {@link File} in the classpath before trying the working directory.
   * If a {@link File} is found then this will load it into a {@link DataNode}.
   * 
   * @param fname The name of the file.
   * @return A {@link DataNode} for the data within the {@link File} or null.
   * @throws IOException Thrown if there is a problem loading the file.
   */
  public DataNode readFile(String fname) throws IOException {
    URL loc = getClass().getClassLoader().getResource(fname);
    
    if(loc != null) {
      try {
        URI loci = loc.toURI();
        return readFile(loci);
      } catch(URISyntaxException ex) {}
    }
    
    return readFile(new File(fname));
  }
  
  /**
   * Attempts to read the given file using one of the attached {@link DataFileReader}.
   * If none can read the file this method will simply return null.
   * 
   * @param file A {@link URI} for the {@link File} to load.
   * @return The data read from the file or null.
   * @throws IOException If there was an exception while reading from the file.
   */
  public DataNode readFile(URI file) throws IOException, URISyntaxException {
    return readFile(new File(file));
  }
  
  private static final String locprefix = DataFileManager.class.getName().toLowerCase();
  
  private static final String NO_READER = locprefix+ ".no_reader";
  private static final String FILE_NOT_FOUND = locprefix + ".file_not_found";
  
  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
