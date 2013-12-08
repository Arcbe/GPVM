/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.io;

import gpvm.util.StringManager;
import java.io.IOException;

/**
 * An exception indicating that a data file was not valid and could not
 * be loaded.
 * 
 * @author russell
 */
public class InvalidDataFileException extends IOException {

  public InvalidDataFileException() {
    super(StringManager.getLocalString("err_invalid_data_file"));
  }

  public InvalidDataFileException(String message) {
    super(message);
  }
}
