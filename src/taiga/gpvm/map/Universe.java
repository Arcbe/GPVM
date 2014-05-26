/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm.map;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import taiga.gpvm.HardcodedValues;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import taiga.code.networking.NetworkedObject;
import taiga.code.networking.Packet;

/**
 *
 * @author russell
 */
public class Universe extends NetworkedObject {
  
  public Universe() {
    super(HardcodedValues.UNIVERSE_NAME);
    
    listeners = new ArrayList<>();
  }
  
  public void addWorld(String name, MapGenerator gen) {
    
    World nworld = new World(name);
    nworld.addChild(gen);
    
    addChild(nworld);
    
    fireWorldCreated(nworld);
  }
  
  public void addListener(UniverseListener list) {
    listeners.add(new WeakReference<>(list));
  }
  
  public void removeListener(UniverseListener list) {
    for(WeakReference<UniverseListener> ref : listeners) {
      if(ref.get() == list) {
        listeners.remove(ref);
        return;
      }
    }
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
  
  private void fireWorldCreated(World world) {
    for(WeakReference<UniverseListener> ref : listeners) {
      UniverseListener list = ref.get();
      if(list == null) {
        listeners.remove(ref);
      } else {
        list.worldCreated(world);
      }
    }
  }
  
  private List<WeakReference<UniverseListener>> listeners;
  
  private static final Logger log = Logger.getLogger(Universe.class.getName());
}
