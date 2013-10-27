package placeholder.map;

/**
 *
 * @author russell
 */
public class TileDefinition {
  public final String name;
  public final String canonname;
  public final byte metadata;

  public TileDefinition(String name, String canonname, byte metadata) {
    assert metadata <= 48;
    this.name = name;
    this.canonname = canonname;
    this.metadata = metadata;
  }
}
