/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.render;

import gpvm.map.GameMap;
import gpvm.map.Region;
import gpvm.util.geometry.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 * Handles the rendering of a GameMap.
 * 
 * @author russell
 */
public final class MapRenderer {
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
      rend.setRenderBatch(renderclass);
    }
  }
  
  public void renderGrid(boolean grid) {
    rendgrid = grid;
  }
  
  public void update(Camera cam) {
    assert map != null;
    
    drawlist.clear();
    
    if(cam.direction.x > 0) {
      addQuad(Quad.PxNy, cam.position);
      addQuad(Quad.PxPy, cam.position);
      
      if(cam.direction.y > 0) addQuad(Quad.NxPy, cam.position);
      else addQuad(Quad.NxNy, cam.position);
    } else {
      addQuad(Quad.NxNy, cam.position);
      addQuad(Quad.NxPy, cam.position);
      
      if(cam.direction.y > 0) addQuad(Quad.PxPy, cam.position);
      else addQuad(Quad.PxNy, cam.position);
    }
  }
  
  public void render(Camera cam) {
    if(map == null) return;
    
    //setup the matrices
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    
    for(RegionRenderer reg : drawlist) {
      Coordinate loc = reg.getLocation();
      GL11.glLoadIdentity();
      GL11.glScalef(2, 2,2);
      GL11.glTranslatef(loc.x, loc.y, loc.z);
      
      reg.render(rendgrid);
    }
  }
  
  private static int drawdistance = 2;
  
  private GameMap map;
  private ArrayList<RegionRenderer> drawlist;
  private HashMap<Coordinate, RegionRenderer> renderers;
  private Class<? extends RenderingBatch> renderclass;
  private boolean rendgrid;
  
  private void addRenderer(Coordinate location) {
    if(renderers.containsKey(location)) {
      drawlist.add(renderers.get(location));
    } else {
      RegionRenderer rend = new RegionRenderer(map.getRegion(location), map);
      rend.setRenderBatch(renderclass);
      renderers.put(location, rend);
      drawlist.add(rend);
    }
  }
  
  private void addQuad(Quad q, Vector3f origin) {
    int mi = (q == Quad.PxNy || q == Quad.PxPy ? 1 : -1);
    int mj = (q == Quad.PxPy || q == Quad.NxPy ? 1 : -1);
    
    for(int i = 0; i < drawdistance; i++) {
      for(int j = 0; j < drawdistance; j++) {
        for(int k = 0; k < drawdistance; k++) {
          int x = (int)origin.x + mi * i * Region.REGION_SIZE;
          int y = (int)origin.y + mj * j * Region.REGION_SIZE;
          int z = (int)origin.z + k * Region.REGION_SIZE;
          Coordinate loc = new Coordinate(x, y, z);
          
          addRenderer(loc);
        }
      }
    }
  }
  
  private enum Quad {
    PxPy,
    PxNy,
    NxPy,
    NxNy
  }
}
