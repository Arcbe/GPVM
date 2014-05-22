package taiga.code.networking;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.registration.RegisteredObject;

/**
 * Manages networking for the registration tree. This will find all {@link NetworkedObject}s
 * it can find allowing them to communicate across a network.
 * 
 * @author russell
 */
public abstract class NetworkManager extends RegisteredObject {
  
  public static final String NETWORK_CHARSET = "UTF-16";
  public static final short NETWORK_MANAGER_ID = 0;

  public NetworkManager(String name) {
    super(name);
    
    current = 0;
    history = new Packet[256];
  }
  
  public void scanRegisteredObjects() {
    ArrayList<NetworkedObject> rawobjs = new ArrayList<>();
    objects = new HashMap<>();
    index = new HashMap<>();
    
    RegisteredObject root = this;
    while(root.getParent() != null)
      root = root.getParent();
    
    scan(root, rawobjs);
    
    log.log(Level.INFO, SCAN_COMPLETE, rawobjs.size());
    
    //if this a server we need to assign ids for the objects regardless of whether
    //this is also a client.
    if(isServer()) {
      short curid = 1;
      
      for(NetworkedObject obj : rawobjs) {
        obj.id = curid++;
        objects.put(obj.getFullName(), obj);
        obj.attachManager(this);
        index.put(obj.id, obj);
        
        log.log(Level.FINEST, ID_ASSIGNED, new Object[]{obj.getFullName(), obj.id});
      }
      
      synced = true;
    } else {
      for(NetworkedObject obj : rawobjs) {
        objects.put(obj.getFullName(), obj);
        obj.attachManager(this);
        
        sendSyncRequest(obj);
      }
      
      synced = false;
    }
    
  }
  
  public void sendMessage(InetAddress dest, Packet msg) {
    //make sure that there are nopackets with duplicate numbers.
    synchronized(this) {
      msg.number = current++;
    }
    
    history[msg.number + 128] = msg;
    
    sendPacket(dest, msg);
  }
  
  public abstract boolean isServer();
  public abstract boolean isClient();
  public abstract boolean isConnected();
  
  protected abstract void sendPacket(InetAddress dest, Packet msg);
  
  protected void packetRecieved(Packet pack) {
    if(pack.target == 0) {
      switch(pack.data[0]) {
        case SYNC_VAL:
          recieveSyncRequest(pack);
          return;
      }
    }
    
    NetworkedObject obj = index.get(pack.target);
    
    obj.messageRecieved(pack);
    
    //TODO: add packet resend code.
  }
  
  private Map<String, NetworkedObject> objects;
  private Map<Short, NetworkedObject> index;
  private boolean synced;
  
  private Packet[] history;
  private byte current;
  
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
    
    System.arraycopy(str, 0, req.data, 1, str.length);
    
    sendPacket(null, req);
  }
  
  private void recieveSyncRequest(Packet pack) {
    String name;
    try {
      name = new String(pack.data, 1, pack.data.length - 1, NETWORK_CHARSET);
    } catch (UnsupportedEncodingException ex) {
      log.log(Level.SEVERE, ENCODING_ERR, ex);
      
      return;
    }
    
    int id = objects.get(name).id;
    
    Packet response = new Packet();
    
  }
  
  private enum PacketType {
    //sends a packet composed of a single arbitrary string corresponding to a
    //networked object in order to synchorize the ids across the network.
    sync(SYNC_VAL);
    
    private final byte value;

    private PacketType(byte val) {
      value = val;
    }
    
    public int getValue() {
      return value;
    }
    
    public static PacketType valToType(byte val) {
      switch (val) {
        case 0: return sync;
        default: return null;
      }
    }
  }
  
  private static final byte SYNC_VAL = 0;
  
  private static final String locprefix = NetworkManager.class.getName().toLowerCase();
  
  private static final String ENCODING_ERR = locprefix + ".encoding_err";
  private static final String FOUND_OBJECT = locprefix + ".found_network_object";
  private static final String SCAN_COMPLETE = locprefix + ".scan_complete";
  private static final String ID_ASSIGNED = locprefix + ".id_assigned";
  
  private static final Logger log = Logger.getLogger(locprefix, 
    System.getProperty("taiga.code.logging.text"));
}
