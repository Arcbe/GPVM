/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package placeholder.render.renderers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import org.lwjgl.opengl.GL11;
import placeholder.render.RawBatch;
import placeholder.render.RenderInfo;
import placeholder.render.TileInfo;
import placeholder.render.TileRenderer;
import placeholder.render.Vertex;
import placeholder.render.vertices.ColorVertex;
import placeholder.util.geometry.Coordinate;

import static placeholder.util.geometry.Direction.*;

/**
 * A simple tile renderer that renders each tile as a solid color.
 * 
 * @author russell
 */
public class ColorRenderer extends TileRenderer{
  @Override
  public RawBatch[] batchTiles(Collection<TileInfo> info) {
    RawBatch batch = new RawBatch();
    batch.rendermode = GL11.GL_QUADS;
    
    ArrayList<ColorVertex> vertices = new ArrayList<>();
    
    for(TileInfo tile : info) {
      //get the color for the tile.
      int color;
      Coordinate loc = tile.relativepos;
      if(tile.info == null) {
        color = Color.magenta.getRGB();
      } else {
        assert tile.info instanceof ColorInfo;
        color = ((ColorInfo)tile.info).color;
      }
      
      //add the east face
      if(tile.adjacenttiles[East.getIndex()] == null) {
        vertices.add(new ColorVertex(loc.x, loc.y, loc.z, color));
        vertices.add(new ColorVertex(loc.x, loc.y + 1, loc.z, color));
        vertices.add(new ColorVertex(loc.x, loc.y + 1, loc.z + 1, color));
        vertices.add(new ColorVertex(loc.x, loc.y, loc.z + 1, color));
      }
      
      //west face
      if(tile.adjacenttiles[West.getIndex()] == null) {
        vertices.add(new ColorVertex(loc.x + 1, loc.y, loc.z, color));
        vertices.add(new ColorVertex(loc.x + 1, loc.y + 1, loc.z, color));
        vertices.add(new ColorVertex(loc.x + 1, loc.y + 1, loc.z + 1, color));
        vertices.add(new ColorVertex(loc.x + 1, loc.y, loc.z + 1, color));
      }
      
      //north face
      if(tile.adjacenttiles[North.getIndex()] == null) {
        vertices.add(new ColorVertex(loc.x, loc.y + 1, loc.z, color));
        vertices.add(new ColorVertex(loc.x + 1, loc.y + 1, loc.z, color));
        vertices.add(new ColorVertex(loc.x + 1, loc.y + 1, loc.z + 1, color));
        vertices.add(new ColorVertex(loc.x, loc.y + 1, loc.z + 1, color));
      }
      
      //south face
      if(tile.adjacenttiles[South.getIndex()] == null) {
        vertices.add(new ColorVertex(loc.x, loc.y, loc.z, color));
        vertices.add(new ColorVertex(loc.x + 1, loc.y, loc.z, color));
        vertices.add(new ColorVertex(loc.x + 1, loc.y, loc.z + 1, color));
        vertices.add(new ColorVertex(loc.x, loc.y, loc.z + 1, color));
      }
      
      //Top face
      if(tile.adjacenttiles[Up.getIndex()] == null) {
        vertices.add(new ColorVertex(loc.x, loc.y, loc.z + 1, color));
        vertices.add(new ColorVertex(loc.x, loc.y + 1, loc.z + 1, color));
        vertices.add(new ColorVertex(loc.x + 1, loc.y + 1, loc.z + 1, color));
        vertices.add(new ColorVertex(loc.x + 1, loc.y, loc.z + 1, color));
      }
      
      //Top face
      if(tile.adjacenttiles[Up.getIndex()] == null) {
        vertices.add(new ColorVertex(loc.x, loc.y, loc.z, color));
        vertices.add(new ColorVertex(loc.x, loc.y + 1, loc.z, color));
        vertices.add(new ColorVertex(loc.x + 1, loc.y + 1, loc.z, color));
        vertices.add(new ColorVertex(loc.x + 1, loc.y, loc.z, color));
      }
    }
    
    Vertex[] verts = new Vertex[vertices.size()];
    batch.vertices = vertices.toArray(verts);
    return new RawBatch[]{batch};
  }

  @Override
  public Class<? extends RenderInfo> getRenderInfo() {
    return ColorInfo.class;
  }
}
