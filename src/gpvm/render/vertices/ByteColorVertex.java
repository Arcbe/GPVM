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
public class ByteColorVertex implements Vertex{
  /**
   * The position on the x axis of the {@link ColorVertex}.
   */
  public byte x;
  /**
   * The position on the y axis of the {@link ColorVertex}.
   */
  public byte y;
  /**
   * The position on the z axis of the {@link ColorVertex}.
   */
  public byte z;
  
  /**
   * The color of the vertex in the ARGB format.
   */
  public int color;

  /**
   * Creates a new {@link ColorVertex} from the given values.
   * 
   * @param x The x coordinate.
   * @param y The y coordinate.
   * @param z The z coordinate.
   * @param color The color of the vertex.
   */
  public ByteColorVertex(byte x, byte y, byte z, int color) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.color = color;
  }
  
  @Override
  public byte[] getData() {
    byte[] result = new byte[3 + Integer.SIZE / 8];
    
    //encode position
    result[0] = x;
    result[1] = y;
    result[2] = z;
    
    //encode color
    IntUtils.intToBytes(color, result, 3, ByteOrder.nativeOrder());
    
    return result;
  }

  @Override
  public AttributeFormat[] getFormat() {
    return form;
  }
  
  private static AttributeFormat[] form = new AttributeFormat[] {
    new AttributeFormat(GL11.GL_BYTE, GL11.GL_VERTEX_ARRAY, 0, 3),
    new AttributeFormat(GL11.GL_UNSIGNED_BYTE, GL11.GL_COLOR_ARRAY, 3, 4)
  };
}