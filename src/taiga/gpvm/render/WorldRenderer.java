/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm.render;

import gpvm.util.geometry.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.opengl.GL11;
import taiga.code.graphics.Renderable;
import taiga.gpvm.map.World;

/**
 * Handles the rendering of a GameMap.
 * 
 * @author russell
 */
public final class WorldRenderer extends Renderable {
  public WorldRenderer(World world) {
    super(world.name);
    
    map = world;
    rendgrid = false;
    drawlist = new ArrayList<>();
    renderers = new HashMap<>();
  }
  
  public void renderGrid(boolean grid) {
    rendgrid = grid;
  }
  
  public void setCamera(Camera cam) {
    camera = cam;
    //setup the matrices
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    
    for(RegionRenderer reg : drawlist) {
      Coordinate loc = reg.getLocation();
      GL11.glLoadIdentity();
      GL11.glTranslatef(loc.x, loc.y, loc.z);
      
      //eg.render(rendgrid);
    }
  }
  
  private static int drawdistance = 4;
  
  private World map;
  private Camera camera;
  private ArrayList<RegionRenderer> drawlist;
  private HashMap<Coordinate, RegionRenderer> renderers;
  private Class<? extends RenderingBatch> renderclass;
  private boolean rendgrid;

  @Override
  protected void updateSelf() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected void renderSelf(int pass) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  private enum Quad {
    PxPy,
    PxNy,
    NxPy,
    NxNy
  }
}
