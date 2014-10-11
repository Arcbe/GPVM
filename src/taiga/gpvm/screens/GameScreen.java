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

package taiga.gpvm.screens;

import java.util.logging.Logger;
import taiga.code.math.Matrix4;
import taiga.code.opengl.Camera;
import taiga.code.opengl.RenderableSwitcher;
import taiga.code.registration.NamedObject;
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
    
    setPasses(HardcodedValues.NUM_GRAPHICS_LAYERS);
  }
  
  public void setCamera(Camera cam) {
    for(NamedObject obj : this) {
      if(obj instanceof WorldRenderer) {
        ((WorldRenderer)obj).cam = cam;
      }
    }
  }

  @Override
  protected void updateRenderable() {
  }

  @Override
  protected void renderSelf(int pass, Matrix4 proj) {
  }

  @Override
  public void worldCreated(World world) {
    WorldRenderer rend = new WorldRenderer(world);
    
    addChild(rend);
  }
  
  @Override
  public void worldChanged(WorldChange change, Object prev) {
    WorldRenderer world = getObject(change.world.name);
    
    if(world == null) return;
    world.worldChanged(change, prev);
  }
  
  private static final String locpref = GameScreen.class.getName().toLowerCase();
  
  private static final Logger log = Logger.getLogger(locpref, 
    System.getProperty("taiga.code.logging.text"));
}
