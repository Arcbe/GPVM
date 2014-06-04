package taiga.gpvm.schedule;

import taiga.gpvm.util.geom.Coordinate;
import taiga.gpvm.map.Tile;
import taiga.gpvm.map.World;
import taiga.gpvm.map.WorldMutator;
import taiga.gpvm.registry.TileEntry;

/**
 * A single task for changing a {@link World}.  This change can be a change of
 * {@link TileEntry}, damage, or change of a metadata for a {@link Tile}.
 * @author russell
 */
public class WorldChange {
  /**
   * The {@link World} that this {@link WorldChange} will change.
   */
  public final World world;
  /**
   * The {@link Coordinate} of the {@link Tile} to change.
   */
  public final Coordinate location;
  /**
   * Any data needed for the change.
   */
  public final Object[] data;
  /**
   * The type of change that this {@link WorldChange} represents.
   */
  public final ChangeType type;
  /**
   * The update to apply this change on.
   */
  public final long update;

  /**
   * Creates a new {@link WorldChange} with the given values.
   * 
   * @param world The {@link World} that this {@link WorldChange} affects.
   * @param loc The {@link Coordinate} of the {@link Tile} to change.
   * @param data The data entries needed for the change.
   * @param type The type of the change.
   * @param update The update to apply this {@link WorldChange} on.
   */
  public WorldChange(World world, Coordinate loc, Object[] data, ChangeType type, long update) {
    this.world = world;
    this.location = loc;
    this.data = data;
    this.type = type;
    this.update = update;
  }
  
  /**
   * Creates a {@link WorldChange} for either damaging a {@link Tile} or setting
   * its damage value.
   * 
   * @param world The {@link World} that this {@link WorldChange} affects.
   * @param loc The {@link Coordinate} of the {@link Tile} to change.
   * @param amount The amount of damage to apply, or the value to set the damage
   *  depending of which type of change this is.
   * @param set If true this {@link WorldChange} will set the damage value of the
   *  {@link Tile} to the given amount.  Otherwise this {@link WorldChange} will
   *  apply the given amount as damage.
   * @param update The update when this {@link WorldChange} should be applied.
   */
  public WorldChange(World world, Coordinate loc, long amount, boolean set, long update) {
    this.world = world;
    this.location = loc;
    this.data = new Object[]{amount};
    this.type = set ? ChangeType.SetDamage : ChangeType.Damage;
    this.update = update;
  }
  
  /**
   * Creates a {@link WorldChange} for changing a {@link TileEntry}.  This type
   * of change will also reset the damage value of the {@link Tile} to 0.
   * 
   * @param world The {@link World} this {@link WorldChange} affects.
   * @param loc The {@link Coordinate} of the {@link Tile} to change.
   * @param entry The {@link TileEntry} to set the {@link Tile} to.
   * @param update The update when this {@link WorldChange} should be applied.
   */
  public WorldChange(World world, Coordinate loc, TileEntry entry, long update) {
    this.world = world;
    this.location = loc;
    this.data = new Object[] {entry};
    this.type = ChangeType.ChangeType;
    this.update = update;
  }
  
  /**
   * Checks whether this {@link WorldChange} will override the given one if they
   * both applied.  If this {@link WorldChange} is compatible with the given one
   * then this method will return false.
   * 
   * @param change The {@link WorldChange} to check.
   * @return Whether this {@link WorldChange} overrides the given {@link WorldChange}
   */
  public boolean doesOverride(WorldChange change) {
    if(type == ChangeType.ChangeType)
      return true;
    if(change.type == ChangeType.ChangeType)
      return false;
    if(type == ChangeType.SetDamage)
      return true;
    return false;
  }
  
  /**
   * Applies this {@link WorldChange} using the given {@link WorldMutator}.
   * 
   * @param mutator The {@link WorldMutator} to use to apply this change.
   */
  public void applyChange(WorldMutator mutator) {
    switch(type) {
      case SetDamage:
        mutator.setDamageValue((Long)data[0], location);
        break;
      case Damage:
        mutator.damageTile((Long)data[0], location);
        break;
      case ChangeType:
        mutator.setTileEntry((TileEntry)data[0], location);
        break;
    }
  }

  /**
   * Enumerates the various types of {@link WorldChange}s.
   */
  public static enum ChangeType {
    /**
     * Sets the damage value of the {@link Tile} to the value in first data index.
     */
    SetDamage(1),
    /**
     * Subtracts the value in the first data index from the {@link Tile}'s damage
     * value.
     */
    Damage(0),
    /**
     * Changes the {@link TileEntry} for the {@link Tile}.  This will also reset
     * the damage value.
     */
    ChangeType(2);
    
    public final int priority;

    private ChangeType(int priority) {
      this.priority = priority;
    }
  }
}
