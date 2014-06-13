/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.networking.NetworkManager;
import taiga.code.networking.NetworkedObject;
import taiga.code.networking.Packet;
import taiga.code.util.ByteUtils;

/**
 * A {@link Registry} that synchronizes itself to other {@link NetworkRegistry}s
 * connected through the {@link NetworkManager}.
 * 
 * @author russell
 * @param <T> The type of object that this {@link NetworkRegistry} will use.
 */
public class NetworkRegistry<T extends RegistryEntry> extends Registry<T> {

  /**
   * Creates a new {@link NetworkRegistry} with the given name.
   * @param name 
   */
  public NetworkRegistry(String name) {
    super(name);
    
    addChild(new Synchronizer());
  }
  
  private class Synchronizer extends NetworkedObject {
    
    public static final String NAME = "sync";

    public Synchronizer() {
      super(NAME);
    }
    
    public void syncEntry(String ename) {
      byte[] enbytes;
      try {
        enbytes = ename.getBytes(NetworkManager.NETWORK_CHARSET);
      } catch (UnsupportedEncodingException ex) {
        log.log(Level.SEVERE, SYNC_REQ_EX, ex);
        
        return;
      }
      
      byte[] data = new byte[enbytes.length + 1];
      data[0] = SYNC_REQ;
      System.arraycopy(enbytes, 0, data, 1, enbytes.length);
      
      Packet result = new Packet();
      result.data = data;
      sendMessage(result);
    }

    @Override
    protected void connected() {
      for(RegistryEntry ent : getEntries())
        syncEntry(ent.name);
      
      waitForSync();
    }

    @Override
    protected void messageRecieved(Packet pack) {
      switch(pack.data[0]) {
        case SYNC_REQ:
          receiveSyncRequest(pack);
          break;
        case SYNC_RES:
          receiveSyncResponse(pack);
          break;
      }
    }

    @Override
    protected void managerAttached() {
    }
    
    private void waitForSync() {
      //TODO: implement this
    }
    
    private void receiveSyncResponse(Packet pack) {
      String ename;
      try {
        ename = new String(pack.data, 5, pack.data.length - 5, NetworkManager.NETWORK_CHARSET);
      } catch (UnsupportedEncodingException ex) {
        log.log(Level.SEVERE, SYNC_REQ_EX, ex);
        
        return;
      }
      
      int id = ByteUtils.toInteger(pack.data, 1);
      
      RegistryEntry entry = getEntry(ename);
      if(entry == null) {
        log.log(Level.SEVERE, UNKNOWN_ENTRY, ename);
      } else {
        entries.put(id, getEntry(ename));
        
        log.log(Level.FINE, ASSIGNED_ID, new Object[] {ename, id});
      }
    }
    
    private void receiveSyncRequest(Packet pack) {
      String ename;
      try {
        ename = new String(pack.data, 1, pack.data.length - 1, NetworkManager.NETWORK_CHARSET);
      } catch (UnsupportedEncodingException ex) {
        log.log(Level.SEVERE, SYNC_REQ_EX, ex);
        
        return;
      }
      
      RegistryEntry entry = getEntry(ename);
      if(entry == null) {
        log.log(Level.SEVERE, UNKNOWN_ENTRY, ename);
        return;
      }
      
      int eid = entry.getID();
      
      byte[] data = new byte[pack.data.length + 4];
      
      data[0] = SYNC_RES;
      ByteUtils.toBytes(eid, 1, data);
      System.arraycopy(pack.data, 1, data, 5, pack.data.length - 1);
      
      Packet result = new Packet();
      result.data = data;
      sendMessage(result, pack.source);
    }
    
    private static final byte SYNC_REQ = 0;
    private static final byte SYNC_RES = 1;
  }
  
  private static final String locprefix = NetworkRegistry.class.getName().toLowerCase();
  
  private static final String SYNC_REQ_EX = locprefix + ".sync_req_ex";
  private static final String ASSIGNED_ID = locprefix + ".assigned_id";
  private static final String UNKNOWN_ENTRY = locprefix + ".unknown_entry";
  
  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
