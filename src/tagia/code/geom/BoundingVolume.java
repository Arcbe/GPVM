package tagia.code.geom;

/**
 * Interface for class that represent volumes in 3d space and can calculate
 * whether the intersect.
 * 
 * @author russell
 */
public interface BoundingVolume {
  /**
   * Calculates whether this {@link BoundingVolume} intersects with the
   * given {@link Ellipsoid}.
   * 
   * @param ell The {@link Ellipsoid} to check collision with.
   * @return Whether this {@link BoundingVolume} collides with the {@link Ellipsoid}.
   */
  public boolean collides(Ellipsoid ell);
}
