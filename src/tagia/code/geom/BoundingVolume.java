package tagia.code.geom;


/**
 * An interface for collections of {@link Box}es for use as hit boxes.
 * 
 * @author russell
 */
public interface BoundingVolume {
  /**
   * Returns a {@link AABox} that covers the entire {@link BoundingVolume}.
   * 
   * @return 
   */
  public AABox getBounds();
}
