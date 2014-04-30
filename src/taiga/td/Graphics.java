/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.td;

import org.lwjgl.opengl.GL11;
import taiga.code.opengl.GraphicsSystem;

/**
 *
 * @author russell
 */
public class Graphics extends GraphicsSystem {
  
  public static final String NAME = "graphics";

  public Graphics() {
    super(NAME);
  }

  @Override
  protected void rendering() {
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    
    GL11.glEnable(GL11.GL_VERTEX_ARRAY);
  }
  
}
