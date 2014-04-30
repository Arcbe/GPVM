/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.td;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import taiga.code.graphics.Renderable;

/**
 *
 * @author russell
 */
public class ship extends Renderable { 

  public static final String NAME = "ship";
  public static final float size = 0.05f;
  public static final float[] coordinates = new float[] {
    1f, 0.00f,
    -.6f, 0.8f,
    -.6f, -.8f,
    0.00f, 0.00f
  };
  public static final int[] indices = new int[] {
    0, 1, 3,
    0, 2, 3,
    2, 3, 1
  };
  
  private float x;
  private float y;
  private float accx;
  private float accy;
  
  public ship() {
    super(NAME);
  }
  
  public ship(String name) {
    super(name);
  }

  @Override
  protected void updateSelf() {
  }

  @Override
  protected void renderSelf(int pass) {
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glLoadIdentity();
    
    GL11.glScalef(size, size, 0);
    GL11.glRotated(Math.atan2(accy, accx), x, y, 0);
    
    GL11.glVertexPointer(2, 0, getCoords());
    GL11.glDrawElements(GL11.GL_TRIANGLES, getIndices());
  }
  
  private static FloatBuffer coordbuff;
  private static IntBuffer indbuff;
  
  private static FloatBuffer getCoords() {
    if(coordbuff == null) {
      coordbuff = BufferUtils.createFloatBuffer(coordinates.length);
      
      coordbuff.put(coordinates);
      coordbuff.flip();
    }
    
    return coordbuff;
  }
  
  private static IntBuffer getIndices() {
    if(indbuff == null) {
      indbuff = BufferUtils.createIntBuffer(indices.length);
      
      indbuff.put(indices);
      indbuff.flip();
    }
    
    return indbuff;
  }
}
