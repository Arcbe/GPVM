/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.opengl.gui;

import java.util.logging.Logger;

public class Button extends BackedComponent {

  public Button(String name) {
    super(name);
  }

  private static final String locprefix = Button.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
