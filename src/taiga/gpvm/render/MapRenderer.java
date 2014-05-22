/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm.render;

import taiga.gpvm.map.GameMap;
import taiga.gpvm.map.Region;
import gpvm.util.geometry.Coordinate;
import gpvm.util.geometry.Vector;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.opengl.GL11;
import taiga.code.graphics.Renderable;

/**
 * Handles the rendering of a GameMap.
 * 
 * @author russell
 */
public final class MapRenderer extends Renderable {
  public MapRenderer(String name) {
    super(name);
    
    rendgrid = false;
    drawlist = new ArrayList<>();
    renderers = new HashMap<>();
  }
  
  public void setMap(GameMap map) {
    this.map = map;
    
    //none of the renderers are correct if the map changes.
    renderers.clear();
  }
  
  public void renderGrid(boolean grid) {
    rendgrid = grid;
  }
  
  public void render(Camera cam) {
    if(map == null) return;
    
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
  
  private GameMap map;
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
