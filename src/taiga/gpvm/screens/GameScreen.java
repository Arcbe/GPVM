/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.screens;

import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.graphics.RenderableSwitcher;
import taiga.code.registration.ChildListener;
import taiga.code.registration.RegisteredObject;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.map.Universe;
import taiga.gpvm.map.UniverseListener;
import taiga.gpvm.map.World;
import taiga.gpvm.render.WorldRenderer;

/**
 * The main screen for the game.  This screen will render to the world the {@link Camera}
 * is currently in, and an interface.
 * 
 * @author russell
 */
public class GameScreen extends RenderableSwitcher implements UniverseListener {
  
  /**
   * Creates a new {@link GameScreen}.
   */
  public GameScreen() {
    super(HardcodedValues.GAME_SCREEN_NAME);
  }

  @Override
  protected void updateSelf() {
  }

  @Override
  protected void renderSelf(int pass) {
  }

  @Override
  public void worldCreated(World world) {
    addChild(new WorldRenderer(world));
  }

  @Override
  protected void dettached(RegisteredObject parent) {
    RegisteredObject obj = getObject(HardcodedValues.UNIVERSE_NAME);
    if(obj instanceof Universe) {
      ((Universe)obj).removeListener(this);
    } else {
      log.log(Level.WARNING, NO_UNIVERSE);
    }
  }

  @Override
  protected void attached(RegisteredObject parent) {
    RegisteredObject obj = getObject(HardcodedValues.UNIVERSE_NAME);
    if(obj instanceof Universe) {
      ((Universe)obj).addListener(this);
    }
  }
  
  private static final String locpref = GameScreen.class.getName().toLowerCase();
  
  private static final String NO_UNIVERSE = locpref + ".no_universe";
  
  private static final Logger log = Logger.getLogger(locpref, 
    System.getProperty("taiga.code.logging.text"));
}
