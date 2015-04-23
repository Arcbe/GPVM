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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.registration.NamedObject;
import taiga.code.text.TextLocalizer;
import taiga.code.util.ByteUtils;

/**
 * Manages networking for the registration tree. This will find all {@link NetworkedObject}s
 * it can find allowing them to communicate across a network.
 * 
 * @author russell
 */
public abstract class NetworkManager extends NamedObject {
  /**
   * The {@link Charset} that will be used to encode {@link Strings} into bytes.
   */
  public static final Charset NETWORK_CHARSET = Charset.forName("UTF-16");
  /**
   * The id for all {@link NetworkManager}s when communicating between managers.
   */
  public static final short NETWORK_MANAGER_ID = 0;
  /**
   * The default timeout for network operation in milliseconds.
   */
  public static final int DEFAULT_TIMEOUT = 3000;
  private static final long serialVersionUID = 1L;

  /**
   * Creates a new {@link NetworkManager} with the given name.
   * 
   * @param name The name for the {@link Network}.
   */
  public NetworkManager(String name) {
    super(name);
    
    index = new HashMap<>(0);
    objects = new HashMap<>(0);
    curstate = State.initialized;
  }
  
  /**
   * Scans the registration tree that this {@link NetworkManager} is a part of
   * to find {@link NetworkedObject}s.  This method must be called in order for
   * any of the {@link NetworkedObject} to be attached to the {@link NetworkManager}.
   * This method will also block until all of the IDs are synced, or it times out.
   * 
   * @throws java.util.concurrent.TimeoutException Thrown if the id synchronization
   *  times out.
   */
  public void scanRegisteredObjects() throws TimeoutException, IOException {
    log.log(Level.FINEST, "scanRegistereObjects()");
    
    ArrayList<NetworkedObject> rawobjs = new ArrayList<>(0);
    objects.clear();
    
    NamedObject root = getRoot();
    
    scan(root, rawobjs);
    
    rawobjs.stream().forEach((obj) -> {
      objects.put(obj.getFullName(), obj);
      obj.attachManager(this);
    });
    
    log.log(Level.INFO, "Found {0} network objects.", rawobjs.size());
    
    //if this a server we need to assign ids for the objects regardless of whether
    //this is also a client.
    if(isServer()) {
      generateIDs();
    } else if(isConnected()) {
      syncIDs();
    }
  }
  
  /**
   * Returns the current time out duration for network operations.
   * 
   * @return The current time out duration.
   */
  public int getTimeout() {
    return DEFAULT_TIMEOUT;
  }
  
  /**
   * Returns a hash value for the attached {@link NetworkedObject}s.  This will
   * provide a value to check compatibility between {@link NetworkManager}s, if
   * the hashes match then the two manager will probably be able to connect to
   * each other, otherwise they will not be able to connect.
   * 
   * @return a hash value for the names of the attached {@link NetworkedObject}s
   */
  public long getObjectHash() {
    long hash = 1;
    
    //hash all of the object names together to see if this manager can connect
    //to a remote manager.
    for(String obj : objects.keySet()) {
      long h = obj.hashCode();
      //try to reduce the number of factors in the hashes by making them all odd.
      if(h % 2 == 0) h++;
      
      hash *= h;
    }
    
    return hash;
  }
  
  /**
   * Sends a packet to the given destination.
   * 
   * @param dest The key object associated with the desire remote client.
   * @param sysid The ID number of the {@link NetworkedObject} that is sending the
   *  {@link DatagramPacket}.
   * @param msg The data packet to send.
   * @throws java.io.IOException Thrown if the message cannot be sent for any reason.
   */
  protected abstract void sendPacket(Object dest, short sysid, byte[] msg) throws IOException;
  
  /**
   * Called when a {@link Packet} is received from the network.
   * 
   * @param remote The key for the remote client that sent the packet, or null if
   *  this is a client manager.
   * @param pack The {@link DatagramPacket} that was received.
   * @param sysid The ID number of the system that the {@link DatagramPacket} is
   * for.
   */
  protected void packetRecieved(Object remote, byte[] pack, short sysid) {
    if(sysid == 0) {
      switch(pack[0]) {
        case SYNC_REQ:
          receiveSyncRequest(remote, pack);
          return;
        case SYNC_RES:
          receiveSyncResponse(pack);
          return;
        case HASH_CHECK:
          receiveHashCheck(remote, pack);
          return;
        case VALID_HASH:
          curstate = State.validated;
          synchronized (this) { this.notifyAll(); }
          return;
        case INVALID_RES:
          curstate = State.initialized;
          synchronized (this) { this.notifyAll(); }
          return;
        default:
          log.log(Level.SEVERE, "Unknown packet type {0} received.", pack[0]);
          return;
      }
    }
    
    NetworkedObject obj = index.get(sysid);
    
    if(obj == null) {
      log.log(Level.SEVERE, UNKNOWN_PACKET_ID, sysid);
      return;
    }
    
    obj.messageRecieved(remote, pack);
  }
  
  private void scan(NamedObject root, List<NetworkedObject> list) {
    if(root instanceof NetworkedObject) {
      log.log(Level.INFO, FOUND_OBJECT, root.getFullName());
      
      list.add((NetworkedObject) root);
    }
    
    for(NamedObject obj : root)
      scan(obj, list);
  }
  
  private void generateIDs() {
    log.log(Level.FINEST, "generateIDs()");
    curstate = State.validated;
    
    short curid = 1;
    index.clear();

    for(NetworkedObject obj : objects.values()) {
      curid++;
      obj.id = curid;
      index.put(obj.id, obj);

      log.log(Level.FINE, "Network object {0} assigned id {1}.", new Object[]{obj.getFullName(), obj.id});
    }

    //ids are correct since they are created locally, and no further action is
    //required.
    curstate = State.connected;
  }
  
  private synchronized void syncIDs() throws TimeoutException, IOException {
    if(!isConnected() || !isClient() || objects.isEmpty()) return;
    
    log.log(Level.INFO, "Synchronizing with remote server.");
    
    checkCompatibility();
    
    index.clear();
    objects.values().stream().forEach((obj) -> {
      try {
        sendSyncRequest(obj);
      } catch (IOException ex) {
        log.log(Level.SEVERE, "Exception sending request for object synchronization.", ex);
      }
    });

    long timeout = System.currentTimeMillis() + getTimeout();
    while(System.currentTimeMillis() <= timeout) {
      
      try {
        this.wait(getTimeout());
      } catch(InterruptedException ex) {
        if(curstate == State.connected) break;
      }
    }
    
    if(curstate != State.connected)
      throw new TimeoutException(TextLocalizer.localize(SYNC_TIMEOUT_EX));
    
    log.log(Level.INFO, "Network ids synchronized with remote server.");
  }
  
  private synchronized void checkCompatibility() throws TimeoutException, IOException {
    long hash = getObjectHash();
    log.log(Level.FINEST, "checkCompatibility() : {0}", Long.toHexString(hash).toUpperCase());
    curstate = State.bound;
    
    //send the hash check packet
    byte[] pack = ByteUtils.toBytes(hash, 1, new byte[9]);
    pack[0] = HASH_CHECK;
    sendPacket(null, NETWORK_MANAGER_ID, pack);
    
    //wait for the response.
    long timeout = System.currentTimeMillis() + getTimeout();
    while(System.currentTimeMillis() < timeout) {
      try {
        this.wait(timeout - System.currentTimeMillis());
      } catch(InterruptedException ex) {
        if(curstate == State.validated) return;
      }
    }
    
    throw new TimeoutException("Timed out waiting for hash check with server.");
  }
  
  private void sendSyncRequest(NetworkedObject obj) throws IOException {
    byte[] str;
    
    str = obj.getFullName().getBytes(NETWORK_CHARSET);
    
    byte[] data = new byte[str.length + 1];
    data[0] = SYNC_REQ;
    data = new byte[str.length + 1];
    
    System.arraycopy(str, 0, data, 1, str.length);
    DatagramPacket p;
    
    sendPacket(null, NETWORK_MANAGER_ID, data);
  }
  
  private void receiveHashCheck(Object remote, byte[] data) {
    long hash = ByteUtils.toLong(data, 1);
    
    try {
      boolean valid = hash == getObjectHash();
      sendPacket(remote, NETWORK_MANAGER_ID, new byte[]{(valid ? VALID_HASH : INVALID_RES)});
      
      if(valid) log.log(Level.INFO, "Valid client hash received from {0}.", remote);
      else log.log(Level.WARNING, "Invalid hash {0} received from client {1}.  Expected hash {2}", new Object[]{
        Long.toHexString(hash).toUpperCase(),
        remote,
        Long.toHexString(getObjectHash()).toUpperCase()});
    } catch (IOException ex) {
      Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, "Exception while responding to hash check.", ex);
    }
  }
  
  private void receiveSyncResponse(byte[] pack) {
    NetworkedObject obj;
    String oname;
    
    oname = new String(pack, 3, pack.length - 3, NETWORK_CHARSET);
    obj = objects.get(oname);
    
    short id = ByteUtils.toShort(pack, 1);
    
    index.put(id, obj);
    obj.id = id;
    
    log.log(Level.FINE, ID_ASSIGNED, new Object[] {obj.getFullName(), id});
    
    //check if this is the last sync response.
    if(index.size() == objects.size()) {
      fireConnected();
      synchronized(this) { this.notifyAll(); }
    }
  }
  
  private void receiveSyncRequest(Object remote, byte[] pack) {
    String oname;
    
    oname = new String(pack, 1, pack.length - 1, NETWORK_CHARSET);
    
    short id = objects.get(oname).id;
    
    //syncresponse is the id followed by the encoded string.
    //there are two more bytes needed for the objects id.
    byte[] data = new byte[pack.length + 2];
    data[0] = SYNC_RES;
    
    System.arraycopy(ByteUtils.toBytes(id), 0, data, 1, 2);
    System.arraycopy(pack, 1, data, 3, pack.length - 1);
    
    try {
      sendPacket(remote, NETWORK_MANAGER_ID, data);
    } catch (IOException ex) {
      Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, "Exception responding to object synchronization request.", ex);
    }
  }
  
  private void fireConnected() {
    curstate = State.connected;
    
    objects.values().stream().forEach((obj) -> {
      obj.connected();
    });
  }
  
  protected void fireClientConnected(Object clientkey) {
    objects.values().stream().forEach((NetworkedObject client) -> {
      client.clientConnected(clientkey);
    });
  }
  
  protected void fireClientDisconnect(Object clientkey) {
    objects.values().stream().forEach((NetworkedObject client) -> {
      client.clientDisconnected(clientkey);
    });
  }
  
  /**
   * This method should be called when this {@link NetworkManager} connects to a
   * server and is ready to begin synchronizing ids.
   * 
   * @throws java.util.concurrent.TimeoutException Thrown if this operation times
   * out. The time out duration is determined by the
   * @throws IOException If there is an exception while trying to send a datagram
   * to the server.
   */
  protected void connect() throws TimeoutException, IOException {
    syncIDs();
    
    fireConnected();
  }
  
  private final Map<String, NetworkedObject> objects;
  private final Map<Short, NetworkedObject> index;
  private State curstate;
  
  private enum State {
    initialized,
    bound,
    validated,
    connected
  }
  
  /**
   * Returns whether this {@link NetworkManager} is acting as a server.  A
   * {@link NetworkManager} may also act as a client regardless of whether its a
   * server too.
   * 
   * @return Whether this {@link NetworkManager} is a server.
   */
  public abstract boolean isServer();
  
  /**
   * Returns whether this {@link NetworkManager} is acting as a client.  A
   * {@link NetworkManager} may also act as a server regardless of whether its a
   * client too.
   * 
   * @return Whether this {@link NetworkManager} is a server.
   */
  public abstract boolean isClient();
  
  /**
   * Checks whether this {@link NetworkManager} is connected to a network.  For
 a client this means that it is connected to a server, and for a server this
 mean that it is ready to accept clients.
   * 
   * @return Whether this {@link NetworkManager} is connected to a network.
   */
  public abstract boolean isConnected();
  
  // id for a sync request, this packet has a single string encoded as bytes.
  private static final byte SYNC_REQ = 0;
  //id for a sync response, this has an short id followed by a single string.
  private static final byte SYNC_RES = 1;
  //id for a check of the hash values between the server and client.
  private static final byte HASH_CHECK = 2;
  //id for a signal that the client and server are not compatible.
  private static final byte INVALID_RES = 3;
  //id for a signal that the hash values match.
  private static final byte VALID_HASH = 4;
  
  private static final String locprefix = NetworkManager.class.getName().toLowerCase();
  
  private static final String ENCODING_ERR = locprefix + ".encoding_err";
  private static final String FOUND_OBJECT = locprefix + ".found_network_object";
  private static final String ID_ASSIGNED = locprefix + ".id_assigned";
  private static final String IDS_SYNCHRONIZED = locprefix + ".ids_synchronized";
  private static final String SYNC_TIMEOUT_EX = locprefix + ".sync_timeout_ex";
  private static final String UNKNOWN_PACKET_ID = locprefix + ".unknown_packet_id";
  
  private static final Logger log = Logger.getLogger(locprefix, 
    System.getProperty("taiga.code.logging.text"));
}
