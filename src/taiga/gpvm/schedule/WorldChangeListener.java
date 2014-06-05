package taiga.gpvm.schedule;

/**
 * A listener for changes to a {@link World} caused by a {@link WorldChange}.
 * 
 * @author russell
 */
public interface WorldChangeListener {
  /**
   * Called when the given {@link WorldChange} is applied.
   * 
   * @param change The {@link WorldChange} that was applied.
   * @param prev The previous value for the field changed by the {@link WorldChange}.
   */
  public void worldChanged(WorldChange change, Object prev);
}
