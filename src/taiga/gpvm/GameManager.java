/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm;

import java.awt.event.WindowEvent;
import taiga.code.input.InputSystem;
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
import taiga.gpvm.registry.TileRenderingRegistry;
import taiga.gpvm.registry.SkyRegistry;
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
    TileRenderingRegistry rendreg = getObject(HardcodedValues.TILE_RENDERING_REGISTRY_NAME);
    GraphicsRoot graphics = getObject(HardcodedValues.GRAPHICSSYSTEM_NAME);
    GameScreen gamescreen = getObject(HardcodedValues.GAME_SCREEN_NAME);
    Universe uni = getObject(HardcodedValues.UNIVERSE_NAME);
    WorldUpdater updater = getObject(HardcodedValues.WORLD_UPDATER_NAME);
    InputSystem input = getObject(HardcodedValues.INPUT_SYSTEM_NAME);
    SkyRegistry skies = getObject(HardcodedValues.SKY_REGISTRY_NAME);
    
    if(client) {
      if(rendreg == null) rendreg = addChild(new TileRenderingRegistry());
      if(graphics == null) graphics = addChild(new GraphicsRoot());
      if(gamescreen == null) gamescreen = graphics.addChild(new GameScreen());
      if(input == null) input = addChild(new InputSystem(HardcodedValues.INPUT_SYSTEM_NAME));
      if(skies == null) skies = addChild(new SkyRegistry());
      
      graphics.addUpdateable(input);
      graphics.addWindowListener(this);
      uni.addListener(gamescreen);
      updater.addWorldChangeListener(gamescreen);
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
