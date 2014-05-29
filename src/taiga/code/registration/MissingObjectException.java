/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.registration;

import taiga.code.text.TextLocalizer;

/**
 *
 * @author russell
 */
public class MissingObjectException extends Exception {
  public MissingObjectException() {
    super(TextLocalizer.localize(DEFAULT_MESSAGE));
  }

  public MissingObjectException(String message) {
    super(message);
  }

  public MissingObjectException(String message, Throwable cause) {
    super(message, cause);
  }

  public MissingObjectException(Throwable cause) {
    super(TextLocalizer.localize(DEFAULT_MESSAGE), cause);
  }

  public MissingObjectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
  
  private static final String DEFAULT_MESSAGE = MissingObjectException.class.getName().toLowerCase() + ".default_message";
}
