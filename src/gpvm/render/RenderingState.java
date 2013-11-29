/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.render;

/**
 * Combined with a {@link RawBatch} this class allows for the use of more
 * complex rendering operations.  After OpenGL is prepared to receive the
 * vertices from the {@link RawBatch} but before they are sent a {@link RenderingState}
 * is used if available.  This state allows for custom modifications to the OpenGL
 * context to be performed for rendering (e.g. setting up custom shaders).
 * 
 * This class is intended for use with a {@link RawBatch} and therefore should
 * have the ability to be batched.  This implies that the {@link RenderingState}
 * may only be called once before an arbitrary number of {@link RawBatch}es are
 * rendered.
 * 
 * @author russell
 */
public interface RenderingState {
  /**
   * This method is called once each rendering operation allowing for custom
   * rendering setup.  This will be called after the default setup based on
   * a {@link RawBatch}
   */
  public void setupState();
  
  /**
   * This checks to see if this {@link RenderingState} batched with a
   * given {@link RenderingState}.  Returning true implies that either rendering
   * state can be used for any {@link RawBatch} and have identical results.  Returning
   * false has no adverse affects, but may impact performance.
   * 
   * @param other The {@link RenderingState} check for compatibility.
   * @return Whether this {@link RenderingState} is compatible with the given one.
   */
  public boolean compatible(RenderingState other);
}
