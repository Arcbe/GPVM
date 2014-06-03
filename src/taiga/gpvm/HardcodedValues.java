/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm;

import taiga.code.opengl.GraphicsSystem;
import taiga.gpvm.map.MapGenerator;
import taiga.gpvm.map.Universe;
import taiga.gpvm.map.WorldUpdater;
import taiga.gpvm.registry.RenderingRegistry;
import taiga.gpvm.registry.TileRegistry;
import taiga.gpvm.render.ColorRenderer;
import taiga.gpvm.render.Renderer;
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
  
  //internal system names
  //<editor-fold>
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
   * Name for the {@link RenderingRegistry}.
   */
  public static final String RENDERING_REGISTRY_NAME = "renderinginfo";
  /**
   * Name for {@link MapGenerator}s.
   */
  public static final String MAP_GENERATOR_NAME = "generator";
  /**
   * Name for the {@link GameScreen}
   */
  public static final String GAME_SCREEN_NAME = "gamescreen";
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
  public static final String WORLD_COMMS_NAME = "comms";
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
