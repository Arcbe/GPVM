/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm;

import taiga.gpvm.render.ColorRenderer;
import taiga.gpvm.render.Renderer;

/**
 * The various values that are hard coded into the game platform.
 * @author russell
 */
public class HardcodedValues {
  //internal system names
  public static final String GAMEMANAGER_NAME = "game";
  public static final String GRAPHICSSYSTEM_NAME = "graphics";
  public static final String TILE_REGISTRY_NAME = "tiles";
  public static final String RENDERING_REGISTRY_NAME = "renderinginfo";
  public static final String MAP_GENERATOR_NAME = "generator";
  public static final String GAME_MAP_NAME = "map";
  public static final String GAME_SCREEN_NAME = "gamescreen";
  public static final String UNIVERSE_NAME = "universe";
  
  //field names for Tile Registry entries
  public static final String CANON_NAME_FIELD = "canonicalname";
  public static final String SOLID_FIELD = "solid";
  public static final String OPAQUE_FIELD = "opaque";
  public static final String NAMESPACE_SEPERATOR = ".";
  
  //field names for Rendering Registry
  public static final String RENDERER_CLASS_FIELD = "renderer";
  public static final String RENDERING_INFO_FIELD = "rendering-info";
  public static final Class<? extends Renderer> DEFAULT_RENDERER = ColorRenderer.class;
}
