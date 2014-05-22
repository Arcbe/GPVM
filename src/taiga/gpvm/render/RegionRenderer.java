package taiga.gpvm.render;

import gpvm.HardcodedValues;
import taiga.gpvm.map.GameMap;
import taiga.gpvm.map.Region;
import taiga.gpvm.map.RegionListener;
import taiga.gpvm.map.Tile;
import gpvm.render.vertices.ByteColorVertex;
import gpvm.util.geometry.Coordinate;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import taiga.code.graphics.Renderable;
import taiga.gpvm.registry.RenderingRegistry;

/**
 * A renderer for a {@link Region} in the {@link GameMap}.
 * 
 * @author russell
 */
public final class RegionRenderer extends Renderable implements RegionListener {
  public static final String NAME_PREFIX = "region";
  
  public boolean outline;
  
  /**
   * Creates a new {@link RegionRenderer} for the target region.
   * 
   * @param target The {@link Region} that will be rendered.
   * @param gmap The {@link GameMap} that this region is a part of.
   */
  public RegionRenderer(Region target) {
    super(NAME_PREFIX + target.getLocation());
    
    reg = target;
    reg.addListener(this);
    dirty = true;
    
    entries = new HashMap<>();
    rendindex = new HashMap<>();
    instances = new HashMap<>();
  }
  
  /**
   * Clears all rendering data and regenerates it from the map.
   */
  public void scanRegion() {
    
    //clear all of the data
    entries.clear();
    rendindex.clear();
    
    //construct the various tile infos.
    for(byte i = 0; i < Region.REGION_SIZE; i++) {
      for(byte j = 0; j < Region.REGION_SIZE; j++) {
        for(byte k = 0; k < Region.REGION_SIZE; k++) {
          updateTile(i, j, k);
        }
      }
    }
    
    dirty = false;
  }
  
  /**
   * Updates rendering information for a single tile in the {@link Region}.
   * 
   * @param x The x coordinate of the {@link Tile} in the {@link Region}.
   * @param y The y coordinate of the {@link Tile} in the {@link Region}.
   * @param z The z coordinate of the {@link Tile} in the {@link Region}.
   */
  public void updateTile(int x, int y, int z) {
    RenderingRegistry rendreg = (RenderingRegistry) getObject(HardcodedValues.RENDERING_REGISTRY_NAME);
    
    //first check to see if the tile needs rendering
    Tile tar = reg.getTile(x, y, z);
    if(tar == null || tar.type == null) return;
    
    //now check if the tile is actually visible
    Coordinate loc = new Coordinate(x, y, z);
    Tile[] ngbrs = reg.getGameMap().getNeighborTiles(loc);
    boolean visible = false;
    for(int i = 0; i < ngbrs.length; i++) {
      if(ngbrs[i] == null || ngbrs[i].type == null) {
        visible = true;
        break;
      }
      
      if(!ngbrs[i].type.opaque) {
        visible = true;
        break;
      }
    }
    if(!visible) return;
    
    //The tile can be rendered and is visible, now collect information on the tile.
    TileInfo info = new TileInfo();
    info.adjacent = ngbrs;
    info.tile = tar;
    info.absposition = reg.getLocation().add(x, y, z, new Coordinate());
    info.rendentry = rendreg.getEntry(tar.type);
    
    List<TileInfo> ents = entries.get(info.rendentry.renderer);
    TileInfo oldent = rendindex.get(info.absposition);
    Renderer newrend = instances.get(info.rendentry.renderer);
    
    // create the renderer if needed
    if(newrend == null) {
      try {
        newrend = info.rendentry.renderer.newInstance();
      } catch (InstantiationException | IllegalAccessException ex) {
        log.log(Level.SEVERE, null, ex);
        assert false;
        return;
      }
    }
    
    //create the list of tileinfo.
    if(ents == null) {
      ents = new ArrayList<>();
      entries.put(info.rendentry.renderer, ents);
    }
    
    // Update the previous renderer if needed
    if(oldent != null) {
      Renderer oldrend = instances.get(oldent.rendentry.renderer);
      if(oldrend != newrend){
        List<TileInfo> oldents = entries.get(oldent.rendentry.renderer);

        oldents.remove(oldent);
        oldrend.compile(oldents);
      }
    }
    
    ents.add(info);
    newrend.compile(ents);
  }

  @Override
  public void regionUnloading(Region reg) {
  }

  @Override
  public void regionUpdated(Region reg) {
  }
  
  private boolean dirty;
  private final Region reg;
  
  //cached data for static rendering.
  private final Map<Coordinate, TileInfo> rendindex;
  private final Map<Class<? extends Renderer>, List<TileInfo>> entries;
  private final Map<Class<? extends Renderer>, Renderer> instances;
  
  private static VertexArrayBatch grid;
  
  private static VertexArrayBatch getGrid() {
    if(grid != null) return grid;
    
    //construct the vertices for the grid.
    ArrayList<ByteColorVertex> vertices = new ArrayList<>();
   
    for(byte i = 0; i <= Region.REGION_SIZE; i++) {
      //bottom
      vertices.add(new ByteColorVertex(i, (byte)0, (byte)0, Color.GREEN.getRGB()));
      vertices.add(new ByteColorVertex(i, Region.REGION_SIZE, (byte)0, Color.GREEN.getRGB()));
      vertices.add(new ByteColorVertex((byte)0, i, (byte)0, Color.GREEN.getRGB()));
      vertices.add(new ByteColorVertex(Region.REGION_SIZE, i, (byte)0, Color.GREEN.getRGB()));
      
      //top
      vertices.add(new ByteColorVertex(i, (byte)0, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ByteColorVertex(i, Region.REGION_SIZE, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ByteColorVertex((byte)0, i, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ByteColorVertex(Region.REGION_SIZE, i, Region.REGION_SIZE, Color.GREEN.getRGB()));
      
      //front
      vertices.add(new ByteColorVertex(i, (byte)0, (byte)0, Color.GREEN.getRGB()));
      vertices.add(new ByteColorVertex(i, (byte)0, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ByteColorVertex((byte)0, (byte)0, i, Color.GREEN.getRGB()));
      vertices.add(new ByteColorVertex(Region.REGION_SIZE, (byte)0, i, Color.GREEN.getRGB()));
      
      //back
      vertices.add(new ByteColorVertex(i, Region.REGION_SIZE, (byte)0, Color.GREEN.getRGB()));
      vertices.add(new ByteColorVertex(i, Region.REGION_SIZE, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ByteColorVertex((byte)0, Region.REGION_SIZE, i, Color.GREEN.getRGB()));
      vertices.add(new ByteColorVertex(Region.REGION_SIZE, Region.REGION_SIZE, i, Color.GREEN.getRGB()));
      
      //left
      vertices.add(new ByteColorVertex((byte)0, i, (byte)0, Color.GREEN.getRGB()));
      vertices.add(new ByteColorVertex((byte)0, i, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ByteColorVertex((byte)0, (byte)0, i, Color.GREEN.getRGB()));
      vertices.add(new ByteColorVertex((byte)0, Region.REGION_SIZE, i, Color.GREEN.getRGB()));
      
      //right
      vertices.add(new ByteColorVertex(Region.REGION_SIZE, i, (byte)0, Color.GREEN.getRGB()));
      vertices.add(new ByteColorVertex(Region.REGION_SIZE, i, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ByteColorVertex(Region.REGION_SIZE, (byte)0, i, Color.GREEN.getRGB()));
      vertices.add(new ByteColorVertex(Region.REGION_SIZE, Region.REGION_SIZE, i, Color.GREEN.getRGB()));
    }
    
    RawBatch bat = new RawBatch();
    bat.rendermode = GL11.GL_LINES;
    bat.vertices = new ByteColorVertex[vertices.size()];
    vertices.toArray(bat.vertices);
    
    grid = new VertexArrayBatch();
    grid.compile(bat);
    
    return grid;
  }

  public Coordinate getLocation() {
    return reg.getLocation();
  }

  @Override
  protected void updateSelf() {    
    if(dirty)
      scanRegion();
  }

  @Override
  protected void renderSelf(int pass) {
    if(outline) {
      getGrid().draw();
    }
    
    for(Renderer rend : instances.values())
      rend.render();
  }
  
  private static final String locprefix = RegionRenderer.class.getName().toLowerCase();
  
  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
