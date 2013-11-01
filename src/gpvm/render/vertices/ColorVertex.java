/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.render.vertices;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import gpvm.render.Vertex;
import gpvm.util.FloatUtils;
import gpvm.util.IntUtils;

/**
 * Represents a position in world space with a defined color.
 * @author russell
 */
public class ColorVertex implements Vertex{
  public float x;
  public float y;
  public float z;
  
  public int color;

  public ColorVertex(float x, float y, float z, int color) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.color = color;
  }
  
  @Override
  public byte[] getData() {
    byte[] result = new byte[Float.SIZE * 3 / 8 + Integer.SIZE / 8];
    
    //encode position
    FloatUtils.floatToBytes(x, result, 0, ByteOrder.nativeOrder());
    FloatUtils.floatToBytes(y, result, 4, ByteOrder.nativeOrder());
    FloatUtils.floatToBytes(z, result, 8, ByteOrder.nativeOrder());
    
    //encode color
    IntUtils.intToBytes(color, result, 12, ByteOrder.nativeOrder());
    
    return result;
  }

  @Override
  public AttributeFormat[] getFormat() {
    return form;
  }
  
  private static AttributeFormat[] form = new AttributeFormat[] {
    new AttributeFormat(GL11.GL_FLOAT, GL11.GL_VERTEX_ARRAY, 0, 3),
    new AttributeFormat(GL11.GL_UNSIGNED_BYTE, GL11.GL_COLOR_ARRAY, Float.SIZE * 3 / 8, 4)
  };
}