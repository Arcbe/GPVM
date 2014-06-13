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
   * @return The short value of the bytes.
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
}
