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

import java.nio.FloatBuffer;
import java.util.Collection;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.ReadableVector3f;
import taiga.code.math.ReadableMatrix4;
import taiga.code.util.DataNode;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.entity.Entity;

public class ColorEntityRenderer implements EntityRenderer {
  
  public static final String COLOR_FIELD = "color";

  @Override
  public void render(Collection<Entity> ents, int pass, ReadableMatrix4 proj, ReadableMatrix4 modelview) {
    if(pass != HardcodedValues.OPAQUE_WORLD_LAYER) return;
    
    ARBShaderObjects.glUseProgramObjectARB(0);
    GL11.glPointSize(size);
    
    FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
    proj.store(buffer, false);
    buffer.flip();
    
    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadMatrix(buffer);
    
    buffer.rewind();
    modelview.store(buffer, false);
    buffer.flip();
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glLoadMatrix(buffer);
    
    GL11.glBegin(GL11.GL_POINTS);
    
    for(Entity ent : ents) {
      ReadableVector3f loc = ent.getBounds().getCenter();
      GL11.glVertex3f(loc.getX(), loc.getY(), loc.getZ());
      GL11.glColor3f(red, green, blue);
    }
    
    GL11.glEnd();
  }

  @Override
  public void loadData(DataNode data) {
    if(data == null || data.getObject(COLOR_FIELD) == null) {
      red = 1f;
      green = 0f;
      blue = 1f;
    } else {
      int color = (int) data.<DataNode>getObject(COLOR_FIELD).data;
      
      blue = (0xFF & color) / 255f;
      green = (0xFF & color >> 8) / 255f;
      red = (0xFF & color >> 16) / 255f;
    }
    
    if(data == null || data.getObject(COLOR_FIELD) == null) {
      size = 2f;
    } else {
      size = (float) data.<DataNode>getObject(COLOR_FIELD).data;
    }
  }
  
  private float red;
  private float green;
  private float blue;
  private float size;

  private static final String locprefix = ColorEntityRenderer.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
