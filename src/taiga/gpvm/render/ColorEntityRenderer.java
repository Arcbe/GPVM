/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.render;

import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.ReadableVector3f;
import taiga.code.io.DataNode;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.entity.Entity;

public class ColorEntityRenderer implements EntityRenderer {
  
  public static final String COLOR_FIELD = "color";

  @Override
  public void render(Entity ent, int pass) {
    if(pass != HardcodedValues.OPAQUE_WORLD_LAYER) return;
    
    GL11.glPointSize(size);
    
    GL11.glBegin(GL11.GL_POINTS);
    
    ReadableVector3f loc = ent.getBounds().getCenter();
    GL11.glVertex3f(loc.getX(), loc.getY(), loc.getZ());
    GL11.glColor3f(red, green, blue);
    
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
