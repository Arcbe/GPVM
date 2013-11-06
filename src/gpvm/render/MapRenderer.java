/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.render;

import gpvm.map.GameMap;
import gpvm.util.geometry.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author russell
 */
final class MapRenderer {
  public MapRenderer(Class<? extends RenderingBatch> renderer) {
    rendgrid = false;
    drawlist = new ArrayList<>();
    renderers = new HashMap<>();
    setRenderer(renderer);
  }
  
  public void setMap(GameMap map) {
    this.map = map;
    
    //none of the renderers are correct if the map changes.
    renderers.clear();
  }
  
  public void setRenderer(Class<? extends RenderingBatch> renderer) {
    renderclass = renderer;
    
    for(RegionRenderer rend : renderers.values()) {
      rend.setRenderingBatch(renderclass);
    }
  }
  
  public void renderGrid(boolean grid) {
    rendgrid = grid;
  }
  
  public void update(Camera cam) {
    drawlist.clear();
    drawlist.add(new RegionRenderer(map.getRegion(new Coordinate()),map));
  }
  
  public void render(Camera cam) {
    if(map == null) return;
    
    //setup the matrices
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glLoadIdentity();
    
    for(RegionRenderer reg : drawlist)
      reg.render(rendgrid);
  }
  
  private GameMap map;
  private ArrayList<RegionRenderer> drawlist;
  private HashMap<Coordinate, RegionRenderer> renderers;
  private Class<? extends RenderingBatch> renderclass;
  private boolean rendgrid;
}
