/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.io;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.registration.RegisteredObject;

/**
 * A manager for reading in data files.  This manager can read any file that it
 * has a {@link DataFileReader} for.  In order to add a {@link DataFileReader}
 * to this manager it must be added as a child.
 * 
 * @author russell
 */
public class DataFileManager extends RegisteredObject {
  public static final String DATAFILEMANAGER_NAME = "fileio";
  public static final String DATANODE_ROOT_NAME = "root";

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
   * Convenience method for reading files.  This simply calls {@link DataFileManager#readFile(java.io.File) }
   * with the {@link File} constructed from the given name.
   * 
   * @param fname
   * @return
   * @throws IOException 
   */
  public DataNode readFile(String fname) throws IOException {
    return readFile(new File(fname));
  }
  
  private static final String NO_READER = DataFileManager.class.getName().toLowerCase() + ".no_reader";
  
  private static final Logger log = Logger.getLogger(DataFileManager.class.getName(),
    System.getProperty("taiga.code.logging.text"));
}
