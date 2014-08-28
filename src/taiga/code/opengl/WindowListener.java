/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.opengl;

/**
 * Listens for the creation and destruction of the {@link Window} backing a
 * {@link GraphicsSystem}.
 * 
 * @author russell
 */
public interface WindowListener {
  public void windowCreated();
  public void windowDestroyed();
  public void windowResized();
}
