/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package placeholder.testing;

import com.sun.istack.internal.logging.Logger;
import java.nio.ByteOrder;
import placeholder.util.FloatUtils;

/**
 *
 * @author russell
 */
public class UtilTest {
  public static void test() {
    if(!FloatUtilTest()) {
      Logger.getLogger(UtilTest.class).severe("FloatUtils failed testing.");
    } else {
      Logger.getLogger(UtilTest.class).info("FloatUtils passed testing");
    }
  }
  
  public static boolean FloatUtilTest() {
    byte[] arr = new byte[5];
    arr[0] = 0;
    
    FloatUtils.floatToBytes(.5f, arr, 1, ByteOrder.BIG_ENDIAN);
    
    if(arr[0] != 0 ||
            arr[1] != 63 ||
            arr[2] != 0 ||
            arr[3] != 0 ||
            arr[4] != 0) {
      Logger.getLogger(UtilTest.class).severe("FloatUtil.floatToBytes: failed with {" + 
              arr[0] + "," +
              arr[1] + "," +
              arr[2] + "," +
              arr[3] + "," +
              arr[4] + "}");
      return false;
    }
    
    float result = FloatUtils.bytesToFloat(arr, 1, ByteOrder.BIG_ENDIAN);
    if(result == .5f) return true;
    else {
      Logger.getLogger(UtilTest.class).severe("FloatUtils.bytesToFloat: expected .5 got " + result);
      return false;
    }
  }
}
