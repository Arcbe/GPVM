package gpvm.render;

import gpvm.Registrar;
import gpvm.map.GameMap;
import gpvm.map.Region;
import gpvm.map.RegionListener;
import gpvm.map.Tile;
import gpvm.map.TileRegistry;
import gpvm.render.vertices.ColorVertex;
import gpvm.util.Settings;
import gpvm.util.geometry.Coordinate;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.lwjgl.opengl.GL11;

/**
 * A renderer for a {@link Region} in the {@link GameMap}.
 * 
 * @author russell
 */
public final class RegionRenderer implements RegionListener {

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
    drawables = new ArrayList<>();
    tiles = new HashMap<>();
    
    scanRegion();
  }
  
  /**
   * Clears all rendering data and regenerates it from the map.
   */
  public void scanRegion() {
    //clear all of the data
    drawables.clear();
    tiles.clear();
    
    Coordinate curloc = new Coordinate();
    
    //construct the various tile infos.
    for(byte i = 0; i < Region.REGION_SIZE; i++) {
      for(byte j = 0; j < Region.REGION_SIZE; j++) {
        for(byte k = 0; k < Region.REGION_SIZE; k++) {
          curloc.x = i;
          curloc.y = j;
          curloc.z = k;
          Tile tile = map.getTile(curloc.add(reg.getLocation()));
          
          //the region should be loaded so no null tiles
          //and air tiles are not rendered.
          assert tile != null;
          if(tile.type == 0) continue;
          
          //construct and populate the tile info.
          TileInfo info = new TileInfo();
          info.adjacenttiles = map.getNeighborTiles(curloc);
          
          //there has to be at least one non opaque adjacent tile inorder to render the tile
          boolean visible = false;
          for(Tile t : info.adjacenttiles) {
            if(t.type == 0 || 
                    !Registrar.getInstance().getTileRegistry().getDefinition(t.type).opaque) {
              visible = true;
              break;
            }
          }
          if(!visible) continue;
          
          TileRegistry.TileEntry ent = Registrar.getInstance().getTileRegistry().getEntry(tile.type);
          info.absolutepos = new Coordinate(curloc);
          info.definition = ent.def;
          info.relativepos = new Coordinate(i, j, k);
          info.tile = tile;
          info.info = Registrar.getInstance().getRenderRegistry().getEntry(ent.TileID).info;
          
          tiles.put(new Coordinate(curloc), info);
        }
      }
    }
  }
  
  /**
   * Runs through the list of tiles and creates {@link RenderingBatch}s for them.
   */
  public void createRenderers() {
    drawables.clear();
    
    Collection<TileInfo> ts = tiles.values();
    boolean[] processed = new boolean[ts.size()];
    
    TileRegistry.ReadOnlyTileRegistry tr = Registrar.getInstance().getTileRegistry();
    RenderRegistry.ReadOnlyRenderRegistry rr = Registrar.getInstance().getRenderRegistry();
    
    int ind = 0;
    for(TileInfo tile : ts) {
      if(processed[ind]) continue;
      
      //when an un processed tile is found collect all of the ones that use the same renderer.
      int j = 0;
      ArrayList<TileInfo> infos = new ArrayList<>();
      Class<? extends TileRenderer> renderer = rr.getEntry(tr.getBaseID(tile.tile.type)).renderer;
      for(TileInfo col : ts) {
        if(renderer == rr.getEntry(tr.getBaseID(col.tile.type)).renderer) {
          processed[j] = true;
          infos.add(col);
        }
        j++;
      }
      try {
        //now that we have all of the tiles time to be the rendering batch
        TileRenderer rend = renderer.newInstance();
        RawBatch[] raws = rend.batchTiles(infos);
        
        for(RawBatch bat : raws) {
          RenderingBatch finbat = renderclass.newInstance();
          finbat.compile(bat);
          drawables.add(finbat);
        }
      } catch (IllegalAccessException | InstantiationException ex) {
        java.util.logging.Logger.getLogger(RegionRenderer.class.getName()).log(Level.SEVERE, Settings.getLocalString("err_invalid_renderer"), ex);
      }
      
      ind++;
    }
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
    
    for(RenderingBatch bat : drawables) {
      bat.draw();
    }
  }
  
  /**
   * Allows the {@link RegionRenderer} to update itself if there was a recent
   * change to the {@link Region}.
   */
  public void update() {
    if(!dirty) return;
    assert !unloaded;
    
    createRenderers();
    
    dirty = false;
  }
  
  public void setRenderBatch(Class<? extends RenderingBatch> batclass) {
    renderclass = batclass;
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
  private Map<Coordinate, TileInfo> tiles;
  
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
}
