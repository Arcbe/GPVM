/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.render;

import taiga.gpvm.util.geom.Coordinate;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.map.Tile;
import taiga.gpvm.registry.RenderingInfo;
import taiga.gpvm.util.geom.Direction;

/**
 *
 * @author russell
 */
public class ColorRenderer implements Renderer {

  @Override
  public void compile(List<TileInfo> tiles) {
    ArrayList<Coordinate> vertices = new ArrayList<>();
    ArrayList<Integer> colors = new ArrayList<>();
    
    for(TileInfo info : tiles) {
      Coordinate corner = info.absposition.getRelativeCoordinate();
      
      //Magenta is the default color for uglyness.
      int color = 0xFFFF00FF;
      if(info.rendentry != null && info.rendentry.info instanceof ColorInfo) {
        color = ((ColorInfo)info.rendentry.info).color;
      }
      
      //south face
      Tile adj = info.adjacent[Direction.South.getIndex()];
      if(adj == null || adj.type == null || !adj.type.opaque) {
        vertices.add(corner);
        vertices.add(corner.add(1, 0, 0, new Coordinate()));
        vertices.add(corner.add(1, 0, 1, new Coordinate()));
        vertices.add(corner.add(0, 0, 1, new Coordinate()));
        colors.add(color);
      }
      
      //north face
      adj = info.adjacent[Direction.North.getIndex()];
      if(adj == null || adj.type == null || !adj.type.opaque) {
        vertices.add(corner.add(0, 1, 0, new Coordinate()));
        vertices.add(corner.add(1, 1, 0, new Coordinate()));
        vertices.add(corner.add(1, 1, 1, new Coordinate()));
        vertices.add(corner.add(0, 1, 1, new Coordinate()));
        colors.add(color);
      }
      
      //west face
      adj = info.adjacent[Direction.West.getIndex()];
      if(adj == null || adj.type == null || !adj.type.opaque) {
        vertices.add(corner);
        vertices.add(corner.add(0, 1, 0, new Coordinate()));
        vertices.add(corner.add(0, 1, 1, new Coordinate()));
        vertices.add(corner.add(0, 0, 1, new Coordinate()));
        colors.add(color);
      }
      
      //east face
      adj = info.adjacent[Direction.East.getIndex()];
      if(adj == null || adj.type == null || !adj.type.opaque) {
        vertices.add(corner.add(1, 0, 0, new Coordinate()));
        vertices.add(corner.add(1, 1, 0, new Coordinate()));
        vertices.add(corner.add(1, 1, 1, new Coordinate()));
        vertices.add(corner.add(1, 0, 1, new Coordinate()));
        colors.add(color);
      }
      
      //bottom face
      adj = info.adjacent[Direction.Down.getIndex()];
      if(adj == null || adj.type == null || !adj.type.opaque) {
        vertices.add(corner);
        vertices.add(corner.add(1, 0, 0, new Coordinate()));
        vertices.add(corner.add(1, 1, 0, new Coordinate()));
        vertices.add(corner.add(0, 1, 0, new Coordinate()));
        colors.add(color);
      }
      
      //top face
      adj = info.adjacent[Direction.Up.getIndex()];
      if(adj == null || adj.type == null || !adj.type.opaque) {
        vertices.add(corner.add(0, 0, 1, new Coordinate()));
        vertices.add(corner.add(1, 0, 1, new Coordinate()));
        vertices.add(corner.add(1, 1, 1, new Coordinate()));
        vertices.add(corner.add(0, 1, 1, new Coordinate()));
        colors.add(color);
      }
    }
    
    verts = BufferUtils.createIntBuffer(3 * vertices.size());
    color = BufferUtils.createByteBuffer(48 * colors.size());
    IntBuffer tcolor = color.asIntBuffer();
    
    for(Coordinate coor : vertices) {
      verts.put(coor.x);
      verts.put(coor.y);
      verts.put(coor.z);
    }
    
    for(Integer col : colors) {
      tcolor.put(col);
      tcolor.put(col);
      tcolor.put(col);
      tcolor.put(col);
    }
    
    verts.flip();
    color.flip();
 }

  @Override
  public void render(int pass) {
    if(verts == null || 
      color == null || 
      pass != HardcodedValues.OPAQUE_WORLD_LAYER) return;
    
    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
    GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
    
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
    
    //There are 3 elements to each vertex
    //stride is zero the vertices are tightly packed.
    GL11.glVertexPointer(3, 0, verts);
    
    //4 elements in each color: alpha, red, green, and blue
    GL11.glColorPointer(4, true, 0, color);
    
    GL11.glDrawArrays(GL11.GL_QUADS, 0, verts.limit() / 3);
  }

  @Override
  public Class<? extends RenderingInfo> getInfoClass() {
    return ColorInfo.class;
  }
  
  private ByteBuffer color;
  private IntBuffer verts;
}
