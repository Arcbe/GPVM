///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package gpvm.util.geometry;
//
///**
// *
// * @author russell
// */
//public final class Quaternion {
//  public Quaternion() {
//    x = 0;
//    y = 0;
//    z = 0;
//    w = 1;
//  }
//  
//  public Quaternion(float x, float y, float z, float w) {
//    this.x = x;
//    this.y = y;
//    this.z = z;
//    this.w = w;
//  }
//  
//  public Quaternion mult(Quaternion other) {
//    return mult(other, this);
//  }
//  
//  public Quaternion mult(Quaternion other, Quaternion out) {
//    float nw = w * other.w - x * other.x - y * other.y - z * other.z;
//    float nx = w * other.x + x * other.w + y * other.z - z * other.y;
//    float ny = w * other.y + y * other.w + z * other.x - x * other.z;
//    float nz = w * other.z + z * other.w + x * other.y - y * other.x;
//    
//    out.x = nx;
//    out.y = ny;
//    out.z = nz;
//    out.w = nw;
//    
//    return out;
//  }
//  
//  public Vector getDirection() {
//    return getDirection(new Vector());
//  }
//  
//  public Vector getDirection(Vector out) {
//    out.x = w * w + x * x - y * y - z * z;
//    out.y = 2 * x * y - 2 * w * z;
//    out.z = 2 * x * z + 2 * w * y;
//    
//    return out;
//  }
//  
//  private float x;
//  private float y;
//  private float z;
//  private float w;
//  
//  public static Quaternion rotateX(float angle) {
//    return rotateX(angle, new Quaternion());
//  }
//  
//  public static Quaternion rotateX(float angle, Quaternion out) {
//    angle /= 2;
//    
//    out.w = (float) Math.cos(angle);
//    out.x = (float) Math.sin(angle);
//    out.y = 0;
//    out.z = 0;
//    
//    return out;
//  }
//  
//  public static Quaternion rotateY(float angle) {
//    return rotateY(angle, new Quaternion());
//  }
//  
//  public static Quaternion rotateY(float angle, Quaternion out) {
//    angle /= 2;
//    
//    out.w = (float) Math.cos(angle);
//    out.x = 0;
//    out.y = (float) Math.sin(angle);
//    out.z = 0;
//    
//    return out;
//  }
//  
//  public static Quaternion rotateZ(float angle) {
//    return rotateZ(angle, new Quaternion());
//  }
//  
//  public static Quaternion rotateZ(float angle, Quaternion out) {
//    angle/= 2;
//    
//    out.w = (float) Math.cos(angle);
//    out.x = 0;
//    out.y = 0;
//    out.z = (float) Math.sin(angle);
//    
//    return out;
//  }
//}
