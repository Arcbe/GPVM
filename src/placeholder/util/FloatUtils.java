/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package placeholder.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Contains some methods to perform various operation on float primitive
 * data types.
 * 
 * @author russell
 */
public final class FloatUtils {
  /**
   * Converts a float into an array of bytes and stores them in the 
   * given array.  The array must be length at least offset + 5.
   * 
   * @param in The float to convert
   * @param out The array to store the bytes in
   * @param offset The index in the array to start storing the bytes
   * @return The given array with the float bytes added.
   */
  public static byte[] floatToBytes(float in, byte[] out, int offset, ByteOrder ord) {
    assert out.length > offset + 4;
    
    ByteBuffer buf = ByteBuffer.wrap(out);
    buf.order(ord);
    buf.position(offset);
    buf.putFloat(in);
    
    return out;
  }
  
  /**
   * Converts an array of bytes in a float.  The array must have a length 
   * at least offset + 5.
   * 
   * @param bytes The array of bytes to convert.
   * @param offset The offset to the bytes in the array
   * @return The float represented by the bytes.
   */
  public static float bytesToFloat(byte[] bytes, int offset, ByteOrder ord) {
    assert bytes.length > offset + 4;
    
    ByteBuffer buf = ByteBuffer.wrap(bytes);
    buf.order(ord);
    buf.position(offset);
    
    return buf.getFloat();
  }
}
