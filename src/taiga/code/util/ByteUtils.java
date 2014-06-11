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
    byte[] result = new byte[4];
    
    result[3] = (byte) (i >> 24);
    result[2] = (byte) (i >> 16);
    result[1] = (byte) (i >> 8);
    result[0] = (byte) (i);
    
    return result;
  }
  
  /**
   * Converts the given short into an array of bytes in network byte order.
   * 
   * @param s The short to convert.
   * @return An array of bytes equivalent to the given integer.
   */
  public static byte[] toBytes(short s) {
    byte[] result = new byte[2];
    
    result[1] = (byte) (s >> 8);
    result[0] = (byte) (s);
    
    return result;
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
  public final static short toShort(byte[] b, final int offset) {
    short result;
    
    result = b[offset];
    result |= (short)(b[offset + 1]) << 8;
    
    return result;
  }
}
