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
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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
    JNioBindingParameters params = new JNioBindingParameters(this);
    
    server.bindSCTPListeningAddress(addr, params);
  }
  
  private JNioServer server;
  private ThreadLocal<ByteBuffer> buffers = new ThreadLocal<ByteBuffer>() {

    @Override
    protected ByteBuffer initialValue() {
      return ByteBuffer.allocateDirect(JNioBindingParameters.DEFAULT_BUFFER_SIZE);
    }
  };
  
  private static final String locprefix = JNIONamedServer.class.getName().toLowerCase();
  
  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));

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
  protected void sendPacket(InetAddress dest, int sysid, DatagramPacket msg) {
    
  }

  @Override
  public void onDataRecieved(JNioStreamChannel jnsc) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void onMessageRecieved(JNioDatagramChannel jndc) {
  }

  /**
   *
   * @param arg0
   */
  @Override
  public void onClientConnected(JNioChannel arg0) {
  }

  /**
   *
   * @param arg0
   * @param arg1
   */
  @Override
  public void onClientDisconnected(JNioChannel arg0, boolean arg1) {
  }
}
