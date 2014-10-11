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

import java.net.InetAddress;
import java.util.logging.Logger;
import net.frozencode.jniolib.server.JNioChannel;
import net.frozencode.jniolib.server.JNioEventListener;
import net.frozencode.jniolib.server.JNioServer;
import taiga.code.networking.NetworkManager;
import taiga.code.networking.Packet;

/**
 * An implementation of a {@link NetworkManager} that uses the {@link net.frozencode.jniolib.server}
 * package as a network backend.
 * 
 * @author russell
 */
public class JNIORegisteredServer extends NetworkManager implements JNioEventListener {

  /**
   * Creates a new {@link JNIORegisteredServer}  with the given name.
   * 
   * @param name The name for the server.
   */
  public JNIORegisteredServer(String name) {
    super(name);
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
   */
  @Override
  public void onDataRecieved(JNioChannel arg0) {
  }

  /**
   *
   * @param arg0
   * @param arg1
   */
  @Override
  public void onClientDisconnected(JNioChannel arg0, boolean arg1) {
  }
  
  private JNioServer server;
  
  private static final String locprefix = JNIORegisteredServer.class.getName().toLowerCase();
  
  private static final String DISCONNECT_INTTERRUPTED = locprefix + ".disconnect_interrupted";
  private static final String DISCONNECTED = locprefix + ".disconnected";
  
  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));

  @Override
  public boolean isServer() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean isClient() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean isConnected() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected void sendPacket(InetAddress dest, Packet msg) {
    
  }
}
