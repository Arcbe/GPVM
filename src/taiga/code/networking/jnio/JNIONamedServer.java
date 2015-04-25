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

package taiga.code.networking.jnio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.frozencode.jniolib.server.JNioBindingParameters;
import net.frozencode.jniolib.server.JNioChannel;
import net.frozencode.jniolib.server.JNioDatagramChannel;
import net.frozencode.jniolib.server.JNioEventListener;
import net.frozencode.jniolib.server.JNioServer;
import net.frozencode.jniolib.server.JNioStreamChannel;
import taiga.code.networking.NetworkManager;

/**
 * An implementation of a {@link NetworkManager} that uses the {@link net.frozencode.jniolib.server}
 * package as a network backend.
 * 
 * @author russell
 */
public class JNIONamedServer extends NetworkManager implements JNioEventListener {
  private static final long serialVersionUID = 1L;

  /**
   * Creates a new {@link JNIORegisteredServer}  with the given name.
   * 
   * @param name The name for the server.
   * @throws java.io.IOException
   */
  public JNIONamedServer(String name) throws IOException {
    super(name);
    
    server = new JNioServer(); 
  }
  
  public void openConnection(int port) throws IOException {
    InetSocketAddress addr = new InetSocketAddress(InetAddress.getLocalHost(), port);
    
    openConnection(addr);
  }
  
  public void openConnection(InetSocketAddress addr) throws IOException {
    log.log(Level.FINEST, "openConnection({0})", addr);
    
    server.bindTCPListeningAddress(addr, this);
    
    log.log(Level.INFO, "Connection on address {0} opened.", addr);
  }

  @Override
  public boolean isServer() {
    return true;
  }

  @Override
  public boolean isClient() {
    return false;
  }

  @Override
  public boolean isConnected() {
    return server.isAlive();
  }

  @Override
  protected void sendPacket(Object dest, short sysid, byte[] msg) throws IOException {
    if(!(dest instanceof JNioChannel) || !isConnected())
      throw new IOException("No valid connection for client " + dest);
    
    ByteBuffer buf = buffers.get();
    buf.clear();
    
    buf.putShort((short) msg.length);
    buf.putShort(sysid);
    buf.put(msg);
    
    buf.flip();
    if(dest instanceof WritableByteChannel) {
      
      ((WritableByteChannel) dest).write(buf);
    }
  }

  @Override
  public void onDataRecieved(JNioStreamChannel jnsc) {
    try {
      Message msg = readMessage(jnsc);
      while(msg != null) {
        packetRecieved(jnsc, msg.msg, msg.sysid);
        
        msg = readMessage(jnsc);
      }
    } catch (IOException ex) {
      log.log(Level.WARNING, "Exception while receiving network message.", ex);
    }
  }

  @Override
  public void onMessageRecieved(JNioDatagramChannel jndc) {
    ByteBuffer buf = buffers.get();
    
    try {
      buf.clear();
      jndc.receive(buf);
    } catch (IOException ex) {
      log.log(Level.WARNING, "Exception while receiving network message.", ex);
    }
    
    buf.rewind();
    short sysid = buf.getShort();
    byte[] bytes = new byte[buf.remaining()];
    
    buf.get(bytes);
    packetRecieved(jndc, bytes, sysid);
  }

  @Override
  public void onClientConnected(JNioChannel arg0) {
    fireClientConnected(arg0);
  }
  
  @Override
  public void onClientDisconnected(JNioChannel arg0, boolean arg1) {
    fireClientDisconnect(arg0);
  }
  
  private Message readMessage(ReadableByteChannel chan) throws IOException {
    ByteBuffer buf = buffers.get();
    Message msg = new Message();
    int read = 0;
    
    //get the header for the message.
    buf.clear();
    buf.limit(4);
    read = chan.read(buf);
    buf.rewind();
    
    //if nothing was read then we are at the end of the channel for now.
    if(read == 0) return null;
    if(read != 4) throw new IOException("Could not read header of network packet.");
    
    short size = buf.getShort();
    msg.sysid = buf.getShort();
    msg.msg = new byte[size];
    
    //read in the message.
    buf.rewind();
    buf.limit(size);
    read = chan.read(buf);
    buf.rewind();
    
    if(read != size) throw new IOException("Could not read entire network packet.");
    buf.get(msg.msg);
    
    return msg;
  }
  
  private final JNioServer server;
  private static final ThreadLocal<ByteBuffer> buffers = new ThreadLocal<ByteBuffer>() {

    @Override
    protected ByteBuffer initialValue() {
      return ByteBuffer.allocateDirect(JNioBindingParameters.DEFAULT_BUFFER_SIZE);
    }
  };
  
  private static class Message {
    short sysid;
    byte[] msg;
  }
  
  private static final String locprefix = JNIONamedServer.class.getName().toLowerCase();
  
  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
