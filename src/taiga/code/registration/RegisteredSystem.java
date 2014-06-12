/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.registration;

import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A system within a program that can be started and stopped.  This may use a 
 * separate {@link Thread} to run the {@link RegisteredSystem}.  The {@link RegisteredSystem}
 * may be started and stopped an arbitrary number of times and should be ready to
 * start as soon as it is constructed.
 * 
 * @author russell
 */
public abstract class RegisteredSystem extends ReusableObject {

  /**
   * Creates a new {@link RegisteredSystem} with the given name.
   * 
   * @param name The name for the new system.
   */
  public RegisteredSystem(String name) {
    super(name);
    
    running = false;
    listeners = new HashSet<>();
  }
  
  /**
   * Causes the {@link RegisteredSystem} to start operating.  If this is called
   * while the {@link RegisteredSystem} is already running it has no effect.
   * This method will also start any subsystems that have been added as children
   * and will recursively start all system in the tree that it can reach.  At the
   * end of this method this system must be started and in a running state.
   */
  public final void start() {
    //start any subsystems
    for(RegisteredObject obj : this) {
      if(obj != null && obj instanceof RegisteredSystem) {
        ((RegisteredSystem)obj).start();
      }
    }
    
    //then start this system
    startSystem();
    
    running = true;
    log.log(Level.INFO, STARTED, getFullName());
    
    fireSystemStarted();
  }
  
  /**
   * Causes the {@link RegisteredSystem} to stop operating, the exact effect depends
   * on the implementing class.  If the {@link RegisteredSystem} is already stopped
   * or has never started then this method has no effect.  This method will also
   * stop all subsystems that have been registered as children and subsequently
   * stop all {@link RegisteredSystem}s in the tree that it can reach.  At the
   * end of this method the system must be stopped.
   */
  public final void stop() {
    //stop any subsystems
    for(RegisteredObject obj : this) {
      if(obj != null && obj instanceof RegisteredSystem) {
        ((RegisteredSystem)obj).stop();
      }
    }
    
    //then stop this system
    stopSystem();
    
    running = false;
    log.log(Level.INFO, STOPPED, getFullName());
    
    fireSystemStopped();
  }
  
  /**
   * Returns whether the system is currently has been started and is currently running.
   * 
   * @return Whether the system is running.
   */
  public final boolean isRunning() {
    return running;
  }
  
  /**
   * Adds the given {@link SystemListener} to this {@link RegisteredSystem}.
   * 
   * @param list The {@link SystemListener} to add.
   */
  public void addSystemListener(SystemListener list) {
    listeners.add(list);
  }
  
  /**
   * Removes the given {@link SystemListener} to this {@link RegisteredSystem}.
   * 
   * @param list  The {@link SystemListener} to remove.
   */
  public void removeSystemListener(SystemListener list) {
    listeners.remove(list);
  }
  
  /**
   * Called when this {@link RegisteredSystem} is supposed to start.
   */
  protected abstract void startSystem();
  /**
   * Called when this {@link RegisteredSystem} is supposed to stop.
   */
  protected abstract void stopSystem();
  
  private boolean running;
  private Collection<SystemListener> listeners;
  
  private void fireSystemStarted() {
    for(SystemListener list : listeners)
      list.systemStarted(this);
  }
  
  private void fireSystemStopped() {
    for(SystemListener list : listeners)
      list.systemStopped(this);
  }
  
  private static final String STARTED = RegisteredSystem.class.getName().toLowerCase() + ".started";
  private static final String STOPPED = RegisteredSystem.class.getName().toLowerCase() + ".stopped";
  
  private static final Logger log = Logger.getLogger(RegisteredSystem.class.getName(),
    System.getProperty("taiga.code.logging.text"));
}
