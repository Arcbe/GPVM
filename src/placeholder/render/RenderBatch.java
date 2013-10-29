package placeholder.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import placeholder.util.geometry.Matrix;

/**
 *
 * @author russell
 */
public final class RenderBatch {

  public RenderBatch(RawBatch bat) {
    assert bat.vertices.length > 0;
    ByteBuffer vert = bat.vertices[0].getData();
    stride = vert.limit();
    elecnt = bat.vertices.length;
    rendermode = bat.rendermode;
    vertcnt = bat.vertices.length;
    
    //create and populate the buffer with the vertex data
    data = BufferUtils.createByteBuffer(vert.limit() * elecnt);
    for(int i = 0; i < bat.vertices.length; i++)
      data.put(bat.vertices[i].getData());
    data.flip();
    
    //create the attribute definition.
    attrs = new ArrayList<>();
    for(Vertex.AttributeFormat form : bat.vertices[0].getFormat())
      attrs.add(new AttributeDef(form, vert.limit()));
  }
  
  public void draw(Matrix transform) {
    //make sure that none of the arrays are enable accidentally
    GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
    GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
    
    //enable the vertex array
    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
    
    for(AttributeDef def : attrs) {
      switch(def.array) {
        case GL11.GL_VERTEX_ARRAY:
          data.position(def.offset);
          GL11.glVertexPointer(def.size, def.type, stride, data.slice());
          break;
        case GL11.GL_COLOR_ARRAY:
          //adjust the array in order to get the color values
          data.position(def.offset);
          GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
          GL11.glColorPointer(def.size, def.type, stride, data.slice());
          break;
        case GL11.GL_TEXTURE_COORD_ARRAY:
          data.position(def.offset);
          GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
          GL11.glTexCoordPointer(def.size, def.type, stride, data);
          break;
          //TODO: figure out normals.
      }
    }
    
    GL11.glDrawArrays(rendermode, 0, vertcnt);
  }
  
  private int stride;
  private int rendermode;
  private int elecnt;
  private int vertcnt;
  private ArrayList<AttributeDef> attrs;
  private ByteBuffer data;
  
  private static class AttributeDef {
    public AttributeDef(Vertex.AttributeFormat format, int vertsize) {
      type = format.type;
      offset = format.offset;
      size = format.size;
      array = format.attribute;
    }
    
    public int size;
    public int array;
    public int offset;
    public int type;
  }
}
