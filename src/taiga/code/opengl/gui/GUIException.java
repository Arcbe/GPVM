/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.opengl.gui;

public class GUIException extends Exception {

  public GUIException() {
  }

  public GUIException(String message) {
    super(message);
  }

  public GUIException(String message, Throwable cause) {
    super(message, cause);
  }

  public GUIException(Throwable cause) {
    super(cause);
  }
}
