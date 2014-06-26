//package tagia.code.geom;
//
//import java.util.logging.Logger;
//import org.lwjgl.util.vector.Matrix3f;
//import org.lwjgl.util.vector.ReadableVector3f;
//import org.lwjgl.util.vector.Vector3f;
//
///**
// * Represents a volume of space bounded by an ellipsoid, a deformed sphere.
// * The {@link Ellipsoid} is defined by a center point and {@link Matrix3f}.
// * The {@link Matrix3f} defines a linear transformation used to deform a unit
// * sphere in the desired {@link Ellipsoid}.
// * 
// * @author russell
// */
//public class Ellipsoid implements BoundingVolume {
//
//  /**
//   * Creates a new {@link Ellipsoid} of a sphere centered at the origin.
//   */
//  public Ellipsoid() {
//    center = new Vector3f();
//    deformation = new Matrix3f();
//  }
//  
//  /**
//   * Creates a new {@link Ellipsoid} of a sphere centered at the given {@link Vector3f},
//   * and with the given size.
//   * 
//   * @param center The center point for the sphere.
//   * @param size The size of the sphere.
//   */
//  public Ellipsoid(Vector3f center, float size) {
//    this.center = center;
//    deformation = new Matrix3f();
//    
//    deformation.m00 = size;
//    deformation.m11 = size;
//    deformation.m22 = size;
//  }
//  
//  /**
//   * Creates a new {@link Ellipsoid} centered at the given point and deformed
//   * on the x, y, and z axis by the given amount.
//   * 
//   * @param center The center point for the {@link Ellipsoid}.
//   * @param x The amount of deformation on the x axis.
//   * @param y The amount of deformation on the y axis.
//   * @param z The amount of deformation on the z axis.
//   */
//  public Ellipsoid(Vector3f center, float x, float y, float z) {
//    this.center = center;
//    deformation = new Matrix3f();
//    
//    deformation.m00 = x;
//    deformation.m11 = y;
//    deformation.m22 = z;
//  }
//  
//  /**
//   * Creates a new {@link Ellipsoid} centered at the given point and using
//   * the given {@link Matrix3f} to deform itself.
//   * 
//   * @param center The center point for the {@link Ellipsoid}
//   * @param def The linear deformation of a sphere used to create this {@link Ellipsoid}.
//   */
//  public Ellipsoid(Vector3f center, Matrix3f def) {
//    this.center = center;
//    this.deformation = def;
//  }
//  
//  /**
//   * Returns the center of this {@link Ellipsoid}.
//   * 
//   * @return The center of this {@link Ellipsoid}.
//   */
//  public Vector3f getCenter() {
//    return center;
//  }
//  
//  /**
//   * Sets the center to the given {@link Vector3f}.
//   * 
//   * @param center The new center for this {@link Ellipsoid}.
//   */
//  public void setCenter(ReadableVector3f center) {
//    this.center.set(center);
//  }
//  
//  /**
//   * Sets the center to the given point.
//   * 
//   * @param x The x coordinate of the center.
//   * @param y The y coordinate of the center.
//   * @param z The z coordinate of the center.
//   */
//  public void setCenter(float x, float y, float z) {
//    center.set(x, y, z);
//  }
//  
//  /**
//   * Translates the center of this {@link Ellipsoid} by the given amount.
//   * 
//   * @param trans The amount to translate the center by.
//   */
//  public void translate(ReadableVector3f trans) {
//    center.translate(trans.getX(), trans.getY(), trans.getZ());
//  }
//  
//  /**
//   * Translates the center of this {@link Ellipsoid} by the given amount.
//   * 
//   * @param x The amount to translate by in the x direction.
//   * @param y The amount to translate by in the y direction.
//   * @param z The amount to translate by in the z direction.
//   */
//  public void translate(float x, float y, float z) {
//    center.translate(x, y, z);
//  }
//  
//  /**
//   * Multiplies the deformation {@link Matrix3f} of this {@link Ellipsoid}
//   * by the given {@link Matrix3f}.  The given {@link Matrix3f} if used on the
//   * left hand side of the multiplication.
//   * 
//   * @param trans The transformation {@link Matrix3f} to apply.
//   */
//  public void transform(Matrix3f trans) {
//    Matrix3f.mul(trans, deformation, deformation);
//  }
//  
//  /**
//   * Sets the deformation {@link Matrix3f} for this {@link Ellipsoid} to
//   * the values of the given {@link Matrix3f}.
//   * 
//   * @param def The new {@link Matrix3f} for the deformation of this {@link Ellipsoid}.
//   */
//  public void setDeformation(Matrix3f def) {
//    Matrix3f.load(def, deformation);
//  }
//  
//  /**
//   * Returns the {@link Matrix3f} used to deform this {@link Ellipsoid}.
//   * 
//   * @return The deformation matrix.
//   */
//  public Matrix3f getDeformation() {
//    return deformation;
//  }
//
//  @Override
//  public boolean collides(Ellipsoid ell) {
//    createIndef();
//    ell.createIndef();
//    
//    //get the vector between the center of the two ellipsoids
//    Vector3f dist = new Vector3f();
//    Vector3f.sub(ell.center, center, dist);
//    
//    //transform teh dist vector to find the length relative to the edge of the ellipsoid.
//    Vector3f temp = new Vector3f();
//    Matrix3f.transform(indef, dist, temp);
//    
//    //get rid of the part of the vector inside this ellipsoid
//    float len = temp.length();
//    dist.scale((1f - len) / len);
//    dist.negate();
//    
//    Matrix3f.transform(ell.indef, dist, dist);
//    
//    return dist.lengthSquared() <= 1;
//  }
//  
//  private void createIndef() {
//    if(indef == null || prev == null) {
//      prev = Matrix3f.load(deformation, prev);
//      indef = Matrix3f.invert(prev, indef);
//      
//      return;
//    }
//    
//    float diff = prev.m00 - deformation.m00;
//    diff += prev.m10 - deformation.m10;
//    diff += prev.m20 - deformation.m20;
//    diff += prev.m01 - deformation.m01;
//    diff += prev.m11 - deformation.m11;
//    diff += prev.m21 - deformation.m21;
//    diff += prev.m02 - deformation.m02;
//    diff += prev.m12 - deformation.m12;
//    diff += prev.m22 - deformation.m22;
//    if(diff * diff > .001) {
//      prev = Matrix3f.load(deformation, prev);
//      indef = Matrix3f.invert(prev, indef);
//    }
//  }
//  
//  private final Vector3f center;
//  
//  private final Matrix3f deformation;
//  private transient Matrix3f prev;
//  private transient Matrix3f indef;
//
//  private static final String locprefix = Ellipsoid.class.getName().toLowerCase();
//
//  private static final Logger log = Logger.getLogger(locprefix,
//    System.getProperty("taiga.code.logging.text"));
//}
