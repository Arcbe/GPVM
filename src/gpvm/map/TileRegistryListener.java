package gpvm.map;

/**
 * A listener for changes to a {@link TileRegistry}
 * @author russell
 */
public interface TileRegistryListener {
  /**
   * Called in the event that the {@link TileRegistry} is cleared of all entries.
   */
  public void registryCleared();
  /**
   * Called when an entry is added to the {@link TileRegistry}.
   * 
   * @param def The {@link TileDefinition} that was added to the registry.
   * @param id The id that was assigned to the {@link TileDefinition}.
   */
  public void entryAdded(TileDefinition def, long id);
}
