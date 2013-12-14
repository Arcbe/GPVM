/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.render;

/**
 *
 * @author russell
 */
public class RawBatch {
  /**
   * Optional array of indices that can be used for rendering.  Instead of
   * listing all vertices in order and possibly repeating some, this array of
   * indices can be used to index the vertices so that only copy is needed for 
   * each vertex.  Alternatively, this can be left null to indicate that the
   * list of vertices is all that should be sent to the OpenGL context.
   */
  public int[] indices;
  
  /**
   * A list of vertices that will be drawn to the screen.
   */
  public Vertex[] vertices;
  
  /**
   * The rendering mode that will be used with the list of vertices.
   */
  public int rendermode;
  
  /**
   * An optional object that can provide custom effects.  If left null
   * then this parameter will be ignored.
   */
  public RenderingState state;
  
  /**
   * Checks whether this {@link RawBatch} can be combined with a given
   * {@link RawBatch}
   * @param other The {@link RawBatch} to check.
   * @return Whether the two {@link RawBatch}es can be combined.
   */
  public boolean compatible(RawBatch other) {
    if(other.rendermode != rendermode) return false;
    
    //check the rendering states.
    if(state == null ^ other.state == null) return false;
    if(state != null && !state.compatible(other.state)) return false;
    
    if(indices == null ^ other.indices == null) return false;
    
    //quick check to see if either is empty.
    if(vertices.length == 0 || other.vertices.length == 0) return true;
    
    Vertex.AttributeFormat[] form = vertices[0].getFormat();
    Vertex.AttributeFormat[] oform = other.vertices[0].getFormat();
    if(form.length != oform.length) return false;
    
    for(int i = 0; i < form.length; i++) {
      boolean found = false;
      for(int j = 0; j < form.length; j++) {
        if(form[i].equals(oform[j])) {
          found = true;
          break;
        }
      }
      
      if(!found) return false;
    }
    
    return true;
  }
  
  /**
   * Combines two {@link RawBatch}es so that this batch can be used to
   * render the original two batches.
   * 
   * @param other The {@link RawBatch} to combine with this one.
   */
  public void combine(RawBatch other) {
    assert compatible(other);
    
    //if there is nothing in this batch then just copy the other one
    if(vertices == null) {
      rendermode = other.rendermode;
      vertices = other.vertices;
      indices = other.indices;
      return;
    }
    
    //copy the vertices into the new array.
    Vertex[] nvertices = new Vertex[vertices.length + other.vertices.length];
    System.arraycopy(vertices, 0, nvertices, 0, vertices.length);
    System.arraycopy(other.vertices, 0, nvertices, vertices.length, other.vertices.length);
    vertices = nvertices;
    
    //copy the indices into the new array
    if(indices == null) return;
    int[] nindices = new int[indices.length + other.indices.length];
    System.arraycopy(indices, 0, nindices, 0, indices.length);
    //don't forget to offset the indices from the other batch.
    for(int i = 0; i < other.indices.length; i++)
      nindices[i + indices.length] = indices[i] + vertices.length;
    indices = nindices;
  }
}
