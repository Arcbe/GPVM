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

package taiga.gpvm.render;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import taiga.code.math.Matrix4;
import taiga.code.opengl.Renderable;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.entity.Entity;
import taiga.gpvm.util.geom.Coordinate;

/**
 * Handles batching {@link Entity}s together for rendering.
 * @author Russell Smith
 */
public class RegionalEntityRenderer extends Renderable {

  /**
   * Creates a new {@link EntityRenderer}.
   * 
   * @param worldname The name of the {@link World} to render.
   */
  public RegionalEntityRenderer(String worldname) {
    super(HardcodedValues.NAME_REGIONAL_ENTITY_RENDERER);
    
    this.renderers = new HashMap<>();
  }
  
  public void addRegion(Coordinate coor) {
    
  }

  @Override
  protected void updateRenderable() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected void renderSelf(int pass, Matrix4 proj) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  private final Map<EntityRenderer, Collection<Entity>> renderers;

  private static final String locprefix = RegionalEntityRenderer.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
