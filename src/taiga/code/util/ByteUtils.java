package taiga.code.util;

/**
 * Various conveniences methods for dealing with bytes including encoding
 * primitives into byte arrays.
 * 
 * @author russell
 */
public class ByteUtils {
  /**
   * Converts the given integer into an array of bytes in network byte order.
   * 
   * @param i The integer to convert.
   * @return An array of bytes equivalent to the given integer.
   */
  public static byte[] toBytes(int i) {
    return toBytes(i, 0, new byte[4]);
  }
  
  /**
   * Converts the given integer into an array of bytes in network byte order and
   * places it into the given array.
   * 
   * @param i The integer to convert.
   * @param offset The offset for where to put the bytes in the returned array.
   * @param out The array to place the bytes into.
   * @return A reference to the given byte array.
   */
  public static byte[] toBytes(int i, int offset, byte[] out) {
    
    out[offset + 3] = (byte) (i >> 24);
    out[offset + 2] = (byte) (i >> 16);
    out[offset + 1] = (byte) (i >> 8);
    out[offset] = (byte) (i);
    
    return out;
  }
  
  /**
   * Converts the given long into an array of bytes in network byte order.
   * 
   * @param l The long to convert.
   * @return An array of bytes equivalent to the given integer.
   */
  public static byte[] toBytes(long l) {
    return toBytes(l, 0, new byte[8]);
  }
  
  /**
   * Converts the given long into an array of bytes in network byte order and
   * places it into the given array.
   * 
   * @param l The long to convert.
   * @param offset The offset for where to put the bytes in the returned array.
   * @param out The array to place the bytes into.
   * @return A reference to the given byte array.
   */
  public static byte[] toBytes(long l, int offset, byte[] out) {
    
    out[offset + 7] = (byte) (l >> 56);
    out[offset + 6] = (byte) (l >> 48);
    out[offset + 5] = (byte) (l >> 40);
    out[offset + 4] = (byte) (l >> 32);
    out[offset + 3] = (byte) (l >> 24);
    out[offset + 2] = (byte) (l >> 16);
    out[offset + 1] = (byte) (l >> 8);
    out[offset] = (byte) (l);
    
    return out;
  }
  
  /**
   * Converts the given short into an array of bytes in network byte order.
   * 
   * @param s The short to convert.
   * @return An array of bytes equivalent to the given integer.
   */
  public static byte[] toBytes(short s) {
    return toBytes(s, 0, new byte[2]);
  }
  
  /**
   * Converts the given short into an array of bytes in network byte order and
   * places them into the given array.
   * 
   * @param s The short to convert.
   * @param offset The offset into the given array to place the bytes.
   * @param out The array to place the bytes.
   * @return A reference to the given byte array.
   */
  public static byte[] toBytes(short s, int offset, byte[] out) {
    
    out[offset + 1] = (byte) (s >> 8);
    out[offset] = (byte) (s);
    
    return out;
  }
  
  /**
   * Converts the bytes in an array into the corresponding short value.  The 
   * bytes need to be in the network byte order.
   * 
   * @param b The array with the bytes to convert
   * @return The short value of the bytes.
   */
  public final static short toShort(final byte[] b) {
    return toShort(b, 0);
  }
  
  /**
   * Converts the bytes in an array into the corresponding short value.  The 
   * bytes need to be in the network byte order.
   * 
   * @param b The array with the bytes to convert
   * @param offset The offset into the array for the bytes to convert.
   * @return The short value of the bytes.
   */
  public final static short toShort(final byte[] b, final int offset) {
    short result;
    
    result = b[offset];
    result |= (short)(b[offset + 1]) << 8;
    
    return result;
  }
  
  /**
   * Converts the bytes in an array into the corresponding integer value.  The 
   * bytes need to be in the network byte order.
   * 
   * @param b The array with the bytes to convert
   * @return The integer value of the bytes.
   */
  public final static int toInteger(byte[] b) {
    return toInteger(b, 0);
  }
  
  /**
   * Converts the bytes in an array into the corresponding integer value.  The 
   * bytes need to be in the network byte order.
   * 
   * @param b The array with the bytes to convert
   * @param offset The offset into the array for the bytes to convert.
   * @return The integer value of the bytes.
   */
  public final static int toInteger(final byte[] b, final int offset) {
    int result;
    
    result = b[offset];
    result |= (int)(b[offset + 1]) << 8;
    result |= (int)(b[offset + 2]) << 16;
    result |= (int)(b[offset + 3]) << 24;
    
    return result;
  }
  
  /**
   * Converts the bytes in an array into the corresponding long value.  The 
   * bytes need to be in the network byte order.
   * 
   * @param b The array with the bytes to convert
   * @return The long value of the bytes.
   */
  public final static long toLong(final byte[] b) {
    return toLong(b, 0);
  }
  
  /**
   * Converts the bytes in an array into the corresponding long value.  The 
   * bytes need to be in the network byte order.
   * 
   * @param b The array with the bytes to convert
   * @param offset The offset into the array for the bytes to convert.
   * @return The long value of the bytes.
   */
  public final static int toLong(final byte[] b, final int offset) {
    int result;
    
    result = b[offset];
    result |= (long)(b[offset + 1]) << 8;
    result |= (long)(b[offset + 2]) << 16;
    result |= (long)(b[offset + 3]) << 24;
    result |= (long)(b[offset + 4]) << 32;
    result |= (long)(b[offset + 5]) << 40;
    result |= (long)(b[offset + 6]) << 48;
    result |= (long)(b[offset + 7]) << 56;
    
    return result;
  }
}
