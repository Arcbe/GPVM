/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package placeholder.render;

/**
 * Compiles a RawBatch of vertices into a ready to render state.
 * 
 * @author russell
 */
public interface RenderingBatch {
  /**
   * Compiles a RawBatch into a ready to draw state.  Each time this is
   * called the previous state, if there is one, should be cleared and replaced
   * by this state.
   * 
   * @param batch The batch of vertices that will be rendered.
   */
  public void compile(RawBatch batch);
  
  /**
   * Renders the current state of the RenderBatch.  If there has not been
   * a previous call to compile, the results of this operation are undefined.
   */
  public void draw();
}
