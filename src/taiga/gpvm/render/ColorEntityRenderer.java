/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
