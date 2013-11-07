package gpvm.render;

import com.sun.istack.internal.logging.Logger;
import gpvm.Registrar;
import gpvm.map.GameMap;
import gpvm.map.Region;
import gpvm.map.RegionListener;
import gpvm.map.Tile;
import gpvm.map.TileDefinition;
import gpvm.map.TileRegistry;
import gpvm.render.vertices.ColorVertex;
import gpvm.util.Settings;
import gpvm.util.geometry.Coordinate;
import java.awt.Color;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;

/**
 * A renderer for a {@link Region} in the {@link GameMap}.
 * 
 * @author russell
 */
public class RegionRenderer implements RegionListener {

  /**
   * Creates a new {@link RegionRenderer} for the target region.
   * 
   * @param target The {@link Region} that will be rendered.
   * @param gmap The {@link GameMap} that this region is a part of.
   */
  public RegionRenderer(Region target, GameMap gmap) {
    reg = target;
    reg.addListener(this);
    map = gmap;
    
    unloaded = false;
    dirty = true;
  }
  
  /**
   * Renders the target {@link Region} and optionally a green grid at the
   * border of the {@link Region}.
   * 
   * @param grid Whether to also render a grid around the region.
   */
  public void render(boolean grid) {
    assert !unloaded;
    
    if(grid) {
      getGrid().draw();
    }
  }
  
  /**
   * Allows the {@link RegionRenderer} to update itself if there was a recent
   * change to the {@link Region}.
   */
  public void update() {
    if(!dirty) return;
    assert !unloaded;
    
    ArrayList<TileInfo> tiles = new ArrayList<>();
    ArrayList<Class<? extends TileRenderer>> renderers = new ArrayList<>();
    Coordinate loc = reg.getLocation();
    
    //get the info for all of the tiles in the region.
    for(byte i = 0; i < Region.REGION_SIZE; i++) {
      for(byte j = 0; j < Region.REGION_SIZE; j++) {
        for(byte k = 0; k < Region.REGION_SIZE; k++) {
          Tile tile = map.getTile(loc.add(i, j, k));
          if(tile == null) continue;
          
          //check the to see if the tile is visible
          Tile[] nbtiles = map.getNeighborTiles(loc);
          TileRegistry.ReadOnlyTileRegistry tregis = Registrar.getInstance().getTileRegistry();
          boolean visible = false;
          for(Tile t : nbtiles) {
            if(t == null) {
              visible = true;
              break;
            }
            
            TileDefinition def = tregis.getDefinition(t.type);
            if(!def.opaque) {
              visible = true;
              break;
            }
          }
          
          if(!visible) continue;
          
          //now start populating the tile info for rendering.
          TileInfo info = new TileInfo();
          info.absolutepos = loc.add(i, j, k, new Coordinate());
          info.adjacenttiles = map.getNeighborTiles(info.absolutepos);
          info.definition = tregis.getDefinition(tile.type);
          info.relativepos = new Coordinate(i, j, k);
          info.tile = tile;
          
          RenderRegistry.RendererEntry entry = Registrar.getInstance().getRenderRegistry().getEntry(tile.type);
          info.info = entry.info;
          
          tiles.add(info);
          renderers.add(entry.renderer);
        }
      }
    }
    
    assert tiles.size() == renderers.size();
    boolean[] checks = new boolean[tiles.size()];
    for(int i = 0; i < checks.length; i++) checks[i] = false;
    
    ArrayList<TileInfo> refined = new ArrayList<>();
    for(int i = 0; i < tiles.size(); i++) {
      if(checks[i]) continue;
      Class<? extends TileRenderer> currend = renderers.get(i);
      
      TileRenderer rend = null;
      try {
        rend = currend.newInstance();
      } catch(InstantiationException | IllegalAccessException ex) {
        Logger.getLogger(RegionRenderer.class).severe(Settings.getLocalString("err_invalid_renderer"), ex);
        continue;
      }
      
      refined.clear();
      //go through and collect all of the tile infos.
      for(int j = i; j < tiles.size(); j++) {
        if(renderers.get(j).equals(currend)) {
          checks[j] = true;
          refined.add(tiles.get(j));
        }
      }
      
      RawBatch[] bats = rend.batchTiles(refined);
      
      for(RawBatch tar : bats) {
        RenderingBatch fin;
        try {
          fin = renderclass.newInstance();
        } catch (IllegalAccessException | InstantiationException ex) {
          Logger.getLogger(RegionRenderer.class).severe(Settings.getLocalString("err_invalid_renderer"), ex);
          return;
        }
        
        fin.compile(tar);
        drawables.add(fin);
      }
    }
  }

  @Override
  public void regionUnloading(Region reg) {
    unloaded = true;
  }

  @Override
  public void regionUpdated(Region reg) {
    dirty = true;
  }
  
  private boolean dirty;
  private boolean unloaded;
  private Region reg;
  private GameMap map;
  private Class<? extends RenderingBatch> renderclass;
  private ArrayList<RenderingBatch> drawables;
  
  private static VertexArrayBatch grid;
  
  private static VertexArrayBatch getGrid() {
    if(grid != null) return grid;
    
    //construct the vertices for the grid.
    ArrayList<ColorVertex> vertices = new ArrayList<>();
   
    for(int i = 0; i <= Region.REGION_SIZE; i++) {
      //bottom
      vertices.add(new ColorVertex(i, 0, 0, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(i, Region.REGION_SIZE, 0, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(0, i, 0, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(Region.REGION_SIZE, i, 0, Color.GREEN.getRGB()));
      
      //top
      vertices.add(new ColorVertex(i, 0, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(i, Region.REGION_SIZE, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(0, i, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(Region.REGION_SIZE, i, Region.REGION_SIZE, Color.GREEN.getRGB()));
      
      //front
      vertices.add(new ColorVertex(i, 0, 0, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(i, 0, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(0, 0, i, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(Region.REGION_SIZE, 0, i, Color.GREEN.getRGB()));
      
      //back
      vertices.add(new ColorVertex(i, Region.REGION_SIZE, 0, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(i, Region.REGION_SIZE, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(0, Region.REGION_SIZE, i, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(Region.REGION_SIZE, Region.REGION_SIZE, i, Color.GREEN.getRGB()));
      
      //left
      vertices.add(new ColorVertex(0, i, 0, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(0, i, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(0, 0, i, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(0, Region.REGION_SIZE, i, Color.GREEN.getRGB()));
      
      //right
      vertices.add(new ColorVertex(Region.REGION_SIZE, i, 0, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(Region.REGION_SIZE, i, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(Region.REGION_SIZE, 0, i, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(Region.REGION_SIZE, Region.REGION_SIZE, i, Color.GREEN.getRGB()));
    }
    
    RawBatch bat = new RawBatch();
    bat.rendermode = GL11.GL_LINES;
    bat.vertices = new ColorVertex[vertices.size()];
    vertices.toArray(bat.vertices);
    
    grid = new VertexArrayBatch();
    grid.compile(bat);
    
    return grid;
  }

  void setRenderingBatch(Class<? extends RenderingBatch> renderclass) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
