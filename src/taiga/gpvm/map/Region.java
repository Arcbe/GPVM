/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm.map;

import java.util.ArrayList;
import taiga.gpvm.util.geom.Coordinate;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import taiga.code.util.ByteUtils;
import taiga.gpvm.registry.TileEntry;

/**
 *
 * @author russell
 */
public final class Region {
  /**
   * Each region will be a cube with edges of length REGION_SIZE.
   */
  public static final byte REGION_SIZE = 32;
  private final Coordinate location;
  private final World map;
  
  /**
   * Constructs a region with only empty {@link Tile}.
   * 
   * @param loc The location in the map where this region lies.
   * @param parent The {@link World} that contains this {@link Region}.
   */
  public Region(Coordinate loc, World parent) {
    map = parent;
    
    tiles = new Tile[REGION_SIZE * REGION_SIZE * REGION_SIZE];
    location = loc;
    
    for(int i = tiles.length - 1; i >= 0; i--)
      tiles[i] = new Tile();
    
    listeners = new ConcurrentLinkedQueue<>();
  }
  
  /**
   * Constructs a region with the given {@link Tile} data.
   * 
   * @param data The data to use when creating the {@link Region}, the data should be
   * laid out using the function z * REGION_SIZE ^ 2 + x * REGION_SIZE + y
   * @param loc The location of the region in the map.
   * @param parent The {@link World} that contains this {@link Region}.
   */
  public Region(Tile[] data, Coordinate loc, World parent) {
    map = parent;
    assert data.length == REGION_SIZE * REGION_SIZE * REGION_SIZE;
    
    for(int i = 0; i < data.length; i++) {
      if(data[i] == null)
        data[i] = new Tile();
    }
    
    tiles = data;
    location = loc;
    listeners = new ConcurrentLinkedQueue<>();
  }
  
  /**
   * Adds a listener for events occurring in the {@link Region}.
   * 
   * @param list The listener to attach to the {@link Region}
   */
  public void addListener(RegionListener list) {
    listeners.add(list);
  }
  
  /**
   * Returns the location of the {@link Region} in the map.  This is the
   * same {@link Coordinate} that was given in the constructor.
   * 
   * @return The location of the {@link Region}
   */
  public Coordinate getLocation() {
    return location;
  }
  
  /**
   * Returns the parent {@link World} containing this {@link Region}.
   * 
   * @return The parent {@link World}.
   */
  public World getWorld() {
    return map;
  }
  
  /**
   * Retrieves the tile at the given point.  The location is relative to the
   * origin of the region and not the origin of the map.
   * 
   * @param x The x coordinate of the tile.
   * @param y The y coordinate of the tile.
   * @param z The z coordinate of the tile.
   * @return The tile at the given point.
   */
  public Tile getTile(int x, int y, int z) {
    assert x < REGION_SIZE;
    assert y < REGION_SIZE;
    assert z < REGION_SIZE;
    assert x > 0;
    assert y > 0;
    assert z > 0;
    
    return tiles[z * REGION_SIZE * REGION_SIZE + x * REGION_SIZE + y];
  }
  
  /**
   * Informs the {@link Region} that is being unloaded.
   */
  public void unload() {
    for(RegionListener list : listeners)
      list.regionUnloading(this);
  }
  
  /**
   * This will encode the {@link Region} into an array of bytes.  Tiles will be
   * listed sequential with in the y direction, followed by in the x direction,
   * and finally in the z direction.  Following each entry will be a single byte
   * that for how many tiles share the same value.  First ids for {@link TileEntry}s
   * will be encoded, then the damage value, and finally a list of meta-data.
   * The meta-data will be encoded as an int id, a coordinate, and a value depending
   * on the type of meta-data.  All values are unsigned.
   * 
   * @return The {@link Region} encoded as a byte array.
   */
  public byte[] toBytes() {
    //An arbitrary starting amount.
    List<Byte> bytes = new ArrayList<>(16000);
    
    encodeDamageValues(bytes);
    encodeTileType(bytes);
    
    byte[] result = new byte[bytes.size()];
    int index = 0;
    for(Byte b : bytes)
      result[index++] = b;
    return result;
  }
  
  private void encodeDamageValues(List<Byte> bytes) {
    
    //first get all of the damage values
    short amount = 1;
    Tile last = null;
    byte[] converts = new byte[8]; //to hold bytes for a long.
    for(Tile t : tiles) {
      if(t == null) {
        throw new NullPointerException();
      }
      
      //lets start this thing.
      if(last == null) {
        last = t;
        continue;
      }
      
      //continue until a tile with a different value is hit.
      if(last.damage == t.damage && amount < 256) {
        amount++;
        continue;
      }
      
      ByteUtils.toBytes(last.damage, 0, converts);
      for(byte b : converts)
        bytes.add(b);
      bytes.add((byte) amount);
      amount = 1;
      
      last = t;
    }
    //and one more encoding to get the last set of tiles.
    ByteUtils.toBytes(last.damage, 0, converts);
    for(byte b : converts)
      bytes.add(b);
    bytes.add((byte) amount);
  }
  
  private void encodeTileType(List<Byte> bytes) {
    
    //first get all of the damage values
    short amount = 1;
    Tile last = null;
    byte[] converts = new byte[4]; //to hold bytes for a long.
    for(Tile t : tiles) {
      if(t == null) {
        throw new NullPointerException();
      }
      
      //lets start this thing.
      if(last == null) {
        last = t;
        continue;
      }
      
      //continue until a tile with a different value is hit.
      if(last.type == t.type && amount < 256) {
        amount++;
        continue;
      }
      
      if(last.type == null)
        ByteUtils.toBytes(-1, 0, converts);
      else
        ByteUtils.toBytes(last.type.getID(), 0, converts);
      for(byte b : converts)
        bytes.add(b);
      bytes.add((byte) amount);
      amount = 1;
      
      last = t;
    }
    //and one more encoding to get the last set of tiles.
    if(last.type == null)
      ByteUtils.toBytes(-1, 0, converts);
    else
      ByteUtils.toBytes(last.type.getID(), 0, converts);
    for(byte b : converts)
      bytes.add(b);
    bytes.add((byte) amount);
  }
  
  private Collection<RegionListener> listeners;
  private Tile[] tiles;
}
