/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Contains various methods to perform operation on integer primitive
 * data types.
 * 
 * @author russell
 */
public class IntUtils {
   /**
   * Converts a int into an array of bytes and stores them in the 
   * given array.  The array must be length at least offset + 5.
   * 
   * @param in The int to convert
   * @param out The array to store the bytes in
   * @param offset The index in the array to start storing the bytes
   * @param ord The byte order to use for encoding the int.
   * @return The given array with the float bytes added.
   */
  public static byte[] intToBytes(int in, byte[] out, int offset, ByteOrder ord) {
    assert out.length > offset + 4;
    
    ByteBuffer buf = ByteBuffer.wrap(out);
    buf.order(ord);
    buf.position(offset);
    buf.putInt(in);
    
    return out;
  }
  
  /**
   * Converts an array of bytes in a int.  The array must have a length 
   * at least offset + 5.
   * 
   * @param bytes The array of bytes to convert.
   * @param offset The offset to the bytes in the array.
   * @param ord The byte order of the encode int.
   * @return The int represented by the bytes.
   */
  public static int bytesToFloat(byte[] bytes, int offset, ByteOrder ord) {
    assert bytes.length > offset + 4;
    
    ByteBuffer buf = ByteBuffer.wrap(bytes);
    buf.order(ord);
    buf.position(offset);
    
    return buf.getInt();
  }
}
