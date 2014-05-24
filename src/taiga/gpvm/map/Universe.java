/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm.map;

import taiga.gpvm.HardcodedValues;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import taiga.code.networking.NetworkedObject;
import taiga.code.networking.Packet;

/**
 *
 * @author russell
 */
public class Universe extends NetworkedObject {
  
  public void addWorld(String name, MapGenerator gen) {
    World nworld = new World(name);
    nworld.addChild(gen);
    
    addChild(nworld);
  }

  @Override
  protected void connected() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected void messageRecieved(Packet pack) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected void managerAttached() {
  }
  
  public Universe() {
    super(HardcodedValues.UNIVERSE_NAME);
  }
  
  private static final Logger log = Logger.getLogger(Universe.class.getName());
}
