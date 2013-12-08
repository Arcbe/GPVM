/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.io;

import gpvm.util.StringManager;

/**
 * Thrown by {@link DataNode} when an attempt is made to access a value
 * that does not exist.
 * 
 * @author russell
 */
public class NoDataValueException extends RuntimeException {
  
  /**
   * @see RuntimeException#RuntimeException() 
   */
  public NoDataValueException() {
  }
  
  /**
   * @see RuntimeException#RuntimeException(java.lang.String) 
   * @param message 
   */
  public NoDataValueException(String message) {
    super(message);
  }

  /**
   * @see RuntimeException#RuntimeException(java.lang.Throwable) 
   * @param cause 
   */
  public NoDataValueException(Throwable cause) {
    super(cause);
  }

  /**
   * @see RuntimeException#RuntimeException(java.lang.String, java.lang.Throwable) 
   * @param message
   * @param cause 
   */
  public NoDataValueException(String message, Throwable cause) {
    super(message, cause);
  }
}
