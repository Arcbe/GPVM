/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.map;

/**
 * A {@link RuntimeException} thrown when a {@link WorldMutator} attempts to
 * target a {@link World} that already has a {@link WorldMutator} targeting it.
 * 
 * @author russell
 */
public class MutatorPresentException extends RuntimeException {

  public MutatorPresentException() {
  }

  public MutatorPresentException(String message) {
    super(message);
  }

  public MutatorPresentException(String message, Throwable cause) {
    super(message, cause);
  }

  public MutatorPresentException(Throwable cause) {
    super(cause);
  }

  public MutatorPresentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
