/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.render;

import gpvm.map.Region;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author russell
 */
public class RegionRendererTest {
  
  public RegionRendererTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of render method, of class RegionRenderer.
   */
  @Test
  public void testRender() {
    System.out.println("render");
    boolean grid = false;
    RegionRenderer instance = null;
    instance.render(grid);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of update method, of class RegionRenderer.
   */
  @Test
  public void testUpdate() {
    System.out.println("update");
    RegionRenderer instance = null;
    instance.update();
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of regionUnloading method, of class RegionRenderer.
   */
  @Test
  public void testRegionUnloading() {
    System.out.println("regionUnloading");
    Region reg = null;
    RegionRenderer instance = null;
    instance.regionUnloading(reg);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of regionUpdated method, of class RegionRenderer.
   */
  @Test
  public void testRegionUpdated() {
    System.out.println("regionUpdated");
    Region reg = null;
    RegionRenderer instance = null;
    instance.regionUpdated(reg);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setRenderingBatch method, of class RegionRenderer.
   */
  @Test
  public void testSetRenderingBatch() {
    System.out.println("setRenderingBatch");
    Class<? extends RenderingBatch> renderclass = null;
    RegionRenderer instance = null;
    instance.setRenderingBatch(renderclass);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
}