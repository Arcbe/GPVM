/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm.render;

import gpvm.util.geometry.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.lwjgl.opengl.GL11;
import taiga.code.graphics.Renderable;
import taiga.gpvm.map.Region;
import taiga.gpvm.map.World;
import taiga.gpvm.map.WorldListener;

/**
 * Handles the rendering of a GameMap.
 * 
 * @author russell
 */
public final class WorldRenderer extends Renderable implements WorldListener {
  public WorldRenderer(World world) {
    super(world.name);
    
    map = world;
    rendgrid = false;
    renderers = new ArrayList<>();
    
    world.addListener(this);
  }
  
  public void renderGrid(boolean grid) {
    rendgrid = grid;
  }
  
  public void setCamera(Camera cam) {
    camera = cam;
    //setup the matrices
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    
//    for(RegionRenderer reg : drawlist) {
//      Coordinate loc = reg.getLocation();
//      GL11.glLoadIdentity();
//      GL11.glTranslatef(loc.x, loc.y, loc.z);
//      
//      //eg.render(rendgrid);
//    }
  }
  
  private static int drawdistance = 4;
  
  private World map;
  private Camera camera;
  private List<RegionRenderer> renderers;
  private boolean rendgrid;

  @Override
  protected void updateSelf() {
  }

  @Override
  protected void renderSelf(int pass) {
  }

  @Override
  public void regionLoaded(Region reg) {
    addChild(new RegionRenderer(reg));
  }

  @Override
  public void regionUnloaded(Region reg) {
  }
}
