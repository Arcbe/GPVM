/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm;

import gpvm.map.Universe;
import gpvm.modding.ModManager;
import gpvm.render.GraphicsSystem;
import gpvm.util.StringManager;
import java.util.List;
import taiga.code.registration.RegisteredSystem;

import static gpvm.HardcodedValues.GAMEMANAGER_NAME;
import taiga.code.util.SettingManager;

/**
 * Controls the overall state of the game.  This class will initialize the
 * various system of the game and clean them up as needed.  For most cases 
 * interaction should go through the this class when possible so that all systems
 * are notified of changes.
 * 
 * @author russell
 */
public class GameManager extends RegisteredSystem {
  public static GameManager getInstance() {
    return instance;
  }
  
  public void startGame() {
    GraphicsSystem.createSystem(StringManager.getDisplayMode());
    
    //first load the mods, the manager already knows what mods to load.
    ModManager.getInstance().loadMods();
    
    List<String> names = ModManager.getInstance().getOverworlds();
    String start = names.get(0);
    
    Universe.getInstance().setActiveWorld(start);
  }

  @Override
  public void resetObject() {}

  @Override
  protected void startSystem() {}

  @Override
  protected void stopSystem() {}

  private GameManager() {
    super(GAMEMANAGER_NAME);
    
    addChild(new SettingManager());
  }
  
  private static GameManager instance = new GameManager();
}
