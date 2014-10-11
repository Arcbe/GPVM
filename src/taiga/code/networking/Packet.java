/*
 * Copyright (C) 2014 Russell Smith.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package taiga.code.networking;

import java.net.InetAddress;

/**
 * A simple array of bytes along with information about where this data is intended
 * to go.
 * 
 * @author russell
 */
public final class Packet {
  /**
   * The address of the remote computer that sent this packet.
   */
  public InetAddress source;
  /**
   * The numerical index for the packet.  The last 256 packets are stored in case they need
   * to be resent.
   */
  public byte number;
  
  /**
   * The id for the target {@link NetworkedObject} that this packet will be sent to.  This will
   * be the same as the id for the {@link NetworkedObject} that sent it.
   */
  public short target;
  
  /**
   * The payload of the packet.
   */
  public byte[] data;

  /**
   * Creates a new {@link Packet} without any data.
   */
  public Packet() {
  }
  
  /**
   * Constructs a new packet based on the given array of bytes.  This uses the
   * {@link Packet#decode(byte[])} method to construct this packet.
   * 
   * @param raw The array of bytes to construct this packet from.
   */
  public Packet(byte[] raw) {
    decode(raw);
  }
  
  /**
   * Converts the data from the packet in an array of bytes that can be decoded
   * back into the original packet.
   * 
   * @return A byte array for this packet.  If the data field of this packet is
   * null then the result of this method is also null.
   */
  public byte[] encode() {
    if(data == null) return null;
    
    //3 bytes are needed to store the number and target of this packet.
    byte[] result = new byte[data.length + 3];
    
    result[0] = number;
    result[1] = (byte) (target >> 8);
    result[2] = (byte) target;
    
    for(int i = 0; i < data.length; i++) {
      result[i + 3] = data[i];
    }
    
    return result;
  }
  
  /**
   * Decodes the data from another packet.  This will change the fields of this
   * packet to match the field of the packet used to encode this data.  The source
   * field is unaffected by this operation.
   * @param raw 
   */
  public void decode(byte[] raw) {
    number = raw[0];
    
    target = (short) ((short) (raw[1]) << 8);
    target |= raw[2];
    
    data = new byte[raw.length - 3];
    for(int i = 0; i < data.length; i++) {
      data[i] = raw[i + 3];
    }
  }
}
