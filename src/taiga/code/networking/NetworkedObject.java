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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import taiga.code.registration.NamedObject;

/**
 * An {@link Object} that is can communicate with a matching object over a network.
 * An identical {@link NetworkedObject} will need to be present in the remote 
 * program with the same full name as this one.  Packets
 * can then be sent between the objects, however only client server architecture
 * is supported.
 * 
 * @author russell
 */
public abstract class NetworkedObject extends NamedObject {

  /**
   * Creates a new {@link NetworkedObject} with th given name.
   * 
   * @param name The name for this {@link NetworkedObject}
   */
  public NetworkedObject(String name) {
    super(name);
    
    id = -1;
  }
  
  /**
   * Sends a {@link DatagramPacket} through the attached {@link NetworkManager}.  This
   * method will only work if the manager is a client.  If the manager is not a
   * client then the {@link NetworkedObject#sendMessage(taiga.code.networking.Packet, java.net.InetAddress) }
   * should be used instead.
   * 
   * @param pack The {@link DatagramPacket} to send.
   * @throws java.io.IOException Thrown if the {@link DatagramPacket} cannot be
   * sent for any reason.
   */
  public void sendMessage(DatagramPacket pack) throws IOException {
    manager.sendPacket(null, getID(), pack);
  }
  
  /**
   * Sends a {@link Packet} to a specific destination.  If the manager is a client
   * then the destination is ignored as {@link DatagramPacket}s can only go to the server.
   * 
   * @param pack The {@link DatagramPacket} to send.
   * @param dest The {@link InetAddress} of the destination.
   * @throws java.io.IOException Thrown if the {@link DatagramPacket} cannot be
   * sent for any reason.
   */
  public void sendMessage(DatagramPacket pack, InetAddress dest) throws IOException {
    manager.sendPacket(dest, getID(), pack);
  }
  
  /**
   * Returns the id used in network communication for this {@link NetworkedObject}.
   * An id of -1 is used when an id has yet to assigned.
   * 
   * @return The id of this {@link NetworkedObject}
   */
  public final short getID() {
    return id;
  }
  
  /**
   * Returns the attached {@link NetworkManager} for this {@link NetworkedObject}.
   * If no manager has been attached then this method will return null.
   * 
   * @return The attached {@link NetworkManager}.
   */
  public final NetworkManager getNetworkManager() {
    return manager;
  }
  
  /**
   * Attaches a {@link NetworkManager} to this {@link NetworkedObject}.
   * 
   * @param man The {@link NetworkManager} to attach.
   */
  protected final void attachManager(NetworkManager man) {
    manager = man;
    managerAttached();
  }
  
  /**
   * Called when the {@link NetworkManager} for this {@link NetworkedObject} has
   * successfully connected to a remote {@link NetworkManager} and been given an
   * id for this {@link NetworkedObject}.
   */
  protected abstract void connected();
  
  /**
   * Called when a {@link DatagramPacket} is received for this {@link NetworkedObject}.
   * 
   * @param pack The received {@link DatagramPacket}.
   */
  protected abstract void messageRecieved(DatagramPacket pack);
  
  /**
   * Called when a {@link NetworkManager} has discovered and attached to this
   * {@link NetworkedObject}.
   */
  protected abstract void managerAttached();
  
  /**
   * Called when a client is connected to the {@link NetworkManager} managing this
   * {@link NetworkedObject}.
   * 
   * @param clientkey An object that acts as an identifier for the new client.
   */
  protected void clientConnected(Object clientkey) {}
  
  /**
   * Called when a client is disconnected from the {@link NetworkManager} managing
   * this {@link NetworkedObject}.
   * 
   * @param clientkey An object that acts as an identifier for the disconnect client.
   */
  protected void clientDisconnected(Object clientkey) {}
  
  /**
   * The id for this object.  This should only be changed by the {@link NetworkManager}.
   */
  protected short id;
  private NetworkManager manager;
}
