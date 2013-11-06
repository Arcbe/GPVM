/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.map;

/**
 *
 * @author russell
 */
public interface RegionListener {
  public void regionUnloading(Region reg);
  public void regionUpdated(Region reg);
}
