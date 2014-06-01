/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm.render;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import taiga.code.graphics.Camera;
import taiga.code.graphics.Renderable;
import taiga.code.util.geom.Coordinate;
import taiga.gpvm.map.Region;
import taiga.gpvm.map.World;
import taiga.gpvm.map.WorldListener;

/**
 * Handles the rendering of a {@link World}.
 * 
 * @author russell
 */
public final class WorldRenderer extends Renderable implements WorldListener {
  /**
   * Creates a new {@link WorldRenderer} that will render the given {@link World}.
   * 
   * @param world The {@link World} to render.
   */
  public WorldRenderer(World world) {
    super(world.name);
    
    map = world;
    renderers = new ArrayList<>();
    
    world.addListener(this);
  }
  
  public void setCamera(Camera cam) {
    camera = cam;
  }
  
  private static int drawdistance = 4;
  
  private World map;
  private List<RegionRenderer> renderers;
  private Camera camera;

  @Override
  protected void updateSelf() {
    for(RegionRenderer reg : renderers)
      reg.update();
  }

  @Override
  protected void renderSelf(int pass) {
    if(camera == null) return;
    
    camera.setupProjectioMatrix();
    
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glPushMatrix();
    
    for(RegionRenderer reg : renderers) {
      Coordinate coor = reg.getLocation();
      GL11.glLoadIdentity();
      GL11.glTranslatef(coor.x, coor.y, coor.z);
      
      reg.render(pass);
    }
    
    GL11.glPopMatrix();
  }

  @Override
  public void regionLoaded(Region reg) {
    RegionRenderer region = new RegionRenderer(reg);
    renderers.add(region);
    addChild(region);
  }

  @Override
  public void regionUnloaded(Region reg) {
  }
}
