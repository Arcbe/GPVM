/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import taiga.code.registration.RegisteredObject;

/**
 * Base class for reading data files.  This provides a simple interface for
 * reading in files and determining what files an implementation can read.
 * @author russell
 */
public abstract class DataFileReader extends RegisteredObject {

  /**
   * Creates a new {@link DataFileReader}.
   * @param name 
   */
  public DataFileReader(String name) {
    super(name);
  }
  
  /**
   * Reads in a {@link File}.  The returned {@link DataNode} is the root of the data
   * tree read from the given {@link File}.
   * 
   * @param file The {@link File} to read.
   * @return The data from the given {@link File}.
   * @throws IOException Thrown if there is an exception file trying to read the
   * given file.
   */
  public abstract DataNode readFile(File file) throws IOException;
  
  /**
   * Checks whether this {@link DataFileReader} can read the given file.
   * 
   * @param file The file to test
   * @return Whether the file can be read by this {@link DataFileReader}
   */
  public abstract boolean canReadFile(File file);
}
