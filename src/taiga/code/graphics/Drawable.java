package taiga.code.graphics;

/**
 * An interface for objects that can be drawn to the window.  This is for
 * 2D drawing.
 * 
 * @author russell
 */
public interface Drawable {
  
  /**
   * Returns the width of this {@link Drawable}.  The exact interpretation
   * depends on the implementing class, but this should be a width guaranteed
   * not to cause stretching.
   * @return 
   */
  public int getWidth();
  
  /**
   * Returns the height of this {@link Drawable}.  The exact interpretation
   * depends on the implementing class, but this should be a height guaranteed
   * not to cause stretching.
   * 
   * @return The height of this {@link Drawable}.
   */
  public int getHeight();
  
  /**
   * Draws the {@link Drawable} in the given area.  The {@link Drawable}
   * should fill the entire area.
   * 
   * @param x The x coordinate of the drawing area.
   * @param y The y coordinate of the drawing area.
   * @param w The width of the drawing area.
   * @param h The height of the drawing area.
   */
  public void draw(int x, int y, int w, int h);
}
