/*
 * Copyright (C) 2014 Russell Smith
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package taiga.gpvm.map;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.util.ByteUtils;
import taiga.gpvm.registry.TileEntry;
import taiga.gpvm.util.geom.Coordinate;

/**
 * Handles the encoding of {@link Region}s into bytes or vice versa.  The
 * encoding will also compress the data.
 * 
 * @author Russell Smith
 */
public class RegionEncoder {
  
  /**
   * Encodes a given {@link Region} into a series of bytes.
   * 
   * @param reg The {@link Region} to encode.
   * @return An array of bytes for the encoded {@link Region}.
   */
  public static final byte[] encode(Region reg) {
    ByteArrayOutputStream out = new ByteArrayOutputStream(32000);
    
    try {
      encode(reg, out);
    } catch (IOException ex) {
      assert false;
    }
    
    return out.toByteArray();
  }
  
  /**
   * Encodes the given {@link Region} and writes the result to the given
   * {@link OutputStream}.
   * 
   * @param reg The {@link Region} to encode.
   * @param out The {@link OutputStream} to write the encoded {@link Region} to.
   * @throws java.io.IOException Thrown if there is an error writing the encoded
   * {@link Region} to the stream.
   */
  public static final void encode(Region reg, OutputStream out) throws IOException {
    byte[] idata = new byte[8];
    
    Coordinate loc = reg.getLocation();
    //encode the location, including world id first.
    out.write(ByteUtils.toBytes(loc.x, 0, idata), 0, 4);
    out.write(ByteUtils.toBytes(loc.y, 0, idata), 0, 4);
    out.write(ByteUtils.toBytes(loc.z, 0, idata), 0, 4);
    
    encodeTiles(reg, out, idata);
    encodeDamage(reg, out, idata);
  }
  
  /**
   * Decodes a {@link Region} from the given bytes, and adds it to the
   * given {@link World}. If the given bytes cannot be decoded into a valid
   * {@link Region} then this will return null.
   * 
   * @param in The bytes that encode the {@link Region}.
   * @param world The {@link World} to add the {@link Region} to.
   * @return The decoded {@link Region}.
   */
  public static final Region decode(byte[] in, World world) {
    try {
      return decode(new ByteArrayInputStream(in), world);
    } catch (IOException ex) {
      return null;
    }
  }
  
  /**
   * Decodes a {@link Region} encode by this class from the given 
   * {@link InputStream} and places it in the given world.
   * 
   * @param in The {@link InputStream} to extract the {@link Region} from.
   * @param world The {@link World} to place the {@link Region} in.
   * @return The decoded {@link Region}.
   * @throws IOException if there is a problem reading the {@link Region} from
   * the {@link InputStream}.
   */
  public static final Region decode(InputStream in, World world) throws IOException {
    assert world != null;
    assert in != null;
    
    byte[] idata = new byte[8];
    Coordinate loc = new Coordinate();
    Region out = new Region(loc, world);
    
    //read the coordinate in form the stream.
    in.read(idata, 0, 12);
    loc.x = ByteUtils.toInteger(idata, 0);
    loc.y = ByteUtils.toInteger(idata, 4);
    loc.z = ByteUtils.toInteger(idata, 8);
    
    decodeTiles(null, in, idata);
    world.addRegion(out);
    return out;
  }
  
  private static void encodeTiles(Region reg, OutputStream out, byte[] temp) throws IOException {
    TileEntry current = null;
    short cnt = 0;
    
    for(int k = 0; k < Region.REGION_SIZE; k++) {
      for(int i = 0; i < Region.REGION_SIZE; i++) {
        for(int j = 0; j < Region.REGION_SIZE; j++) {
          if(reg.getTile(i, j, k).type == current) {
            cnt++;
            continue;
          }
          
          if(cnt == 0) continue;
          
          //write the tile id then the amount and continue on.
          //air tiles will be encoded as -1.
          if(current == null)
            out.write(ByteUtils.toBytes(-1, 0, temp), 0, 4);
          else
            out.write(ByteUtils.toBytes(current.getID(), 0, temp), 0, 4);
          out.write(ByteUtils.toBytes(cnt, 0, temp), 0, 2);
          
          cnt = 0;
        }
      }
    }
    
    //write the tile id then the amount and continue on.
    if(current == null)
      out.write(ByteUtils.toBytes(0, 0, temp), 0, 4);
    else
      out.write(ByteUtils.toBytes(current.getID(), 0, temp), 0, 4);
    out.write(ByteUtils.toBytes(cnt, 0, temp), 0, 2);
  }
  
  private static void decodeTiles(Region reg, InputStream in, byte[] temp) throws IOException {
    int type = 0;
    short amt = 0;
    int cnt = 0;
    
    while(cnt < Region.REGION_SIZE * Region.REGION_SIZE * Region.REGION_SIZE) {
      //get the coordinate for the count
      int k = (cnt / (Region.REGION_SIZE * Region.REGION_SIZE)) % Region.REGION_SIZE;
      int i = (cnt / Region.REGION_SIZE) % Region.REGION_SIZE;
      int j = (cnt) % Region.REGION_SIZE;
      
      if(amt == 0) {
        in.read(temp, 0, 6);
        type = ByteUtils.toInteger(temp);
        amt = ByteUtils.toShort(temp, 4);
      }
      
      reg.setTile(i, j, k, type);
      amt--;
    }
  }
  
  private static void encodeDamage(Region reg, OutputStream out, byte[] temp) throws IOException {
    long current = 0;
    int cnt = 0;
    
    for(int k = 0; k < Region.REGION_SIZE; k++) {
      for(int i = 0; i < Region.REGION_SIZE; i++) {
        for(int j = 0; j < Region.REGION_SIZE; j++) {
          if(reg.getTile(i, j, k).damage == current) {
            cnt++;
            continue;
          }
          
          if(cnt == 0) continue;
          
          //write the tile id then the amount and continue on.
          out.write(ByteUtils.toBytes(current, 0, temp), 0, 8);
          out.write(ByteUtils.toBytes(cnt, 0, temp), 0, 4);
          
          cnt = 0;
        }
      }
    }
    
    //write the tile id then the amount and continue on.
    out.write(ByteUtils.toBytes(current, 0, temp), 0, 8);
    out.write(ByteUtils.toBytes(cnt, 0, temp), 0, 4);
  }
  
  private static final String locprefix = RegionEncoder.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
