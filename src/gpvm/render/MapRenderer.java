/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.render;

import gpvm.map.GameMap;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author russell
 */
class MapRenderer {
  public MapRenderer(Class<? extends RenderingBatch> renderer) {
    rendgrid = false;
    setRenderer(renderer);
  }
  
  public void setMap(GameMap map) {
    
  }
  
  public void setRenderer(Class<? extends RenderingBatch> renderer) {
    renderclass = renderer;
  }
  
  public void renderGrid(boolean grid) {
    rendgrid = grid;
  }
  
  public void render(Camera cam) {
    //if(map == null) return;
    
    //setup the matrices
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glLoadIdentity();
    
    RegionRenderer reg = new RegionRenderer();
    reg.render(rendgrid);
  }
  
  private GameMap map;
  private Class<? extends RenderingBatch> renderclass;
  private boolean rendgrid;
}
