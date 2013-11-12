package gpvm.io;

import java.io.File;

/**
 * Common interface for loading data files.  A {@link DataFileLoader} is
 * registered with the {@link DataLoader} and then used to load data files.
 * 
 * @author russell
 */
public interface DataFileLoader {
  /**
   * Used to load a data file.  The format of the file will depend on which
   * file formats a given {@link DataFileLoader} is registered for in the {@link
   * DataFileLoader}.
   * 
   * @param file The {@link File} to load
   * @return The {@link DataNode} for the root of the data file
   */
  public DataNode loadFile(File file);
  
  /**
   * Checks whether a file can be loaded by this class.  This class should
   * return false if it cannot load a {@link File} or if it cannot be determined.  A
   * true value indicates that the {@link File} can definitely be loaded with this
   * {@link DataFileLoader}
   * @param file The {@link File} to check.
   * @return Whether this loader can load the given {@link File}.
   */
  public boolean canLoad(File file);
}
