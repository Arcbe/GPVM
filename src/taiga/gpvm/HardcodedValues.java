/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm;

import taiga.code.input.InputSystem;
import taiga.code.opengl.GraphicsSystem;
import taiga.code.util.SettingManager;
import taiga.gpvm.entity.EntityManager;
import taiga.gpvm.event.MapEventManager;
import taiga.gpvm.map.MapGenerator;
import taiga.gpvm.map.RegionManager;
import taiga.gpvm.map.Universe;
import taiga.gpvm.registry.EntityRegistry;
import taiga.gpvm.registry.EntityRenderingRegistry;
import taiga.gpvm.schedule.WorldUpdater;
import taiga.gpvm.registry.TileRenderingRegistry;
import taiga.gpvm.registry.TileRegistry;
import taiga.gpvm.render.ColorRenderer;
import taiga.gpvm.render.Renderer;
import taiga.gpvm.render.SkyBoxRenderer;
import taiga.gpvm.screens.GameScreen;

/**
 * The various values that are hard coded into the game platform.
 * @author russell
 */
public class HardcodedValues {
  /**
   * Separator character for object names.  This is used to separate namespaces
   * from objects and other namespaces.
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
   * The name for the {@link EntityRenderingRegistry}.
   */
  public static final String ENTITY_RENDERING_REGISTRY_NAME = "entity-rendering-registry";
  /**
   * The name for the {@link EntityRegistry}.
   */
  public static final String ENTITY_REGISTRY_NAME = "entity-registry";
  /**
   * The name for the {@link EntityManager}s.
   */
  public static final String ENTITY_MANAGER_NAME = "entities";
  /**
   * The name for the {@link SkyBoxRenderer}.
   */
  public static final String SKY_REGISTRY_NAME = "skies";
  /**
   * The name for the {@link InputSystem}.
   */
  public static final String INPUT_SYSTEM_NAME = "input";
  /**
   * The name for the {@link SettingManager}.  This is the same as {@link SettingManager#SETTINGMANAGER_NAME}
   */
  public static final String SETTING_MANAGER_NAME = SettingManager.SETTINGMANAGER_NAME;
  /**
   * The name for the {@link RegionManager}.
   */
  public static final String REGION_MANAGER_NAME = "region-manager";
  /**
   * Name for the {@link GameManager}.
   */
  public static final String GAMEMANAGER_NAME = "game";
  /**
   * Name for the {@link GraphicsSystem}.
   */
  public static final String GRAPHICSSYSTEM_NAME = "graphics";
  /**
   * Name for the {@link TileRegistry}
   */
  public static final String TILE_REGISTRY_NAME = "tiles";
  /**
   * Name for the {@link TileRenderingRegistry}.
   */
  public static final String TILE_RENDERING_REGISTRY_NAME = "tile-renedering-registry";
  /**
   * Name for {@link MapGenerator}s.
   */
  public static final String MAP_GENERATOR_NAME = "generator";
  /**
   * Name for the {@link GameScreen}
   */
  public static final String GAME_SCREEN_NAME = "game-screen";
  /**
   * Name for {@link Universe}s
   */
  public static final String UNIVERSE_NAME = "universe";
  /**
   * Name for {@link WorldUpdater}.
   */
  public static final String WORLD_UPDATER_NAME = "updater";
  /**
   * Name for {@link WorldUpdater} networking.
   */
  public static final String COMMS_NAME = "comms";
  /**
   * Name for {@link MapEventManager}.
   */
  public static final String MAP_EVENT_MANAGER_NAME = "map-events";
  //</editor-fold>
  
  //field names for Tile Registry entries
  //<editor-fold>
  /**
   * Name for the solid attribute in tile data files.
   */
  public static final String SOLID_FIELD = "solid";
  /**
   * Name for the opaque attribute in tile data files.
   */
  public static final String OPAQUE_FIELD = "opaque";
  //</editor-fold>
  
  //field names for Rendering Registry
  // <editor-fold>
  /**
   * Name for the rendering class field in rendering information data files.
   */
  public static final String RENDERER_CLASS_FIELD = "renderer";
  /**
   * Name for the rendering information data in rendering information data files.
   */
  public static final String RENDERING_INFO_FIELD = "rendering-info";
  //</editor-fold>
  
  /**
   * The default {@link Renderer} for tiles.  This will result in a solid magenta
   * cube.
   */
  public static final Class<? extends Renderer> DEFAULT_RENDERER = ColorRenderer.class;
  /**
   * The delay in milliseconds between updates of the world map.
   */
  public static final int UPDATE_DELAY = 100;
}
