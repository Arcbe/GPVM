package taiga.code.registration;

/**
 * A listener for {@link RegisteredSystem} events.  This listener will be notified
 * of the starting and stopping of the {@link RegisteredSystem} it is listening to.
 * 
 * @author russell
 */
public interface SystemListener {
  /**
   * Called when the {@link RegisteredSystem} starts.
   * 
   * @param sys The {@link RegisteredSystem} that started.
   */
  public void systemStarted(RegisteredSystem sys);
  
  /**
   * Called when the {@link RegisteredSystem} stops.
   * 
   * @param sys The {@link RegisteredSystem} that stopped.
   */
  public void systemStopped(RegisteredSystem sys);
}
