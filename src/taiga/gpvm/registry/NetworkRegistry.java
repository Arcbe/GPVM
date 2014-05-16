/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

import taiga.code.networking.NetworkedObject;
import taiga.code.networking.Packet;

/**
 *
 * @author russell
 */
public class NetworkRegistry<T extends RegistryEntry> extends Registry<T> {

  public NetworkRegistry(String name) {
    super(name);
    
    addChild(new Synchronizer());
  }
  
  private class Synchronizer extends NetworkedObject {
    
    public static final String NAME = "sync";

    public Synchronizer() {
      super(NAME);
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
}
