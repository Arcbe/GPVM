/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package placeholder.util.geometry;

/**
 * Indicates a direction in the map.  Positive and negative x are west and 
 * east, positive and negative y are north and south, and finally positive and
 * negative z are up and down respectively.
 * 
 * @author russell
 */
public enum Direction {
  /**
   * negative x direction
   */
  East(0),
  /**
   * positive x direction
   */
  West(1),
  /**
   * positive y direction
   */
  North(2),
  /**
   * negative y direction
   */
  South(3),
  /**
   * positive z direction
   */
  Up(4),
  /**
   * negative z direction
   */
  Down(5);
  
  private int value;

  private Direction(int value) {
    this.value = value;
  }
  
  public int getIndex() {
    return value;
  }
}
