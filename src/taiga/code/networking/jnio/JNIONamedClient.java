/*
 * Copyright (C) 2015 Russell Smith.
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

package taiga.code.networking.jnio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.networking.NetworkManager;

public class JNIONamedClient extends NetworkManager {
  
  public static final int DEFAULT_BUFFER_SIZE = 4096;
  private static final long serialVersionUID = 1L;

  public JNIONamedClient(String name) throws IOException {
    super(name);
  }
  
  public void connect(InetSocketAddress addr) throws IOException {
    log.log(Level.FINEST, "connect({0})", addr);
    
    channel = DatagramChannel.open();
    channel.connect(addr);
    
    listener = new Thread(() -> {
      ByteBuffer buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
      
      while(channel.isConnected()) {
        buffer.clear();
        
        try {
          channel.receive(buffer);
          receiveMessage(buffer);
        } catch(IOException ex) {
          log.log(Level.SEVERE, "Exception occur while waiting for network data.", ex);
        } finally {
          //notify the manager that the connection has ended.
          fireClientDisconnect(this);
        }
      }
    }, "UDP listener");
    
    log.log(Level.INFO, "Opened connection to {0}.", addr);
  }

  @Override
  public boolean isServer() {
    return false;
  }

  @Override
  public boolean isClient() {
    return true;
  }

  @Override
  public boolean isConnected() {
    return channel != null && channel.isConnected();
  }

  @Override
  protected void sendPacket(Object dest, int sysid, DatagramPacket msg) throws IOException {
    if(channel == null || !channel.isConnected()) return;
    
    
  }
  
  private void receiveMessage(ByteBuffer buffer) {
    
  }
  
  private DatagramChannel channel;
  private Thread listener;

  private static final Logger log = Logger.getLogger(JNIONamedClient.class.getName().toLowerCase());
}
