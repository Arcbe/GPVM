/*
 * Copyright (C) 2014 Russell Smith.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package taiga.code.io;

import taiga.code.util.DataNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;
import taiga.code.registration.NamedObject;
import taiga.code.text.TextLocalizer;

/**
 * A manager for reading in data files.  This manager can read any file that it
 * has a {@link DataFileReader} for.  In order to add a {@link DataFileReader}
 * to this manager it must be added as a child.
 * 
 * @author russell
 */
public class DataFileManager extends NamedObject {
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
   * This method will try to find a {@link File} in using the current
   * context {@link ClassLoader} for the {@link Thread} before trying the 
   * current working directory if the {@link ClassLoader} did not have
   * the file. If a {@link File} is found then this will load it into a {@link DataNode}.
   * 
   * @param fname The name of the file.
   * @return A {@link DataNode} for the data within the {@link File} or null.
   * @throws IOException Thrown if there is a problem loading the file.
   */
  public DataNode readFile(String fname) throws IOException {
    ClassLoader context = Thread.currentThread().getContextClassLoader();
    URL loc = context.getResource(fname);
    
    if(loc != null) {
      return readFile(loc);
    }
    
    URL loci = new URL(fname);
    return readFile(loci);
  }
  
  /**
   * Attempts to read the given file using one of the attached {@link DataFileReader}.
   * If none can read the file this method will simply return null.
   * 
   * @param file A {@link URL} for the {@link File} to load.
   * @return The data read from the file or null.
   * @throws IOException If there was an exception while reading from the file.
   */
  public DataNode readFile(URL file) throws IOException {
    for(NamedObject obj : this) {
      if(obj != null && obj instanceof DataFileReader) {
        DataFileReader reader = (DataFileReader)obj;
        
        if(reader.canReadFile(file)) {
          return reader.readFile(file);
        }
      }
    }
    
    throw new IOException(TextLocalizer.localize(NO_READER, file));
  }
  
  private static final String locprefix = DataFileManager.class.getName().toLowerCase();
  
  private static final String NO_READER = locprefix+ ".no_reader";
  
  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
