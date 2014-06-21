package taiga.code.input;

/**
 * Contains the details of an event caused by a mouse button.
 * 
 * @author russell
 */
public class MouseButtonEvent {
  /**
   * The movement of the mouse wheel for this {@link MouseButtonEvent}.
   */
  public final int dwheel;
  
  /**
   * The button that caused this {@link MouseButtonEvent}.
   */
  public final int button;
  
  /**
   * Whether the mouse button is pressed or not.
   */
  public final boolean state;
  
  /**
   * The x coordinate of the event location.  The origin is located in the
   * bottom left corner of the window.
   */
  public final int x;
  
  /**
   * The y coordinate of the event location.  The origin is located in the bottom
   * left corner of the window.
   */
  public final int y;
  
  /**
   * The delta x of the event.
   */
  public final int dx;
  
  /**
   * The delta y of the event.
   */
  public final int dy;
  
  /**
   * The time of the event in nanoseconds.  The absolute time for this value
   * is not defined and only the duration between events has a defined meaning.
   */
  public final long time;

  /**
   * Creates a new {@link MouseButtonEvent} with the given values.
   * 
   * @param dwheel Movement of the mouse wheel.
   * @param button The button that caused the event.
   * @param state Whether the button was pressed.
   * @param x The x coordinate of the event location.
   * @param y The y coordinate of the event location.
   * @param dx The movement in the x direction for the event.
   * @param dy The movement in the y direction for the event.
   * @param time The time the event occurred.
   */
  public MouseButtonEvent(int dwheel, int button, boolean state, int x, int y, int dx, int dy, long time) {
    this.dwheel = dwheel;
    this.button = button;
    this.state = state;
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
    this.time = time;
  }
}
