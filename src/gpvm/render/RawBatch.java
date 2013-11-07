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
}
