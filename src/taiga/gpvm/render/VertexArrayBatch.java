package taiga.gpvm.render;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * Uses a vertex array
 * @author russell
 */
public final class VertexArrayBatch implements RenderingBatch {

  @Override
  public void compile(RawBatch bat) {
    assert bat.vertices.length > 0;
    byte[] vert = bat.vertices[0].getData();
    stride = vert.length;
    rendermode = bat.rendermode;
    vertcnt = bat.vertices.length;
    
    //create and populate the buffer with the vertex data
    data = BufferUtils.createByteBuffer(vert.length * bat.vertices.length);
    for(int i = 0; i < bat.vertices.length; i++)
      data.put(bat.vertices[i].getData());
    data.flip();
    
    //create the attribute definition.
    attrs = new ArrayList<>();
    for(Vertex.AttributeFormat form : bat.vertices[0].getFormat())
      attrs.add(new AttributeDef(form, vert.length));
  }
  
  @Override
  public void draw() {
    //make sure that none of the arrays are enable accidentally
    GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
    GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
    
    //enable the vertex array
    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
    
    //load the data into the vertex arrays
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
  
  //the size in bytes of the vertices
  private int stride;
  private int rendermode;
  private int vertcnt;
  private ArrayList<AttributeDef> attrs;
  private ByteBuffer data;
  
  private static class AttributeDef {
    //the number of elements in the attribute e.g. 3 for 3D coordinates
    public int size;
    //the attribute value e.g. GL_VERTEX_ARRAY or GL_COLOR_ARRAY
    public int array;
    //the offset in bytes for the attribute from the start of each vertex
    public int offset;
    //the data type of the attribute e.g. GL_FLOAT or GL_BYTE
    public int type;
    
    public AttributeDef(Vertex.AttributeFormat format, int vertsize) {
      type = format.type;
      offset = format.offset;
      size = format.size;
      array = format.attribute;
    }
  }
}
