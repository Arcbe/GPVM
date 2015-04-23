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

package taiga.code.networking;

import java.io.IOException;
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
   * @throws java.io.IOException
   */
  public void connect(LoopbackNetwork other) throws TimeoutException, IOException {
    connection = other;
    other.connection = this;
    
    exe = Executors.newSingleThreadExecutor();
    other.exe = Executors.newSingleThreadExecutor();
    
    if(client)
      connect();
    else if(other.client)
      other.connect();
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
  protected void sendPacket(Object dest, short sysid, final byte[] msg) {
    exe.execute(() -> {
      if(connection != null)
        connection.packetRecieved(dest, msg, sysid);
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
