/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.map;

import taiga.code.networking.NetworkedObject;
import taiga.code.networking.Packet;

/**
 *
 * @author russell
 */
public class World extends NetworkedObject {
  
  public World(String name) {
    super(name);
  }

  @Override
  protected void connected() {
  }

  @Override
  protected void messageRecieved(Packet pack) {
  }

  @Override
  protected void managerAttached() {
  }
}
