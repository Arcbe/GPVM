/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm;

import taiga.code.input.InputSystem;
import taiga.code.registration.NamedSystem;

import static taiga.gpvm.HardcodedValues.GAMEMANAGER_NAME;
import taiga.code.io.DataFileManager;
import taiga.code.opengl.WindowListener;
import taiga.code.io.SettingManager;
import taiga.code.opengl.Camera;
import taiga.code.opengl.GraphicsSystem;
import taiga.code.yaml.YAMLDataReader;
import taiga.gpvm.entity.EntityManager;
import taiga.gpvm.event.MapEventManager;
import taiga.gpvm.map.MapGenerator;
import taiga.gpvm.map.Universe;
import taiga.gpvm.map.World;
import taiga.gpvm.registry.EntityRegistry;
import taiga.gpvm.registry.EntityRenderingRegistry;
import taiga.gpvm.schedule.WorldUpdater;
import taiga.gpvm.registry.TileRenderingRegistry;
import taiga.gpvm.registry.SkyRegistry;
import taiga.gpvm.registry.TileRegistry;
import taiga.gpvm.render.SkyBoxRenderer;
import taiga.gpvm.render.WorldRenderer;
import taiga.gpvm.screens.GameScreen;

/**
 * Controls the overall state of the game.  This class will initialize the
 * various system of the game and clean them up as needed.  For most cases 
 * interaction should go through the this class when possible so that all systems
 * are notified of changes.
 * 
 * @author russell
 */
public final class GameManager extends NamedSystem implements WindowListener {

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
    EntityRegistry entities = new EntityRegistry();
    Universe uni = new Universe();
    WorldUpdater updater = new WorldUpdater();
    MapEventManager events = new MapEventManager();
    EntityManager entman = new EntityManager();
    
    addChild(tiles);
    addChild(entities);
    addChild(uni);
    addChild(updater);
    addChild(events);
    addChild(entman);
    
    uni.addListener(updater);
    updater.addWorldChangeListener(events);
    updater.addUpdateable(entman, 0, 1);
    
    setServerMode(server);
    setClientMode(client);
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
  public void setClientMode(boolean client) {
    TileRenderingRegistry tilerendreg = getObject(HardcodedValues.TILE_RENDERING_REGISTRY_NAME);
    EntityRenderingRegistry entrendreg = getObject(HardcodedValues.ENTITY_RENDERING_REGISTRY_NAME);
    GraphicsSystem graphics = getObject(HardcodedValues.GRAPHICS_SYSTEM_NAME);
    GameScreen gamescreen = getObject(HardcodedValues.GAME_SCREEN_NAME);
    Universe uni = getObject(HardcodedValues.UNIVERSE_NAME);
    WorldUpdater updater = getObject(HardcodedValues.WORLD_UPDATER_NAME);
    InputSystem input = getObject(HardcodedValues.INPUT_SYSTEM_NAME);
    SkyRegistry skies = getObject(HardcodedValues.SKY_REGISTRY_NAME);
    
    if(client) {
      if(tilerendreg == null) tilerendreg = addChild(new TileRenderingRegistry());
      if(entrendreg == null) entrendreg = addChild(new EntityRenderingRegistry());
      if(graphics == null) graphics = addChild(new GraphicsSystem(HardcodedValues.GRAPHICS_SYSTEM_NAME));
      if(gamescreen == null) gamescreen = graphics.addChild(new GameScreen());
      if(input == null) input = addChild(new InputSystem(HardcodedValues.INPUT_SYSTEM_NAME));
      if(skies == null) skies = addChild(new SkyRegistry());
      
      graphics.addUpdateable(input);
      graphics.addWindowListener(this);
      uni.addListener(gamescreen);
      updater.addWorldChangeListener(gamescreen);
    }
  }
  
  /**
   * Sets the {@link Camera} that will be used to render the game {@link World}.
   * This is a convenience method and will simply set the {@link Camera} on the
   * {@link GameScreen}.
   * 
   * @param cam The {@link Camera} to use for rendering the {@link World}.
   */
  public void setWorldCamera(Camera cam) {
    GameScreen screen = getObject(HardcodedValues.GRAPHICS_SYSTEM_NAME, HardcodedValues.GAME_SCREEN_NAME);
    
    if(screen != null) screen.setCamera(cam);
  }
  
  public World createWorld(String name, MapGenerator gen) {
    Universe uni = getObject(HardcodedValues.UNIVERSE_NAME);
    if(uni == null) {
      return null;
    }
    
    return uni.addWorld(name, gen);
  }
  
  public void setWorldSky(String name, SkyBoxRenderer sky) {
    WorldRenderer world = getObject(HardcodedValues.GRAPHICS_SYSTEM_NAME, HardcodedValues.GAME_SCREEN_NAME, name);
    if(world == null) return;
    
    world.addChild(sky);
  }

  @Override
  public void windowCreated() {
  }

  @Override
  public void windowDestroyed() {
    stop();
  }
}
