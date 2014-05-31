package taiga.gpvm.map;

/**
 * A listener for the creation or destruction of a {@link World} in a {@link Universe}.
 * 
 * @author russell
 */
public interface UniverseListener {
  /**
   * Called when a new {@link World} has been created in the {@link Universe}.
   * 
   * @param world The {@link World} that was created.
   */
  public void worldCreated(World world);
}
