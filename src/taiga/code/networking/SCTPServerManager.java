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

package taiga.code.networking;

import com.sun.nio.sctp.SctpServerChannel;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;
import java.util.logging.Logger;

public class SCTPServerManager extends NetworkManager{

  public SCTPServerManager(String name) throws IOException {
    super(name);
    
    channel = SctpServerChannel.open();
    channel.configureBlocking(false);
    
    selector = channel.provider().openSelector();
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
    return selector.isOpen();
  }

  @Override
  protected void sendPacket(InetAddress dest, int sysid, DatagramPacket msg) {
    
  }
  
  private SctpServerChannel channel;
  private Selector selector;

  private static final Logger log = Logger.getLogger(SCTPServerManager.class.getName().toLowerCase());
}
