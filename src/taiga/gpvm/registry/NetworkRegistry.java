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

package taiga.gpvm.registry;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.networking.NetworkManager;
import taiga.code.networking.NetworkedObject;
import taiga.code.util.ByteUtils;

/**
 * A {@link Registry} that synchronizes itself to other {@link NetworkRegistry}s
 * connected through the {@link NetworkManager}.
 * 
 * @author russell
 * @param <T> The type of object that this {@link NetworkRegistry} will use.
 */
public class NetworkRegistry<T extends RegistryEntry> extends Registry<T> {
  private static final long serialVersionUID = 1L;

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
    
    public void syncEntry(String ename) throws IOException {
      byte[] enbytes;
      
      enbytes = ename.getBytes(NetworkManager.NETWORK_CHARSET);
      
      byte[] data = new byte[enbytes.length + 1];
      data[0] = SYNC_REQ;
      System.arraycopy(enbytes, 0, data, 1, enbytes.length);
      
      sendMessage(data);
    }

    @Override
    protected void connected() {
      try {
        for(RegistryEntry ent : getEntries())
          syncEntry(ent.name);
        
        waitForSync();
      } catch (IOException ex) {
        throw new UnsupportedOperationException();
      }
    }

    @Override
    protected void messageRecieved(Object remote, byte[] pack) {
      switch(pack[0]) {
        case SYNC_REQ:
          receiveSyncRequest(remote, pack);
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
    
    private void receiveSyncResponse(byte[] pack) {
      String ename;
      
      ename = new String(pack, 5, pack.length - 5, NetworkManager.NETWORK_CHARSET);
      
      int id = ByteUtils.toInteger(pack, 1);
      
      RegistryEntry entry = getEntry(ename);
      if(entry == null) {
        log.log(Level.SEVERE, UNKNOWN_ENTRY, ename);
      } else {
        entries.put(id, getEntry(ename));
        
        log.log(Level.FINE, ASSIGNED_ID, new Object[] {ename, id});
      }
    }
    
    private void receiveSyncRequest(Object remote, byte[] pack) {
      String ename;
      
      ename = new String(pack, 1, pack.length - 1, NetworkManager.NETWORK_CHARSET);
      
      RegistryEntry entry = getEntry(ename);
      if(entry == null) {
        log.log(Level.SEVERE, UNKNOWN_ENTRY, ename);
        return;
      }
      
      int eid = entry.getID();
      
      byte[] data = new byte[pack.length + 4];
      
      data[0] = SYNC_RES;
      ByteUtils.toBytes(eid, 1, data);
      System.arraycopy(pack, 1, data, 5, pack.length - 1);
      
      try {
        sendMessage(data, remote);
      } catch (IOException ex) {
        throw new UnsupportedOperationException();
      }
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
