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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.networking.NetworkManager;

public class JNIONamedClient extends NetworkManager {
  
  public static final int DEFAULT_BUFFER_SIZE = 4096;
  private static final long serialVersionUID = 1L;

  public JNIONamedClient(String name) throws IOException {
    super(name);
  }
  
  public void connect(SocketAddress addr) throws IOException, TimeoutException {
    log.log(Level.FINEST, "connect({0})", addr);
    
    //Open a channel to the remote address
    channel = SocketChannel.open(addr);
    
    //create a thread to listen for inbound datagrams
    listener = new Thread(() -> {
      log.log(Level.FINEST, "Starting datagram listener thread.");
      ByteBuffer buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
      
      while(isConnected()) {
        buffer.clear();
        
        try {
          //get the message from the channel.
          buffer.limit(
            channel.read(buffer));
          buffer.rewind();
          receiveMessage(buffer);
        } catch(IOException ex) {
          log.log(Level.SEVERE, "Exception occur while waiting for network data.", ex);
        } finally {
          //notify the manager that the connection has ended.
          fireClientDisconnect(this);
        }
      }
      
      log.log(Level.FINEST, "Datagram listener thread finished.");
    }, "UDP listener");
    listener.start();
    
    //and finally notify the super class that we are connected.
    log.log(Level.INFO, "Opened connection to {0}.", addr);
    connect();
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
  protected void sendPacket(Object dest, short sysid, byte[] msg) throws IOException {
    if(!isConnected()) return;
    
    ByteBuffer buffer = buffers.get();
    buffer.clear();
    
    buffer.putShort((short) msg.length);
    buffer.putShort(sysid);
    buffer.put(msg);
    
    buffer.flip();
    channel.write(buffer);
  }
  
  private void receiveMessage(ByteBuffer buffer) {
    short sysid = buffer.getShort();
    byte[] data = new byte[buffer.remaining()];
    buffer.get(data);
    
    packetRecieved(null, data, sysid);
  }
  
  private SocketChannel channel;
  private Thread listener;
  private static final ThreadLocal<ByteBuffer> buffers = new ThreadLocal<ByteBuffer>() {
    @Override
    protected ByteBuffer initialValue() {
      return ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
    }
  };

  private static final Logger log = Logger.getLogger(JNIONamedClient.class.getName().toLowerCase());
}
