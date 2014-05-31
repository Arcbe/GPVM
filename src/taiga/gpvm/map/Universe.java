package taiga.gpvm.map;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import taiga.gpvm.HardcodedValues;
import java.util.List;
import java.util.logging.Logger;
import taiga.code.networking.NetworkedObject;
import taiga.code.networking.Packet;

/**
 * This class manages a collection of {@link World}s and provides events for
 * the creation and destruction of contained {@link World}s.
 * @author russell
 */
public class Universe extends NetworkedObject {
  
  /**
   * Creates a new empty {@link Universe}.
   */
  public Universe() {
    super(HardcodedValues.UNIVERSE_NAME);
    
    listeners = new ArrayList<>();
  }
  
  /**
   * Adds a new {@link World} to this {@link Universe} with the given {@link MapGenerator}.
   * 
   * @param name The name for the new {@link World}.
   * @param gen The {@link MapGenerator} for the {@link World}
   */
  public void addWorld(String name, MapGenerator gen) {
    //TODO: add default map generator.
    
    World nworld = new World(name);
    nworld.addChild(gen);
    
    addChild(nworld);
    
    fireWorldCreated(nworld);
  }
  
  /**
   * Adds a {@link UniverseListener} to this {@link Universe}.
   * 
   * @param list The listener to add.
   */
  public void addListener(UniverseListener list) {
    listeners.add(new WeakReference<>(list));
  }
  
  /**
   * Removes a {@link UniverseListener} from this {@link Universe} that was previously
   * added.
   * 
   * @param list The {@link UniverseListener} to remove.
   */
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
    boolean cull = false;
    
    for(WeakReference<UniverseListener> ref : listeners) {
      UniverseListener list = ref.get();
      if(list == null) {
        cull = true;
      } else {
        list.worldCreated(world);
      }
    }
    
    if(cull) cullListeners();
  }
  
  private void cullListeners() {
    Iterator<WeakReference<UniverseListener>> it = listeners.iterator();
    while(it.hasNext())
      if(it.next().get() == null)
        it.remove();
  }
  
  private List<WeakReference<UniverseListener>> listeners;
  
  private static final Logger log = Logger.getLogger(Universe.class.getName());
}
