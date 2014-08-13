/*
 * Copyright (C) 2014 Russell Smith
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package taiga.code.registration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A system within a program that can be started and stopped.  This may use a 
 * separate {@link Thread} to run the {@link NamedSystem}.  The {@link NamedSystem}
 * may be started and stopped an arbitrary number of times and should be ready to
 * start as soon as it is constructed.
 * 
 * @author russell
 */
public abstract class NamedSystem extends ReusableObject {

  /**
   * Creates a new {@link RegisteredSystem} with the given name.
   * 
   * @param name The name for the new system.
   */
  public NamedSystem(String name) {
    super(name);
    
    running = false;
    listeners = new HashSet<>();
  }
  
  /**
   * Causes the {@link NamedSystem} to start operating.  If this is called
   * while the {@link NamedSystem} is already running it has no effect.
   * This method will also start any subsystems that have been added as children
   * and will recursively start all system in the tree that it can reach.  At the
   * end of this method this system must be started and in a running state.
   */
  public final void start() {
    //start any subsystems
    for(NamedObject obj : this) {
      if(obj != null && obj instanceof NamedSystem) {
        ((NamedSystem)obj).start();
      }
    }
    
    //then start this system
    startSystem();
    
    running = true;
    
    fireSystemStarted();
  }
  
  /**
   * Causes the {@link NamedSystem} to stop operating, the exact effect depends
   * on the implementing class.  If the {@link NamedSystem} is already stopped
   * or has never started then this method has no effect.  This method will also
   * stop all subsystems that have been registered as children and subsequently
   * stop all {@link NamedSystem}s in the tree that it can reach.  At the
   * end of this method the system must be stopped.
   */
  public final void stop() {
    //stop any subsystems
    for(NamedObject obj : this) {
      if(obj != null && obj instanceof NamedSystem) {
        ((NamedSystem)obj).stop();
      }
    }
    
    //then stop this system
    stopSystem();
    
    running = false;
    
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
   * Adds the given {@link SystemListener} to this {@link NamedSystem}.
   * 
   * @param list The {@link SystemListener} to add.
   */
  public void addSystemListener(SystemListener list) {
    listeners.add(list);
  }
  
  /**
   * Removes the given {@link SystemListener} to this {@link NamedSystem}.
   * 
   * @param list  The {@link SystemListener} to remove.
   */
  public void removeSystemListener(SystemListener list) {
    listeners.remove(list);
  }
  
  /**
   * Called when this {@link NamedSystem} is supposed to start.
   */
  protected abstract void startSystem();
  /**
   * Called when this {@link NamedSystem} is supposed to stop.
   */
  protected abstract void stopSystem();
  
  private transient boolean running;
  private transient final Collection<SystemListener> listeners;
  
  private void fireSystemStarted() {
    log.log(Level.INFO, STARTED, getFullName());
    
    for(SystemListener list : listeners)
      list.systemStarted(this);
  }
  
  private void fireSystemStopped() {
    log.log(Level.INFO, STOPPED, getFullName());
    
    for(SystemListener list : listeners)
      list.systemStopped(this);
  }
  
  private void readObject(ObjectInputStream in) throws
    ClassNotFoundException,
    IOException {
    in.defaultReadObject();
    
    listeners.addAll((Collection<SystemListener>) in.readObject());
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
    
    Collection<SystemListener> seriallist = new ArrayList<>();
    for(SystemListener list : listeners)
      if(list instanceof Serializable)
        seriallist.add(list);
    out.writeObject(seriallist);
  }
  
  private static final long serialVersionUID = 100L;
  
  private static final String STARTED = NamedSystem.class.getName().toLowerCase() + ".started";
  private static final String STOPPED = NamedSystem.class.getName().toLowerCase() + ".stopped";
  
  private static final Logger log = Logger.getLogger(NamedSystem.class.getName(),
    System.getProperty("taiga.code.logging.text"));
}
