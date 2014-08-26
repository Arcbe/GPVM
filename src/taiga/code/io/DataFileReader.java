/*
 * Copyright (C) 2014 Russell Smith
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package taiga.code.io;

import taiga.code.util.DataNode;
import java.io.IOException;
import java.net.URI;
import taiga.code.registration.NamedObject;

/**
 * Base class for reading data files.  This provides a simple interface for
 * reading in files and determining what files an implementation can read.
 * @author russell
 */
public abstract class DataFileReader extends NamedObject {

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
  public abstract DataNode readFile(URI file) throws IOException;
  
  /**
   * Checks whether this {@link DataFileReader} can read the given file.
   * 
   * @param file The file to test
   * @return Whether the file can be read by this {@link DataFileReader}
   */
  public abstract boolean canReadFile(URI file);
}
