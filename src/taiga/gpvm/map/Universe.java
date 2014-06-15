package taiga.gpvm.map;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import taiga.gpvm.HardcodedValues;
import java.util.logging.Logger;
import sun.security.pkcs.EncodingException;
import taiga.code.networking.NetworkManager;
import taiga.code.networking.NetworkedObject;
import taiga.code.networking.Packet;
import taiga.code.registration.RegisteredObject;
import taiga.code.registration.ReusableObject;
import taiga.code.util.ByteUtils;
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
    super(HardcodedValues.UNIVERSE_NAME);
    
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
    Comms comms = getObject(HardcodedValues.COMMS_NAME);
    removeAllChildren();
    addChild(comms);
    
    listeners.clear();
    index.clear();
  }
  
  private void clearWorlds() {
    for(RegisteredObject obj : this) {
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
    
    for(RegisteredObject obj : this)
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
      super(HardcodedValues.COMMS_NAME);
    }
    
    @Override
    protected void connected() {
      clearWorlds();
      
      Packet request = new Packet();
      request.data = new byte[]{NAME_REQ};
      sendMessage(request);
    }

    @Override
    protected void messageRecieved(Packet pack) {
      switch(pack.data[0]) {
        case NAME_REQ:
          receiveNameRequest(pack);
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
    
    private void receiveNameRequest(Packet pack) {
      for(World target : index.values()) {
        byte[] nameb;
        try {
          nameb = target.name.getBytes(NetworkManager.NETWORK_CHARSET);
        } catch (UnsupportedEncodingException ex) {
          log.log(Level.SEVERE, null, ex);
          continue;
        }
      
        //assemble the response packet.
        Packet res = new Packet();
        res.data = new byte[nameb.length + 3];

        res.data[0] = NAME_RES;
        ByteUtils.toBytes(target.getWorldID(), 1, res.data);
        System.arraycopy(nameb, 0, res.data, 3, nameb.length);

        sendMessage(res, pack.source);
      }
    }
    
    private void receiveNameResponse(Packet pack) {
      //decode the incoming packet
      String name;
      short id = ByteUtils.toShort(pack.data, 1);
      try {
        name = new String(pack.data, 3, pack.data.length - 3, NetworkManager.NETWORK_CHARSET);
      } catch (UnsupportedEncodingException ex) {
        log.log(Level.SEVERE, BAD_PACKET, new Object[] {pack.source, ex});
        return;
      }
      
      World target = getObject(name);
      if(target == null) {
        target = addWorld(name, null);
      }
      
      setID(target, id);
    }
    
    private void receiveRegionRequest(Packet pack) {
      Coordinate coor = new Coordinate();
      
      coor.x = ByteUtils.toInteger(pack.data, 1);
      coor.y = ByteUtils.toInteger(pack.data, 5);
      coor.z = ByteUtils.toInteger(pack.data, 9);
      short wid = ByteUtils.toShort(pack.data, 13);
      
      Region reg = index.get(wid).getRegion(coor);
      
      byte[] regdata = reg.toBytes();
      byte[] data = new byte[regdata.length + 15];
      
      data[0] = REG_RES;
      System.arraycopy(pack.data, 1, data, 1, 14);
      System.arraycopy(regdata, 0, data, 15, regdata.length);
      
      Packet res = new Packet();
      res.data = data;
      sendMessage(pack);
    }
    
    private void receiveRegionResponse(Packet pack) {
      
    }
    
    protected static final int NAME_REQ = 0;
    protected static final int NAME_RES = 1;
    protected static final int REG_REQ = 2;
    protected static final int REG_RES = 3;
  }
}
