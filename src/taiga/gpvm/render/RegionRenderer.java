package taiga.gpvm.render;

import taiga.gpvm.HardcodedValues;
import taiga.gpvm.map.Region;
import taiga.gpvm.map.RegionListener;
import taiga.gpvm.map.Tile;
import taiga.gpvm.util.geom.Coordinate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.registration.RegisteredObject;
import taiga.gpvm.registry.TileRenderingRegistry;

/**
 * A renderer for a {@link Region} in the {@link GameMap}.
 * 
 * @author russell
 */
public final class RegionRenderer extends RegisteredObject implements RegionListener {
  /**
   * The prefix for the name of the {@link RegionRenderer}.
   */
  public static final String NAME_PREFIX = "region-";
  
  /**
   * Flag that indicates whether a grid should be rendered around the {@link Region}.
   */
  public boolean outline;
  
  /**
   * Creates a new {@link RegionRenderer} for the target region.
   * 
   * @param target The {@link Region} that will be rendered.
   */
  public RegionRenderer(Region target) {
    super(NAME_PREFIX + target.getLocation());
    
    entries = new HashMap<>();
    rendindex = new HashMap<>();
    instances = new ConcurrentHashMap<>();
    dirtyents = new HashMap<>();
    
    reg = target;
    reg.addListener(this);
    dirty = true;
  }
  
  /**
   * Clears all rendering data and regenerates it from the map.
   */
  public void scanRegion() {
    
    //clear all of the data
    entries.clear();
    rendindex.clear();
    
    //construct the various entity infos.
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
   * Update the rendering information for a single entity in the {@link Region}.
   * 
   * @param coor The {@link Coordinate} or the {@link Tile} to update.
   */
  public void updateTile(Coordinate coor) {
    coor = coor.getRelativeCoordinate();
    
    updateTile(coor.x, coor.y, coor.z);
  }
  
  /**
   * Updates rendering information for a single entity in the {@link Region}.
   * 
   * @param x The x coordinate of the {@link Tile} in the {@link Region}.
   * @param y The y coordinate of the {@link Tile} in the {@link Region}.
   * @param z The z coordinate of the {@link Tile} in the {@link Region}.
   */
  public void updateTile(int x, int y, int z) {
    TileRenderingRegistry rendreg = (TileRenderingRegistry) getObject(HardcodedValues.TILE_RENDERING_REGISTRY_NAME);
    
    //first check to see if the entity needs rendering
    Tile tar = reg.getTile(x, y, z);
    if(tar == null || tar.type == null) return;
    
    //now check if the entity is actually visible
    Coordinate loc = new Coordinate(x, y, z);
    Tile[] ngbrs = reg.getWorld().getNeighborTiles(loc);
    boolean visible = false;
    for (Tile ngbr : ngbrs) {
      if (ngbr == null || ngbr.type == null) {
        visible = true;
        break;
      }
      if (!ngbr.type.opaque) {
        visible = true;
        break;
      }
    }
    if(!visible) return;
    
    //The entity can be rendered and is visible, now collect information on the entity.
    TileInfo info = new TileInfo();
    info.adjacent = ngbrs;
    info.tile = tar;
    info.absposition = reg.getLocation().add(x, y, z, new Coordinate());
    info.rendentry = rendreg.getEntry(tar.type);
    
    //ues the asociated renderer if avalaible otherwise use the default one.
    Class<? extends Renderer> renderer;
    if(info.rendentry == null) {
      renderer = HardcodedValues.DEFAULT_RENDERER;
    } else {
      renderer = info.rendentry.renderer;
    }
    
    List<TileInfo> ents = entries.get(renderer);
    TileInfo oldent = rendindex.get(info.absposition);
    Renderer newrend = instances.get(renderer);
    
    // create the renderer if needed
    if(newrend == null) {
      try {
        newrend = renderer.newInstance();
        instances.put(newrend.getClass(), newrend);
      } catch (InstantiationException | IllegalAccessException ex) {
        log.log(Level.SEVERE, null, ex);
        assert false;
        return;
      }
    }
    
    //create the list of tileinfo.
    if(ents == null) {
      ents = new CopyOnWriteArrayList<>();
      entries.put(renderer, ents);
    } else {
      ents.remove(oldent);
    }
    
    // Update the previous renderer if needed
    if(oldent != null) {
    
      //ues the asociated renderer if avalaible otherwise use the default one.
      Class<? extends Renderer> oldrendclass;
      if(oldent.rendentry == null) {
        oldrendclass = HardcodedValues.DEFAULT_RENDERER;
      } else {
        oldrendclass = oldent.rendentry.renderer;
      }
      
      Renderer oldrend = instances.get(oldrendclass);
      if(oldrend != newrend){
        List<TileInfo> oldents = entries.get(oldrendclass);

        oldents.remove(oldent);
        dirtyents.put(oldrendclass, true);
      }
    }
    
    ents.add(info);
    rendindex.put(info.absposition, info);
    dirtyents.put(newrend.getClass(), true);
  }

  @Override
  public void regionUnloading(Region reg) {
  }

  @Override
  public void regionUpdated(Region reg) {
  }

  /**
   * Returns the {@link Coordinate} for the bottom souther western {@link Tile}
   * of the {@link Region}.
   * 
   * @return The corner of the {@link Region}.
   */
  public Coordinate getLocation() {
    return reg.getLocation();
  }

  protected void update() {    
    if(dirty)
      scanRegion();
    
    for(Map.Entry<Class<? extends Renderer>, Boolean> ent : dirtyents.entrySet()) {
      if(ent.getValue()) {
        ent.setValue(false);
        Renderer rend = instances.get(ent.getKey());
        List<TileInfo> tiles = entries.get(ent.getKey());
        
        rend.compile(tiles);
      }
    }
  }

  protected void render(int pass) {    
    for(Renderer rend : instances.values())
      rend.render(pass);
  }
  
  private boolean dirty;
  private final Region reg;
  
  //cached data for static rendering.
  private final Map<Coordinate, TileInfo> rendindex;
  private final Map<Class<? extends Renderer>, List<TileInfo>> entries;
  private final Map<Class<? extends Renderer>, Boolean> dirtyents;
  private final Map<Class<? extends Renderer>, Renderer> instances;
  
  private static VertexArrayBatch grid;
  
  private static VertexArrayBatch getGrid() {
//    if(grid != null) 
      return grid;
    
//    //construct the vertices for the grid.
//    ArrayList<ByteColorVertex> vertices = new ArrayList<>();
//   
//    for(byte i = 0; i <= Region.REGION_SIZE; i++) {
//      //bottom
//      vertices.add(new ByteColorVertex(i, (byte)0, (byte)0, Color.GREEN.getRGB()));
//      vertices.add(new ByteColorVertex(i, Region.REGION_SIZE, (byte)0, Color.GREEN.getRGB()));
//      vertices.add(new ByteColorVertex((byte)0, i, (byte)0, Color.GREEN.getRGB()));
//      vertices.add(new ByteColorVertex(Region.REGION_SIZE, i, (byte)0, Color.GREEN.getRGB()));
//      
//      //top
//      vertices.add(new ByteColorVertex(i, (byte)0, Region.REGION_SIZE, Color.GREEN.getRGB()));
//      vertices.add(new ByteColorVertex(i, Region.REGION_SIZE, Region.REGION_SIZE, Color.GREEN.getRGB()));
//      vertices.add(new ByteColorVertex((byte)0, i, Region.REGION_SIZE, Color.GREEN.getRGB()));
//      vertices.add(new ByteColorVertex(Region.REGION_SIZE, i, Region.REGION_SIZE, Color.GREEN.getRGB()));
//      
//      //front
//      vertices.add(new ByteColorVertex(i, (byte)0, (byte)0, Color.GREEN.getRGB()));
//      vertices.add(new ByteColorVertex(i, (byte)0, Region.REGION_SIZE, Color.GREEN.getRGB()));
//      vertices.add(new ByteColorVertex((byte)0, (byte)0, i, Color.GREEN.getRGB()));
//      vertices.add(new ByteColorVertex(Region.REGION_SIZE, (byte)0, i, Color.GREEN.getRGB()));
//      
//      //back
//      vertices.add(new ByteColorVertex(i, Region.REGION_SIZE, (byte)0, Color.GREEN.getRGB()));
//      vertices.add(new ByteColorVertex(i, Region.REGION_SIZE, Region.REGION_SIZE, Color.GREEN.getRGB()));
//      vertices.add(new ByteColorVertex((byte)0, Region.REGION_SIZE, i, Color.GREEN.getRGB()));
//      vertices.add(new ByteColorVertex(Region.REGION_SIZE, Region.REGION_SIZE, i, Color.GREEN.getRGB()));
//      
//      //left
//      vertices.add(new ByteColorVertex((byte)0, i, (byte)0, Color.GREEN.getRGB()));
//      vertices.add(new ByteColorVertex((byte)0, i, Region.REGION_SIZE, Color.GREEN.getRGB()));
//      vertices.add(new ByteColorVertex((byte)0, (byte)0, i, Color.GREEN.getRGB()));
//      vertices.add(new ByteColorVertex((byte)0, Region.REGION_SIZE, i, Color.GREEN.getRGB()));
//      
//      //right
//      vertices.add(new ByteColorVertex(Region.REGION_SIZE, i, (byte)0, Color.GREEN.getRGB()));
//      vertices.add(new ByteColorVertex(Region.REGION_SIZE, i, Region.REGION_SIZE, Color.GREEN.getRGB()));
//      vertices.add(new ByteColorVertex(Region.REGION_SIZE, (byte)0, i, Color.GREEN.getRGB()));
//      vertices.add(new ByteColorVertex(Region.REGION_SIZE, Region.REGION_SIZE, i, Color.GREEN.getRGB()));
//    }
//    
//    RawBatch bat = new RawBatch();
//    bat.rendermode = GL11.GL_LINES;
//    bat.vertices = new ByteColorVertex[vertices.size()];
//    vertices.toArray(bat.vertices);
//    
//    grid = new VertexArrayBatch();
//    grid.compile(bat);
//    
//    return grid;
  }
  
  private static final String locprefix = RegionRenderer.class.getName().toLowerCase();
  
  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
