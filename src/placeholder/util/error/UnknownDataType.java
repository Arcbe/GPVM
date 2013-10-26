/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package placeholder.util.error;

/**
 * Exception indicating that an unknown data type was used.  The data type
 * may not be valid or may just have not been programmed for.
 * 
 * @author russell
 */
public class UnknownDataType extends RuntimeException {

  /**
   * Constructs an exception without a detail message.
   */
  public UnknownDataType() {
  }
  
  /**
   * Constructs an exception with the specified detail message.
   * 
   * @param detail Te message for detailing the exception.
   */
  public UnknownDataType(String detail) {
    super(detail);
  }
}
