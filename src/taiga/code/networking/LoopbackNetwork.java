package taiga.code.networking;

import java.net.InetAddress;

/**
 * A very simple implementation of a {@link NetworkManager}.  This network does not
 * communicate across anything and instead returns all {@link Packet}s it receives
 * back to the sender immediately. This can be used to emulate a network for testing
 * or other purposes.
 * 
 * @author russell
 */
public class LoopbackNetwork extends NetworkManager {

  /**
   * Creates a new {@link LoopbackNetwork} using the given name.
   * @param name 
   */
  public LoopbackNetwork(String name) {
    super(name);
  }

  @Override
  public boolean isServer() {
    return true;
  }

  @Override
  public boolean isClient() {
    return true;
  }

  @Override
  public boolean isConnected() {
    return true;
  }

  @Override
  protected void sendPacket(InetAddress dest, Packet msg) {
    packetRecieved(msg);
  }
  
}
