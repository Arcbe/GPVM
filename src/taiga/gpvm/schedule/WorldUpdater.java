/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.schedule;

import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.networking.NetworkedObject;
import taiga.code.networking.Packet;
import taiga.code.registration.RegisteredSystem;
import taiga.gpvm.HardcodedValues;

/**
 * A class that receives all update requests for the {@link Universe}
 * 
 * @author russell
 */
public class WorldUpdater extends RegisteredSystem {

  /**
   * Creates a new {@link WorldUpdater}
   */
  public WorldUpdater() {
    super(HardcodedValues.WORLD_UPDATER_NAME);
    
    addChild(new WorldComms());
    
    changes = new PriorityQueue<>();
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
    
    createTaskMaster();
    
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
  
  public void submitTask(WorldChange change) {
    changes.add(change);
  }
  
  private Timer timer;
  private TimerTask updatetask;
  private long updatecount;
  private ExecutorService taskmaster;
  private PriorityQueue<WorldChange> changes;
  
  private void update() {
    
    
    updatecount++;
  }
  
  private void createTaskMaster() {
    if(taskmaster != null) return;
    
    taskmaster = Executors.newCachedThreadPool();
  }
  
  private static final String locprefix = WorldUpdater.class.getName().toLowerCase();
  
  private static final String THREAD_STARTED = locprefix + ".thread_started";
  private static final String THREAD_STOPPED = locprefix + ".thread_stopped";
  
  private static final Logger log = Logger.getLogger(locprefix, 
    System.getProperty("taiga.code.logging.text"));
  
  private class WorldComms extends NetworkedObject {

    public WorldComms() {
      super(HardcodedValues.WORLD_COMMS_NAME);
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
    protected void managerAttached() {}
    
  }
}
