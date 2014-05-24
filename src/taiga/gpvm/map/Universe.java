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
  
  public static class World {
    public String name;
    public Class<? extends MapGenerator> generator;
  }
  
  public static Universe getInstance() {
    return instance;
  }
  
  public void addWorld(String name, MapGenerator gen) {
    World nworld = new World();
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
  
  private Universe() {
    super(HardcodedValues.UNIVERSE_NAME);
  }
  
  private static final Universe instance = new Universe();
  private static final Logger log = Logger.getLogger(Universe.class.getName());
}
