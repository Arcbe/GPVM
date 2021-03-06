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

import java.io.IOException;
import java.net.URL;
import taiga.code.registration.NamedObject;
import taiga.code.util.DataNode;

/**
 * Base class for reading data files.  This provides a simple interface for
 * reading in files and determining what files an implementation can read.
 * @author russell
 */
public abstract class DataFileReader extends NamedObject {
  private static final long serialVersionUID = 1L;

  /**
   * Creates a new {@link DataFileReader}.
   * @param name 
   */
  public DataFileReader(String name) {
    super(name);
  }
  
  /**
   * Reads in a {@link URI}.  The returned {@link DataNode} is the root of the data
   * tree read from the given {@link URI}.
   * 
   * @param file The {@link URI} to read.
   * @return The data from the given {@link URI}.
   * @throws IOException Thrown if there is an exception file trying to read the
   * given file.
   */
  public abstract DataNode readFile(URL file) throws IOException;
  
  /**
   * Checks whether this {@link DataFileReader} can read the given file.
   * 
   * @param file The file to test
   * @return Whether the file can be read by this {@link DataFileReader}
   */
  public abstract boolean canReadFile(URL file);
}
