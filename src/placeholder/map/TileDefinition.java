package placeholder.map;

import placeholder.render.TileRenderer;

/**
 *
 * @author russell
 */
public class TileDefinition {
  public final String name;
  public final String canonname;
  public final byte metadata;
  public final boolean opaque;

  public TileDefinition(String name, String canonname, byte metadata, boolean opaque) {
    this.name = name;
    this.canonname = canonname;
    this.metadata = metadata;
    this.opaque = opaque;
  }
}
