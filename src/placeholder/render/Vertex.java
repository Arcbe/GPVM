/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package placeholder.render;

import java.nio.ByteBuffer;

/**
 *
 * @author russell
 */
public interface Vertex {
  public ByteBuffer getData();
  public AttributeFormat[] getFormat();
  
  public static class AttributeFormat {
    public final int type;
    public final int attribute;
    public final int bytes;
    public final int offset;
    public final int size;

    public AttributeFormat(int type, int attribute, int bytes, int offset, int size) {
      this.type = type;
      this.attribute = attribute;
      this.bytes = bytes;
      this.offset = offset;
      this.size = size;
    }
  }
}
