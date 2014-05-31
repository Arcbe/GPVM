/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.networking.jnio;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
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
