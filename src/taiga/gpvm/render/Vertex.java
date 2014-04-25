/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm.render;

/**
 * Represents a single point in world space combine with rendering data.
 * The implementation of this interface will determine exactly what data will be
 * available, such as colors or texture coordinates.
 * 
 * @author russell
 */
public interface Vertex {
  
  /**
   * Returns all the data contained in the vertex as a byte array.  The
   * get format method will then define how that data is formatted in the array.
   * 
   * @return A byte buffer of the data in this vertex.
   */
  public byte[] getData();
  
  /**
   * Returns the format of the data received from the getData method.
   * This format must at least contain an entry for position information, all 
   * other attributes are optional.
   * 
   * @return 
   */
  public AttributeFormat[] getFormat();
  
  /**
   * Defines how an attribute is arranged in a ByteBuffer.
   */
  public static class AttributeFormat {
    /**
     * The primitive type of the data e.g. GL_FLOAT
     */
    public final int type;
    
    /**
     * The constant representing the attribute in OpenGL e.g. GL_COLOR_ARRAY
     */
    public final int attribute;
    
    /**
     * The offset in bytes for where the attribute is stored in the buffer.
     */
    public final int offset;
    
    /**
     * The number of components for this attribute e.g. 3 for a 3D coordinate.
     */
    public final int size;

    /**
     * Constructs a new attribute format.
     * 
     * @param type The type of the attribute
     * @param attribute The enum for hte attribute
     * @param offset The offset within the vertex for the attribute
     * @param size The number of values associated with the attribute.
     */
    public AttributeFormat(int type, int attribute, int offset, int size) {
      this.type = type;
      this.attribute = attribute;
      this.offset = offset;
      this.size = size;
    }
  }
}
