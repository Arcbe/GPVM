package taiga.code.networking;

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

  public NetworkedObject(String name) {
    super(name);
    
    id = -1;
  }
  
  public void sendMessage(Packet pack) {
    
  }
  
  public final int getID() {
    return id;
  }
  
  public final NetworkManager getManager() {
    return manager;
  }
  
  protected final void attachManager(NetworkManager man) {
    manager = man;
    managerAttached();
  }
  
  protected abstract void connected();
  protected abstract void messageRecieved(Packet pack);
  protected abstract void managerAttached();
  
  protected short id;
  private NetworkManager manager;
}
