package taiga.code.input;

/**
 * Receives {@link KeyboardEvent}s.
 * 
 * @author russell
 */
public interface KeyboardListener {
  /**
   * Called when a {@link KeyboardEvent} occurs.
   * 
   * @param event The {@link KeyboardEvent} that occurred.
   */
  public void handleEvent(KeyboardEvent event);
}
