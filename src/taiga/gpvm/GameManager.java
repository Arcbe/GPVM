/*
 * Copyright (C) 2014 Russell Smith.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package taiga.gpvm;

import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.input.InputSystem;
import taiga.code.registration.NamedSystem;

import static taiga.gpvm.HardcodedValues.NAME_GAME_MANAGER;
import taiga.code.io.DataFileManager;
import taiga.code.opengl.WindowListener;
import taiga.code.io.SettingManager;
import taiga.code.opengl.Camera;
import taiga.code.opengl.GraphicsSystem;
import taiga.code.io.YAMLDataReader;
import taiga.code.opengl.shaders.ShaderProgram;
import taiga.gpvm.entity.Entity;
import taiga.gpvm.entity.EntityManager;
import taiga.gpvm.event.MapEventManager;
import taiga.gpvm.map.MapGenerator;
import taiga.gpvm.map.Universe;
import taiga.gpvm.map.World;
import taiga.gpvm.opengl.ResourceManager;
import taiga.gpvm.opengl.ShaderLoader;
import taiga.gpvm.opengl.TexLoader;
import taiga.gpvm.opengl.TextureResource;
import taiga.gpvm.registry.EntityType;
import taiga.gpvm.registry.EntityRegistry;
import taiga.gpvm.registry.EntityRenderingRegistry;
import taiga.gpvm.schedule.WorldUpdater;
import taiga.gpvm.registry.TileRenderingRegistry;
import taiga.gpvm.registry.TileRegistry;
import taiga.gpvm.render.SkyBoxRenderer;
import taiga.gpvm.render.WorldRenderer;
import taiga.gpvm.screens.GameScreen;
import taiga.gpvm.util.geom.Coordinate;

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
    super(NAME_GAME_MANAGER);
    
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
    TileRenderingRegistry tilerendreg = getObject(HardcodedValues.NAME_TILE_RENDERING_REGISTRY);
    EntityRenderingRegistry entrendreg = getObject(HardcodedValues.NAME_ENTITY_RENDERING_REGISTRY);
    GraphicsSystem graphics = getObject(HardcodedValues.NAME_GRAPHICS_SYSTEM);
    GameScreen gamescreen = getObject(HardcodedValues.NAME_GAME_SCREEN);
    Universe uni = getObject(HardcodedValues.NAME_UNIVERSE);
    WorldUpdater updater = getObject(HardcodedValues.NAME_WORLD_UPDATER);
    InputSystem input = getObject(HardcodedValues.NAME_INPUT_SYSTEM);
    ResourceManager assets = getObject(HardcodedValues.NAME_RESOURCE_MANAGER);
    
    if(client) {
      if(tilerendreg == null) tilerendreg = addChild(new TileRenderingRegistry());
      if(entrendreg == null) entrendreg = addChild(new EntityRenderingRegistry());
      if(graphics == null) graphics = addChild(new GraphicsSystem(HardcodedValues.NAME_GRAPHICS_SYSTEM));
      if(gamescreen == null) gamescreen = graphics.addChild(new GameScreen());
      if(input == null) input = addChild(new InputSystem(HardcodedValues.NAME_INPUT_SYSTEM));
      if(assets == null) assets = graphics.addChild(new ResourceManager(HardcodedValues.NAME_RESOURCE_MANAGER));
      
      graphics.addUpdateable(input);
      graphics.addWindowListener(this);
      uni.addListener(gamescreen);
      updater.addWorldChangeListener(gamescreen);
      assets.addLoader(new ShaderLoader(), ShaderProgram.class);
      assets.addLoader(new TexLoader(), TextureResource.class);
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
    GameScreen screen = getObject(HardcodedValues.NAME_GRAPHICS_SYSTEM, HardcodedValues.NAME_GAME_SCREEN);
    
    if(screen != null) screen.setCamera(cam);
  }
  
  public World createWorld(String name, MapGenerator gen) {
    Universe uni = getObject(HardcodedValues.NAME_UNIVERSE);
    if(uni == null) {
      return null;
    }
    
    return uni.addWorld(name, gen);
  }
  
  /**
   * Sets the {@link SkyBoxRenderer} that will be used for the {@link World}
   * with the given name.
   * 
   * @param name The name of the {@link World} to add a {@link SkyBoxRenderer} to.
   * @param sky The {@link SkyBoxRenderer} to add.
   */
  public void setWorldSky(String name, SkyBoxRenderer sky) {
    WorldRenderer world = getObject(HardcodedValues.NAME_GRAPHICS_SYSTEM, HardcodedValues.NAME_GAME_SCREEN, name);
    if(world == null) return;
    
    world.addChild(sky);
  }
  
  /**
   * Creates and returns a new {@link Entity} with the type of the given
   * name.  If there is no type with the given name then this will immediately
   * return null.
   * 
   * @param type The name of the type for the new {@link Entity}.
   * @return The new {@link Entity}.
   */
  public Entity createEntity(String type) {
    return createEntity(getEntityType(type));
  }
  
  public Entity createEntity(EntityType type) {
    return createEntity(type, new Coordinate());
  }
  
  public Entity createEntity(String type, Coordinate location) {
    return createEntity(getEntityType(type), location);
  }
  
  public Entity createEntity(EntityType type, Coordinate location) {
    if(type == null) {
      log.log(Level.WARNING, NO_ENTITY_TYPE);
      return null;
    }
    
    EntityManager man = getObject(HardcodedValues.NAME_ENTITY_MANAGER);
    assert man != null;
    
    return man.createEntity(type, location);
  }
  
  /**
   * Returns the {@link EntityType} with the given name from the {@link
   * EntityRegistry}.  If there is not an {@link EntityType} with the given
   * name, then this will return null.
   * 
   * @param name The name of the {@link EntityType} to return.
   * @return The desired {@link EntityType} or null.
   */
  public EntityType getEntityType(String name) {
    EntityRegistry reg = getObject(HardcodedValues.NAME_ENTITY_REGISTRY);
    assert reg != null;
    
    return reg.getEntry(name);
  }

  @Override
  public void windowCreated() {
  }

  @Override
  public void windowDestroyed() {
    if(isRunning())
      stop();
  }
  
  @Override
  public void windowResized() {
    
  }
  
  private static final String locprefix = GameManager.class.getName().toLowerCase();
  
  private static final String NO_ENTITY_TYPE = locprefix + ".no_entity_type";
  
  private static final Logger log = Logger.getLogger(locprefix);
}
