/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.input;

import org.lwjgl.input.Keyboard;

/**
 *
 * @author russell
 */
public interface KeyListener {
  /**
   * Called when the key being listened for is pressed.
   * 
   * @param code The code for the key that was pressed. This will be one of the
   * constants in the {@link Keyboard} class.
   */
  public void keyPressed(int code);
  
  /**
   * Called when the key being listened for is released.
   * 
   * @param code The code for the key that was releases.  This will be one of the
   * constants in the {@link Keyboard} class.
   */
  public void keyReleased(int code);
}
