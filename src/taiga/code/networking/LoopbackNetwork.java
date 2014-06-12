package taiga.code.networking;

import java.net.InetAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * A very simple implementation of a {@link NetworkManager}.  Sends all {@link Packet}s
 * to another linked {@link LoopbackNetwork}. This can be used to emulate a network
 * for testing purposes.
 * 
 * @author russell
 */
public class LoopbackNetwork extends NetworkManager {

  /**
   * Creates a new {@link LoopbackNetwork} using the given name.
   * 
   * @param name The name of the {@link LoopbackNetwork}
   * @param client Whether this {@link LoopbackNetwork} is a client.
   * @param server Whether this {@link LoopbackNetwork} is a server.
   */
  public LoopbackNetwork(String name, boolean server, boolean client) {
    super(name);
    
    this.client = client;
    this.server = server;
  }
  
  /**
   * Connects this {@link LoopbackNetwork} to the given {@link LoopbackNetwork}.
   * 
   * @param other The other {@link LoopbackNetwork} to connect to.
   * @throws TimeoutException Thrown if the connection takes to long to establish somehow.
   */
  public void connect(LoopbackNetwork other) throws TimeoutException {
    connection = other;
    other.connection = this;
    
    exe = Executors.newSingleThreadExecutor();
    other.exe = Executors.newSingleThreadExecutor();
    
    if(client)
      connected();
    else if(other.client)
      other.connected();
  }

  @Override
  public boolean isServer() {
    return server;
  }

  @Override
  public boolean isClient() {
    return client;
  }

  @Override
  public boolean isConnected() {
    return connection != null;
  }

  @Override
  protected void sendPacket(InetAddress dest, final Packet msg) {
    exe.execute(new Runnable() {

      @Override
      public void run() {
        if(connection != null)
          connection.packetRecieved(msg);
      }
    });
  }
  
  /**
   * Stops the {@link Executor} thread and disconnects this {@link LoopbackNetwork}.
   */
  public void stop() {
    if(exe == null) return;
    
    exe.shutdown();
    exe = null;
    
    if(connection != null)
      connection.stop();
    
    connection = null;
  }
  
  private boolean server;
  private boolean client;
  private LoopbackNetwork connection;
  private ExecutorService exe;
}
