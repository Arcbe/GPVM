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

import taiga.code.input.InputSystem;
import taiga.code.opengl.GraphicsSystem;
import taiga.code.io.SettingManager;
import taiga.gpvm.entity.EntityManager;
import taiga.gpvm.event.MapEventManager;
import taiga.gpvm.map.MapGenerator;
import taiga.gpvm.map.RegionManager;
import taiga.gpvm.map.Universe;
import taiga.gpvm.opengl.ResourceManager;
import taiga.gpvm.registry.EntityRegistry;
import taiga.gpvm.registry.EntityRenderingRegistry;
import taiga.gpvm.schedule.WorldUpdater;
import taiga.gpvm.registry.TileRenderingRegistry;
import taiga.gpvm.registry.TileRegistry;
import taiga.gpvm.render.ColorTileRenderer;
import taiga.gpvm.render.RegionalEntityRenderer;
import taiga.gpvm.render.TileRenderer;
import taiga.gpvm.screens.GameScreen;

/**
 * The various values that are hard coded into the game platform.
 * @author russell
 */
public class HardcodedValues {
  /**
   * Separator character for object names.  This is used to separate name spaces
   * from objects and other name spaces.
   */
  public static final String NAMESPACE_SEPERATOR = ".";
  
  //graphics layer numbers
  //<editor-fold>
  /**
   * The number of layers for rendering.  These will not necessarily be layers in
   * a geometrical sense, e.g. transparencies will have there own layer be will be mixed
   * in spatially with opaque objects.
   */
  public static final int NUM_GRAPHICS_LAYERS = 3;
  /**
   * The layer for rendering backgrounds.
   */
  public static final int SKY_LAYER = 0;
  /**
   * The layer for rendering opaque objects in the world.
   */
  public static final int OPAQUE_WORLD_LAYER = 2;
  //</editor-fold>
  
  //internal system names
  //<editor-fold>
  /**
   * The name for the {@link ResourceManager}.
   */
  public static final String NAME_RESOURCE_MANAGER = "assets";
  /**
   * The name for the {@link RegionalEntityRenderer}.
   */
  public static final String NAME_REGIONAL_ENTITY_RENDERER = "entities";
  /**
   * The name for the {@link EntityRenderingRegistry}.
   */
  public static final String NAME_ENTITY_RENDERING_REGISTRY = "entity-rendering-registry";
  /**
   * The name for the {@link EntityRegistry}.
   */
  public static final String NAME_ENTITY_REGISTRY = "entity-registry";
  /**
   * The name for the {@link EntityManager}s.
   */
  public static final String NAME_ENTITY_MANAGER = "entities";
  /**
   * The name for the {@link InputSystem}.
   */
  public static final String NAME_INPUT_SYSTEM = "input";
  /**
   * The name for the {@link SettingManager}.  This is the same as {@link SettingManager#SETTINGMANAGER_NAME}
   */
  public static final String NAME_SETTING_MANAGER = SettingManager.SETTINGMANAGER_NAME;
  /**
   * The name for the {@link RegionManager}.
   */
  public static final String NAME_REGION_MANAGER = "region-manager";
  /**
   * Name for the {@link GameManager}.
   */
  public static final String NAME_GAME_MANAGER = "game";
  /**
   * Name for the {@link GraphicsSystem}.
   */
  public static final String NAME_GRAPHICS_SYSTEM = "graphics";
  /**
   * Name for the {@link TileRegistry}
   */
  public static final String NAME_TILE_REGISTRY = "tiles";
  /**
   * Name for the {@link TileRenderingRegistry}.
   */
  public static final String NAME_TILE_RENDERING_REGISTRY = "tile-rendering-registry";
  /**
   * Name for {@link MapGenerator}s.
   */
  public static final String NAME_MAP_GENERATOR = "generator";
  /**
   * Name for the {@link GameScreen}
   */
  public static final String NAME_GAME_SCREEN = "game-screen";
  /**
   * Name for {@link Universe}s
   */
  public static final String NAME_UNIVERSE = "universe";
  /**
   * Name for {@link WorldUpdater}.
   */
  public static final String NAME_WORLD_UPDATER = "updater";
  /**
   * Name for {@link WorldUpdater} networking.
   */
  public static final String NAME_COMMS = "comms";
  /**
   * Name for {@link MapEventManager}.
   */
  public static final String NAME_MAP_EVENT_MANAGER = "map-events";
  //</editor-fold>
  
  //field names for data files
  //<editor-fold>
  /**
   * The field name for names in various data files.
   */
  public static final String FIELD_NAME_NAME = "name";
  //</editor-fold>
  
  /**
   * The default {@link TileRenderer} for tiles.  This will result in a solid magenta
   * cube.
   */
  public static final Class<? extends TileRenderer> DEFAULT_RENDERER = ColorTileRenderer.class;
  /**
   * The delay in milliseconds between updates of the world map.
   */
  public static final int UPDATE_DELAY = 100;
}
