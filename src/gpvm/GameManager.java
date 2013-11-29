/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm;

import gpvm.render.RenderingSystem;
import gpvm.util.Settings;

/**
 * Controls the overall state of the game.  This class will initialize the
 * various system of the game and clean them up as needed.  For most cases 
 * interaction should go through the this class when possible so that all systems
 * are notified of changes.
 * 
 * @author russell
 */
public class GameManager {
  public static GameManager getInstance() {
    return instance;
  }
  
  public void startGame() {
    RenderingSystem.createSystem(Settings.getDisplayMode());
  }
  
  public boolean isPaused() {
    return paused;
  }
  
  public void setPaused(boolean val) {
    paused = val;
    
    RenderingSystem.getInstance().setPaused(val);
  }
  
  private boolean paused;
  
  private static GameManager instance = new GameManager();
}
