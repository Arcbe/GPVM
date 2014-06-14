/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.schedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.networking.NetworkedObject;
import taiga.code.networking.Packet;
import taiga.code.registration.RegisteredSystem;
import taiga.code.util.Updateable;
import taiga.gpvm.util.geom.Coordinate;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.map.MutatorPresentException;
import taiga.gpvm.map.Universe;
import taiga.gpvm.map.UniverseListener;
import taiga.gpvm.map.World;
import taiga.gpvm.map.WorldMutator;

/**
 * A class that receives all update requests for the {@link Universe}
 * 
 * @author russell
 */
public class WorldUpdater extends RegisteredSystem implements UniverseListener {

  /**
   * Creates a new {@link WorldUpdater}
   */
  public WorldUpdater() {
    super(HardcodedValues.WORLD_UPDATER_NAME);
    
    addChild(new WorldComms());
    
    changes = new PriorityQueue<>();
    ups = new ArrayList<>();
    mutators = new HashMap<>();
    listeners = new ArrayList<>();
  }

  @Override
  protected void startSystem() {
    //make sure that there are no old junk in the way.
    if(updatetask != null) {
      updatetask.cancel();
    }
    
    if(timer == null) {
      timer = new Timer(getFullName());
    }
    
    updatetask = new TimerTask() {
      @Override
      public void run() {
        update();
      }
    };
    
    timer.scheduleAtFixedRate(updatetask, 0, HardcodedValues.UPDATE_DELAY);
    
    log.log(Level.INFO, THREAD_STARTED);
  }

  @Override
  protected void stopSystem() {
    if(updatetask == null) {
      return;
    }
    
    timer.cancel();
    
    updatetask = null;
    timer = null;
    log.log(Level.INFO, THREAD_STOPPED);
  }

  @Override
  protected void resetObject() {
    stopSystem();
  }

  @Override
  public void worldCreated(World world) {
    WorldMutator mutator = new WorldMutator();
    try {
      mutator.setWorld(world);
      mutators.put(world, mutator);
    } catch (MutatorPresentException ex) {
      throw new IllegalStateException(ex);
    }
  }
  
  /**
   * Submits a {@link WorldChange} to the {@link WorldUpdater}'s change queue. 
   * This {@link WorldChange} will be applied at the next update tick at earliest
   * regardless of the request time.
   * 
   * @param change The {@link WorldChange} to add to the queue.
   */
  public void submitTask(WorldChange change) {
    changes.add(change);
  }
  
  /**
   * Adds an {@link Updateable} to this {@link WorldUpdater}.  This {@link Updateable}
   * will be periodically updated after a given delay.  If the period is 0 or less
   * then only one update will happen, and if the delay is less than 0 then it
   * will be treated as being 0.  All units are in terms of update ticks.
   * 
   * @param up The {@link Updateable} to add.
   * @param delay The delay before the first update.
   * @param period The delay between subsequent updates.
   */
  public void addUpdateable(Updateable up, int delay, int period) {
    UpdateStruct struct = new UpdateStruct();
    
    struct.next = updatecount + delay;
    struct.period = period;
    struct.up = up;
    
    ups.add(struct);
  }
  
  public void addWorldChangeListener(WorldChangeListener list) {
    listeners.add(list);
  }
  
  public void removeWorldChangeListener(WorldChangeListener list) {
    listeners.remove(list);
  }
  
  private Timer timer;
  private TimerTask updatetask;
  private long updatecount;
  private final PriorityQueue<WorldChange> changes;
  private final Collection<UpdateStruct> ups;
  private final Map<World, WorldMutator> mutators;
  private final Collection<WorldChangeListener> listeners;
  
  private void update() {
    List<WorldChange> pending = getPendingChanges();
    List<WorldChange> ready = new ArrayList<>();
    
    for(List<WorldChange> cur = getConflictingChanges(pending) ;
      !cur.isEmpty();
      cur = getConflictingChanges(pending)) {
      for(int i = 0; i < cur.size(); i++) {
        if(cur.get(i) == null) continue;
        
        for(int j = 0; j < cur.size(); j++) {
          if(i == j || cur.get(j) == null) continue;
          
          if(cur.get(i).doesOverride(cur.get(j))) 
            cur.set(j, null);
        }
      }
      
      for(WorldChange change : cur)
        if(change != null)
          ready.add(change);
    }
    
    List<Object> old = applyChanges(ready);
    
    fireEvents(ready, old);
    processUpdateables();
    
    updatecount++;
  }
  
  private void fireEvents(List<WorldChange> chs, List<Object> prev) {
    for(int i = 0; i < chs.size(); i++) {
      for(WorldChangeListener list : listeners) {
        list.worldChanged(chs.get(i), prev.get(i));
      }
    }
  }
  
  private List<Object> applyChanges(List<WorldChange> chs) {
    List<Object> result = new ArrayList<>();
    
    for(WorldChange change : chs) {
      WorldMutator mutator = mutators.get(change.world);
      
      if(mutator == null) {
        log.log(Level.WARNING, NO_MUTATOR_FOUND, change.world.getFullName());
        continue;
      }
      
      result.add(change.applyChange(mutator));
    }
    
    return result;
  }
  
  private void processUpdateables() {
    for(UpdateStruct struct : ups) {
      if(struct.next <= updatecount) {
        struct.up.Update();
        struct.next += struct.period;
      }
    }
  }
  
  private List<WorldChange> getConflictingChanges(Collection<WorldChange> chs) {
    List<WorldChange> result = new ArrayList<>();
    
    Coordinate coor = null;
    World world = null;
    for(WorldChange change : chs) {
      //grab the first location.
      if(coor == null) {
        coor = change.location;
        world = change.world;
        result.add(change);
      //grab all changes in teh same location.
      } else if(world == change.world && coor.equals(change.location)) {
        result.add(change);
      }
    }
    
    chs.removeAll(result);
    
    return result;
  }
  
  private List<WorldChange> getPendingChanges() {
    List<WorldChange> result = new ArrayList<>();
    
    WorldChange change = changes.peek();
    while(change != null && change.update <= updatecount) {
      result.add(changes.poll());
      change = changes.peek();
    }
    
    return result;
  }
  
  private static final String locprefix = WorldUpdater.class.getName().toLowerCase();
  
  private static final String THREAD_STARTED = locprefix + ".thread_started";
  private static final String THREAD_STOPPED = locprefix + ".thread_stopped";
  private static final String NO_MUTATOR_FOUND = locprefix + ".no_mutator_found";
  private static final String NO_UNIVERSE_FOUND = locprefix + ".no_universe_found";
  
  private static final Logger log = Logger.getLogger(locprefix, 
    System.getProperty("taiga.code.logging.text"));
  
  private class UpdateStruct {
    public Updateable up;
    public long next;
    public int period;
  }
  
  private class WorldComms extends NetworkedObject {

    public WorldComms() {
      super(HardcodedValues.COMMS_NAME);
    }

    @Override
    protected void connected() {
    }

    @Override
    protected void messageRecieved(Packet pack) {
    }

    @Override
    protected void managerAttached() {}
    
  }
}
