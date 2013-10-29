/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package placeholder.render.vertices;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import placeholder.render.Vertex;

/**
 *
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
  public ByteBuffer getData() {
    ByteBuffer data = BufferUtils.createByteBuffer(Float.SIZE * 3 / 8 + Integer.SIZE / 8);
    data.order(ByteOrder.nativeOrder());
    
    data.putFloat(x);
    data.putFloat(y);
    data.putFloat(z);
    data.putInt(color);
    data.flip();
    
    return data;
  }

  @Override
  public AttributeFormat[] getFormat() {
    return form;
  }
  
  private static AttributeFormat[] form = new AttributeFormat[] {
    new AttributeFormat(GL11.GL_FLOAT, GL11.GL_VERTEX_ARRAY, Float.SIZE * 3 / 8, 0, 3),
    new AttributeFormat(GL11.GL_UNSIGNED_BYTE, GL11.GL_COLOR_ARRAY, Integer.SIZE / 8, Float.SIZE * 3 / 8, 4)
  };
}
