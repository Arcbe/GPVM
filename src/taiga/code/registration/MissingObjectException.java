/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.registration;

/**
 * An {@link Exception} thrown when a {@link RegisteredObject} cannot be found in
 * a registration tree.
 * 
 * @author russell
 */
public class MissingObjectException extends RuntimeException {
  /**
   * Creates a new {@link MissingObjectException} with a default message.
   */
  public MissingObjectException() {
    super();
  }

  /**
   * Creates a new {@link MissingObjectException} with the given message.
   * 
   * @param message The message for this {@link MissingObjectException}
   */
  public MissingObjectException(String message) {
    super(message);
  }

  /**
   * Creates a new {@link MissingObjectException} with a given message and cause.
   * 
   * @param message The message for this {@link MissingObjectException}
   * @param cause The cause for this {@link MissingObjectException}.
   */
  public MissingObjectException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new {@link MissingObjectException} with a default message and cause.
   * 
   * @param cause The cause for this {@link MissingObjectException}.
   */
  public MissingObjectException(Throwable cause) {
    super(cause);
  }
  
  private static final String DEFAULT_MESSAGE = MissingObjectException.class.getName().toLowerCase() + ".default_message";
}
