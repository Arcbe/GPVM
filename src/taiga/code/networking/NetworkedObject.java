package taiga.code.networking;

import java.net.InetAddress;
import taiga.code.registration.RegisteredObject;

/**
 * An {@link Object} that is can communicate with a matching object over a network.
 * An identical {@link NetworkedObject} will need to be present in the remote 
 * program with the same full name as this one.  Packets
 * can then be sent between the objects, however only client server architecture
 * is supported.
 * 
 * @author russell
 */
public abstract class NetworkedObject extends RegisteredObject {

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
   * Sends a {@link Packet} through the attached {@link NetworkManager}.  This
   * method will only work if the manager is a client.  If the manager is not a
   * client then the {@link NetworkedObject#sendMessage(taiga.code.networking.Packet, java.net.InetAddress) }
   * should be used instead.
   * 
   * @param pack The {@link Packet} to send.
   */
  public void sendMessage(Packet pack) {
    pack.target = getID();
    
    manager.sendMessage(null, pack);
  }
  
  /**
   * Sends a {@link Packet} to a specific destination.  If the manager is a client
   * then the destination is ignored as {@link Packet}s can only go to the server.
   * 
   * @param pack The {@link Packet} to send.
   * @param dest The {@link InetAddress} of the destination.
   */
  public void sendMessage(Packet pack, InetAddress dest) {
    pack.target = getID();
    
    manager.sendMessage(dest, pack);
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
   * Called when a {@link Packet} is received for this {@link NetworkedObject}.
   * 
   * @param pack The received {@link Packet}.
   */
  protected abstract void messageRecieved(Packet pack);
  
  /**
   * Called when a {@link NetworkManager} has discovered and attached to this
   * {@link NetworkedObject}.
   */
  protected abstract void managerAttached();
  
  /**
   * The id for this object.  This should only be changed by the {@link NetworkManager}.
   */
  protected short id;
  private NetworkManager manager;
}
