package gpvm.render;

import gpvm.Registrar;
import gpvm.map.GameMap;
import gpvm.map.Region;
import gpvm.map.RegionListener;
import gpvm.map.Tile;
import gpvm.map.TileDefinition;
import gpvm.map.TileRegistry;
import gpvm.render.vertices.ByteColorVertex;
import gpvm.render.vertices.ColorVertex;
import gpvm.util.StringManager;
import gpvm.util.geometry.Coordinate;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    entries = new ArrayList<>();
    rendindex = new HashMap<>();
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
  }
  
  /**
   * Updates rendering information for a single tile in the {@link Region}.
   * 
   * @param x The x coordinate of the {@link Tile} in the {@link Region}.
   * @param y The y coordinate of the {@link Tile} in the {@link Region}.
   * @param z The z coordinate of the {@link Tile} in the {@link Region}.
   */
  public void updateTile(int x, int y, int z) {
    //there is nothing to do if there is no way to render the tiles.
    if(renderclass == null) return;
    
    //first check to see if the tile needs rendering
    Tile tar = reg.getTile(x, y, z);
    if(tar == null || tar.type == Tile.NULL) return;
    
    //now check if the tile is actually visible
    Coordinate loc = new Coordinate(x, y, z);
    Tile[] ngbrs = map.getNeighborTiles(loc);
    boolean visible = false;
    for(int i = 0; i < ngbrs.length; i++) {
      if(ngbrs[i] == null) {
        visible = true;
        break;
      }
      
      TileDefinition nbdef = Registrar.getInstance().getTileRegistry().getDefinition(ngbrs[i].type);
      if(!nbdef.opaque) {
        visible = true;
        break;
      }
    }
    if(!visible) return;
    
    //The tile can be rendered and is visible, now collect information on the tile.
    TileInfo info = new TileInfo();
    info.relativepos = loc;
    info.adjacenttiles = ngbrs;
    info.definition = Registrar.getInstance().getTileRegistry().getDefinition(tar.type);
    info.tile = tar;
    info.absolutepos = reg.getLocation().add(x, y, z, new Coordinate());
    
    RenderRegistry.RendererEntry rendreg = Registrar.getInstance().getRenderRegistry().getEntry(tar.type);
    info.info = rendreg.info;
    TileRenderer render = rendreg.renderer;
    
    //TODO: is there a better way to do this.
    //check for a static renderer
    if(render instanceof StaticRenderer) {
      StaticRenderer srender = (StaticRenderer) render;
      
      //remove any previous data
      RenderingEntry ent = rendindex.get(loc);
      if(ent != null) ent.remove(loc);
      
      //now add the new data
      RawBatch raw = srender.render(info);
      boolean found = false;
      for (Iterator<RenderingEntry> it = entries.iterator(); it.hasNext();) {
        ent = it.next();
        if(ent.compatible(raw)) {
          ent.add(loc, raw);
          found = true;
          break;
        }
      }
      
      //create a new RenderingEntry if there is not alreay one.
      if(!found) {
        try {
          ent = new RenderingEntry(renderclass.newInstance());
        } catch (InstantiationException ex) {
          Logger.getLogger(RegionRenderer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
          Logger.getLogger(RegionRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
        ent.add(loc, raw);
        entries.add(ent);
      }
      rendindex.put(loc, ent);
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
    
    //draw all of the static things
    for(RenderingEntry ent : entries) {
      ent.compiled.draw();
    }
  }
  
  /**
   * Allows the {@link RegionRenderer} to update itself if there was a recent
   * change to the {@link Region}.
   */
  public void update() {
    assert !unloaded;
    
    for(RenderingEntry ent : entries)
      ent.update();
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
  }
  
  private boolean unloaded;
  private Region reg;
  private GameMap map;
  private Class<? extends RenderingBatch> renderclass;
  
  //cached data for static rendering.
  private Map<Coordinate, RenderingEntry> rendindex;
  private List<RenderingEntry> entries;
  
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

  Coordinate getLocation() {
    return reg.getLocation();
  }
  
  private static class RenderingEntry {
    RenderingBatch compiled;
    Map<Coordinate, RawBatch> batches;
    boolean dirty;

    public RenderingEntry(RenderingBatch bat) {
      compiled = null;
      batches = new HashMap<>();
      dirty = false;
      compiled = bat;
    }
    
    private void update() {
      if(!dirty || batches.isEmpty()) return;
      
      RawBatch comp = new RawBatch();
      for(RawBatch bat : batches.values())
        comp.combine(bat);
      
      compiled.compile(comp);
    }

    private void remove(Coordinate loc) {
      batches.remove(loc);
      
      dirty = true;
    }

    private boolean compatible(RawBatch raw) {
      if(batches.isEmpty()) return true;
      RawBatch test = batches.values().iterator().next();
      return test.compatible(raw);
    }

    private void add(Coordinate loc, RawBatch raw) {
      assert compatible(raw);
      
      batches.put(loc, raw);
      
      dirty = true;
    }
  }
}
