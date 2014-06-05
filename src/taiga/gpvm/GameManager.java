/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm;

import java.awt.event.WindowEvent;
import taiga.code.registration.RegisteredSystem;

import static taiga.gpvm.HardcodedValues.GAMEMANAGER_NAME;
import taiga.gpvm.render.GraphicsRoot;
import taiga.code.io.DataFileManager;
import taiga.code.opengl.WindowListener;
import taiga.code.registration.RegisteredObject;
import taiga.code.util.SettingManager;
import taiga.code.yaml.YAMLDataReader;
import taiga.gpvm.event.MapEventManager;
import taiga.gpvm.map.Universe;
import taiga.gpvm.schedule.WorldUpdater;
import taiga.gpvm.registry.RenderingRegistry;
import taiga.gpvm.registry.TileRegistry;
import taiga.gpvm.schedule.WorldChangeListener;
import taiga.gpvm.screens.GameScreen;

/**
 * Controls the overall state of the game.  This class will initialize the
 * various system of the game and clean them up as needed.  For most cases 
 * interaction should go through the this class when possible so that all systems
 * are notified of changes.
 * 
 * @author russell
 */
public final class GameManager extends RegisteredSystem implements WindowListener {

  @Override
  public void resetObject() {}

  @Override
  protected void startSystem() {}

  @Override
  protected void stopSystem() {}

  /**
   * Creates a new {@link GameManager} with the given setting for being a server
   * or client.
   * 
   * @param server Whether this {@link GameManager} will be a server.
   * @param client Whether this {@link GameManager} will be a client.
   */
  public GameManager(boolean server, boolean client) {
    super(GAMEMANAGER_NAME);
    
    addChild(new DataFileManager());
    getObject(DataFileManager.DATAFILEMANAGER_NAME).addChild(new YAMLDataReader());
    
    SettingManager settings = new SettingManager();
    addChild(settings);
    
    
    TileRegistry tiles = new TileRegistry();
    Universe uni = new Universe();
    WorldUpdater updater = new WorldUpdater();
    MapEventManager events = new MapEventManager();
    
    addChild(tiles);
    addChild(uni);
    addChild(updater);
    addChild(events);
    
    uni.addListener(updater);
    updater.addWorldChangeListener(events);
    
    setServerMode(server);
    setClientMoe(client);
  }
  
  /**
   * Changes whether this {@link GameManager} is a sever or not.
   * 
   * @param server Whether this {@link GameManager} should be a server.
   */
  public void setServerMode(boolean server) {
    if(server) {
      
    }
  }
  
  /**
   * Changes whether this {@link GameManager} is a client or not.
   * 
   * @param client Whether this {@link GameManager} should be a client.
   */
  public void setClientMoe(boolean client) {
    RegisteredObject rendreg = getObject(HardcodedValues.RENDERING_REGISTRY_NAME);
    RegisteredObject graphics = getObject(HardcodedValues.GRAPHICSSYSTEM_NAME);
    RegisteredObject gamescreen = getObject(HardcodedValues.GAME_SCREEN_NAME);
    RegisteredObject uni = getObject(HardcodedValues.UNIVERSE_NAME);
    RegisteredObject updater = getObject(HardcodedValues.WORLD_UPDATER_NAME);
    
    if(client) {
      if(rendreg == null) addChild(new RenderingRegistry());
      if(graphics == null) {
        graphics = new GraphicsRoot();
        addChild(graphics);
      }
      if(gamescreen == null) {
        gamescreen = new GameScreen();
        graphics.addChild(gamescreen);
      }
      
      ((GraphicsRoot)graphics).addWindowListener(this);
      ((Universe)uni).addListener((GameScreen)gamescreen);
      ((WorldUpdater)updater).addWorldChangeListener((WorldChangeListener) gamescreen);
    }
  }

  @Override
  public void windowCreated() {
  }

  @Override
  public void windowDestroyed() {
    stop();
  }
}
