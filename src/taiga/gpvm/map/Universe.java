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

package taiga.gpvm.map;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.networking.NetworkManager;
import taiga.code.networking.NetworkedObject;
import taiga.code.registration.NamedObject;
import taiga.code.registration.ReusableObject;
import taiga.code.util.ByteUtils;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.util.geom.Coordinate;

/**
 * This class manages a collection of {@link World}s and provides events for
 * the creation and destruction of contained {@link World}s.
 * @author russell
 */
public final class Universe extends ReusableObject {
  
  /**
   * Creates a new empty {@link Universe}.
   */
  public Universe() {
    super(HardcodedValues.NAME_UNIVERSE);
    
    listeners = new HashSet<>();
    index = new TreeMap<>();
    
    addChild(new Comms());
  }
  
  /**
   * Adds a new {@link World} to this {@link Universe} with the given {@link MapGenerator}.
   * 
   * @param name The name for the new {@link World}.
   * @param gen The {@link MapGenerator} for the {@link World}
   */
  public World addWorld(String name, MapGenerator gen) {
    World nworld = new World(name);
    nworld.addChild(gen);
    
    addChild(nworld);
    
    assignID(nworld);
    
    fireWorldCreated(nworld);
    
    return nworld;
  }
  
  /**
   * Adds a {@link UniverseListener} to this {@link Universe}.
   * 
   * @param list The listener to add.
   */
  public void addListener(UniverseListener list) {
    listeners.add(list);
  }
  
  /**
   * Removes a {@link UniverseListener} from this {@link Universe} that was previously
   * added.
   * 
   * @param list The {@link UniverseListener} to remove.
   */
  public void removeListener(UniverseListener list) {
    listeners.remove(list);
  }

  @Override
  protected void resetObject() {
    Comms comms = getObject(HardcodedValues.NAME_COMMS);
    removeAllChildren();
    addChild(comms);
    
    listeners.clear();
    index.clear();
  }
  
  private void clearWorlds() {
    for(NamedObject obj : this) {
      if(!(obj instanceof World)) continue;
      World world = (World) obj;
      
      removeChild(obj);
      world.reset();
    }
  }
    
  private void setID(World world, short id) {
    world.setID(id);
    index.put(id, world);

    log.log(Level.FINE, WORLD_ID_SET, new Object[] {world, id});
  }
  
  private void assignID(World world) {
    short newid = 0;
    LinkedList<World> worlds = new LinkedList<>();
    
    for(NamedObject obj : this)
      if(obj instanceof World)
        worlds.add((World) obj);
    
    while(!worlds.isEmpty()) {
      Iterator<World> it = worlds.iterator();
      boolean goodid = true;
      
      while(it.hasNext()) {
        if(it.next().getWorldID() == newid) {
          it.remove();
          newid++;
          goodid = false;
          
          break;
        }
      }
      
      if(goodid)
        break;
    }
    
    setID(world, newid);
  }
  
  private void fireWorldCreated(World world) {
    for(UniverseListener list : listeners)
      list.worldCreated(world);
  }
  
  private final Collection<UniverseListener> listeners;
  private final Map<Short, World> index;
  
  private static final String locprefix = Universe.class.getName().toLowerCase();
  
  private static final String BAD_PACKET = locprefix + ".bad_packet";
  private static final String WORLD_ID_SET = locprefix + ".world_id_set";
  
  private static final Logger log = Logger.getLogger(locprefix, System.getProperty("taiga.code.logging.text"));

  /**
   * Provides network access for the game universe.  This class will handle requests
   * for world names as well as requests for region data.
   */
  protected final class Comms extends NetworkedObject {

    public Comms() {
      super(HardcodedValues.NAME_COMMS);
    }
    
    @Override
    protected void connected() {
      try {
        clearWorlds();
        
        sendMessage(new byte[]{NAME_REQ});
      } catch (IOException ex) {
        throw new UnsupportedOperationException();
      }
    }

    @Override
    protected void messageRecieved(Object remote, byte[] pack) {
      switch(pack[0]) {
        case NAME_REQ:
          receiveNameRequest(remote, pack);
          break;
        case NAME_RES:
          receiveNameResponse(pack);
          break;
        case REG_REQ:
          receiveRegionRequest(pack);
          break;
        case REG_RES:
          receiveRegionResponse(pack);
          break;
      }
    }

    @Override
    protected void managerAttached() {
    }
    
    private void receiveNameRequest(Object remote, byte[] pack) {
      for(World target : index.values()) {
        byte[] nameb;
        
        nameb = target.name.getBytes(NetworkManager.NETWORK_CHARSET);
      
        //assemble the response packet.
        byte[] data = new byte[nameb.length + 3];

        data[0] = NAME_RES;
        ByteUtils.toBytes(target.getWorldID(), 1, data);
        System.arraycopy(nameb, 0, data, 3, nameb.length);
        try {
          sendMessage(data, remote);
        } catch (IOException ex) {
          throw new UnsupportedOperationException();
        }
      }
    }
    
    private void receiveNameResponse(byte[] pack) {
      //decode the incoming packet
      String name;
      short id = ByteUtils.toShort(pack, 1);
      
      name = new String(pack, 3, pack.length - 3, NetworkManager.NETWORK_CHARSET);
      
      World target = getObject(name);
      if(target == null) {
        target = addWorld(name, null);
      }
      
      setID(target, id);
    }
    
    private void receiveRegionRequest(byte[] pack) {
      Coordinate coor = new Coordinate();
      
      coor.x = ByteUtils.toInteger(pack, 1);
      coor.y = ByteUtils.toInteger(pack, 5);
      coor.z = ByteUtils.toInteger(pack, 9);
      short wid = ByteUtils.toShort(pack, 13);
      
      Region reg = index.get(wid).getRegion(coor);
      
      byte[] regdata = RegionEncoder.encode(reg);
      byte[] data = new byte[regdata.length + 15];
      
      data[0] = REG_RES;
      System.arraycopy(pack, 1, data, 1, 14);
      System.arraycopy(regdata, 0, data, 15, regdata.length);
      try {
        sendMessage(data);
      } catch (IOException ex) {
        throw new UnsupportedOperationException();
      }
    }
    
    private void receiveRegionResponse(byte[] pack) {
      
    }
    
    protected static final int NAME_REQ = 0;
    protected static final int NAME_RES = 1;
    protected static final int REG_REQ = 2;
    protected static final int REG_RES = 3;
  }
}
