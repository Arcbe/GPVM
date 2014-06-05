/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.screens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import taiga.code.graphics.RenderableSwitcher;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.map.UniverseListener;
import taiga.gpvm.map.World;
import taiga.gpvm.render.WorldRenderer;
import taiga.gpvm.schedule.WorldChange;
import taiga.gpvm.schedule.WorldChangeListener;

/**
 * The main screen for the game.  This screen will render to the world the {@link Camera}
 * is currently in, and an interface.
 * 
 * @author russell
 */
public class GameScreen extends RenderableSwitcher implements UniverseListener, WorldChangeListener {
  
  /**
   * Creates a new {@link GameScreen}.
   */
  public GameScreen() {
    super(HardcodedValues.GAME_SCREEN_NAME);
    
    listchildren = new ArrayList<>();
  }

  @Override
  protected void updateSelf() {
  }

  @Override
  protected void renderSelf(int pass) {
  }

  @Override
  public void worldCreated(World world) {
    WorldRenderer rend = new WorldRenderer(world);
    
    addChild(rend);
    listchildren.add(rend);
  }
  
  @Override
  public void worldChanged(WorldChange change, Object prev) {
    for(WorldChangeListener list : listchildren)
      list.worldChanged(change, prev);
  }
  
  private Collection<WorldChangeListener> listchildren;
  
  private static final String locpref = GameScreen.class.getName().toLowerCase();
  
  private static final String NO_UNIVERSE = locpref + ".no_universe";
  
  private static final Logger log = Logger.getLogger(locpref, 
    System.getProperty("taiga.code.logging.text"));
}
