package taiga.code.networking;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.registration.RegisteredObject;
import taiga.code.text.TextLocalizer;
import taiga.code.util.ByteUtils;

/**
 * Manages networking for the registration tree. This will find all {@link NetworkedObject}s
 * it can find allowing them to communicate across a network.
 * 
 * @author russell
 */
public abstract class NetworkManager extends RegisteredObject {
  /**
   * The charset that will be used to encode {@link Strings} into bytes.
   */
  public static final String NETWORK_CHARSET = "UTF-16";
  /**
   * The id for all {@link NetworkManager}s when communicating between managers.
   */
  public static final short NETWORK_MANAGER_ID = 0;
  /**
   * The default timeout for network operation in milliseconds.
   */
  public static final int DEFAULT_TIMEOUT = 3000;

  /**
   * Creates a new {@link NetworkManager} with the given name.
   * 
   * @param name The name for the {@link Network}.
   */
  public NetworkManager(String name) {
    super(name);
    
    current = 0;
    history = new Packet[256];
    index = new HashMap<>();
    objects = new HashMap<>();
  }
  
  /**
   * Scans the registration tree that this {@link NetworkManager} is a part of
   * to find {@link NetworkedObject}s.  This method must be called in order for
   * any of the {@link NetworkedObject} to be attached to the {@link NetworkManager}.
   * This method will also lock until all of the IDs are synced, or it times out.
   * 
   * @throws java.util.concurrent.TimeoutException Thrown if the id synchronization
   *  times out.
   */
  public void scanRegisteredObjects() throws TimeoutException {
    ArrayList<NetworkedObject> rawobjs = new ArrayList<>();
    objects.clear();
    
    RegisteredObject root = this;
    while(root.getParent() != null)
      root = root.getParent();
    
    scan(root, rawobjs);
    
    objects.clear();
    
    for(NetworkedObject obj : rawobjs) {
      objects.put(obj.getFullName(), obj);
      obj.attachManager(this);
    }
    
    log.log(Level.INFO, SCAN_COMPLETE, rawobjs.size());
    
    //if this a server we need to assign ids for the objects regardless of whether
    //this is also a client.
    if(isServer()) {
      generateIDs();
    } else if(isConnected()) {
      syncIDs();
      fireConnected();
    }
  }
  
  /**
   * Sends a {@link Packet} through this {@link NetworkManager} to the given 
   * destination.
   * 
   * @param dest The {@link InetAddress} to send the {@link Packet} to.
   * @param msg The {@link Packet} to send.
   */
  public void sendMessage(InetAddress dest, Packet msg) {
    //make sure that there are nopackets with duplicate numbers.
    synchronized(this) {
      msg.number = current++;
    }
    
    history[msg.number] = msg;
    
    sendPacket(dest, msg);
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
   * a client this means that it is connected to a server, and for a server this
   * mean that it is ready to accept clients.
   * 
   * @return Whether this {@link NetworkManager} is connected to a network.
   */
  public abstract boolean isConnected();
  
  /**
   * Sends a packet to the given destination.
   * 
   * @param dest The destination for the packet.
   * @param msg The packet to send.
   */
  protected abstract void sendPacket(InetAddress dest, Packet msg);
  
  /**
   * Called when a {@link Packet} is received from the network.
   * @param pack 
   */
  protected void packetRecieved(Packet pack) {
    if(pack.target == 0) {
      switch(pack.data[0]) {
        case SYNC_REQ:
          receiveSyncRequest(pack);
          return;
        case SYNC_RES:
          receiveSyncResponse(pack);
          return;
      }
    }
    
    NetworkedObject obj = index.get(pack.target);
    
    if(obj == null) {
      log.log(Level.SEVERE, UNKNOWN_PACKET_ID, pack.target);
      return;
    }
    
    obj.messageRecieved(pack);
    
    //TODO: add packet resend code.
  }
  
  /**
   * This method should e called when this {@link NetworkManager} connects to a
   * server.
   * 
   * @throws java.util.concurrent.TimeoutException Thrown if this operation times
   * out. The time out duration is determined by the
   */
  protected void connected() throws TimeoutException {
    syncIDs();
    
    fireConnected();
  }
  
  private void generateIDs() {
    short curid = 1;
    index.clear();

    for(NetworkedObject obj : objects.values()) {
      obj.id = curid++;
      index.put(obj.id, obj);

      log.log(Level.FINEST, ID_ASSIGNED, new Object[]{obj.getFullName(), obj.id});
    }

    synced = true;
  }
  
  private void syncIDs() throws TimeoutException {
    if(!isConnected() || !isClient() || objects.isEmpty()) return;
    
    index.clear();

    for(NetworkedObject obj : objects.values()) {

      sendSyncRequest(obj);
    }

    long timeout = System.currentTimeMillis() + getTimeout();
    while(System.currentTimeMillis() <= timeout && !synced) {
      
      try {
        synchronized(this) {
          this.wait(getTimeout());
        }
      } catch(InterruptedException ex) {
        if(synced) return;
      }
    }
    
    if(!synced)
      throw new TimeoutException(TextLocalizer.localize(SYNC_TIMEOUT_EX));
    
    log.log(Level.INFO, IDS_SYNCHRONIZED);
  }
  
  private void scan(RegisteredObject root, List<NetworkedObject> list) {
    if(root instanceof NetworkedObject) {
      log.log(Level.INFO, FOUND_OBJECT, root.getFullName());
      
      list.add((NetworkedObject) root);
    }
    
    for(RegisteredObject obj : root)
      scan(obj, list);
  }
  
  private void sendSyncRequest(NetworkedObject obj) {
    Packet req = new Packet();
    byte[] str;
    
    try {
      str = obj.getFullName().getBytes(NETWORK_CHARSET);
    } catch (UnsupportedEncodingException ex) {
      log.log(Level.WARNING, ENCODING_ERR, ex);
      
      //TODO: this might need changing.
      return;
    }
    
    req.target = NETWORK_MANAGER_ID;
    req.data = new byte[str.length + 1];
    req.data[0] = SYNC_REQ;
    
    System.arraycopy(str, 0, req.data, 1, str.length);
    
    sendPacket(null, req);
  }
  
  private void receiveSyncResponse(Packet pack) {
    NetworkedObject obj;
    String oname;
    try {
      oname = new String(pack.data, 3, pack.data.length - 3, NETWORK_CHARSET);
      obj = objects.get(oname);
    } catch (UnsupportedEncodingException ex) { 
      log.log(Level.SEVERE, ENCODING_ERR, ex);
      
      return;
    }
    
    short id = ByteUtils.toShort(pack.data, 1);
    
    index.put(id, obj);
    obj.id = id;
    
    log.log(Level.FINE, ID_ASSIGNED, new Object[] {obj.getFullName(), id});
    
    synced = index.size() == objects.size();
    if(synced) synchronized(this) {this.notifyAll();}
  }
  
  private void receiveSyncRequest(Packet pack) {
    String oname;
    try {
      oname = new String(pack.data, 1, pack.data.length - 1, NETWORK_CHARSET);
    } catch (UnsupportedEncodingException ex) {
      log.log(Level.SEVERE, ENCODING_ERR, ex);
      
      return;
    }
    
    short id = objects.get(oname).id;
    
    Packet response = new Packet();
    
    //syncresponse is the id followed by the encoded string.
    response.target = NETWORK_MANAGER_ID;
    
    //there are two more bytes needed for the objects id.
    response.data = new byte[pack.data.length + 2];
    response.data[0] = SYNC_RES;
    
    System.arraycopy(ByteUtils.toBytes(id), 0, response.data, 1, 2);
    System.arraycopy(pack.data, 1, response.data, 3, pack.data.length - 1);
    
    sendPacket(pack.source, response);
  }
  
  private void fireConnected() {
    for(NetworkedObject obj : objects.values())
      obj.connected();
  }
  
  private final Map<String, NetworkedObject> objects;
  private final Map<Short, NetworkedObject> index;
  private boolean synced;
  
  private final Packet[] history;
  private byte current;
  
  // id for a sync request, this packet has a single string encoded as bytes.
  private static final byte SYNC_REQ = 0;
  //id for a sync response, this has an short id followed by a single string.
  private static final byte SYNC_RES = 1;
  
  private static final String locprefix = NetworkManager.class.getName().toLowerCase();
  
  private static final String ENCODING_ERR = locprefix + ".encoding_err";
  private static final String FOUND_OBJECT = locprefix + ".found_network_object";
  private static final String SCAN_COMPLETE = locprefix + ".scan_complete";
  private static final String ID_ASSIGNED = locprefix + ".id_assigned";
  private static final String IDS_SYNCHRONIZED = locprefix + ".ids_synchronized";
  private static final String SYNC_TIMEOUT_EX = locprefix + ".sync_timeout_ex";
  private static final String UNKNOWN_PACKET_ID = locprefix + ".unknown_packet_id";
  
  private static final Logger log = Logger.getLogger(locprefix, 
    System.getProperty("taiga.code.logging.text"));
}
