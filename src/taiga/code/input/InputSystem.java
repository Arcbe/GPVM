/*
 * Copyright (C) 2014 Russell Smith.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package taiga.code.input;

import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import taiga.code.util.Updateable;

/**
 * Handles the generation of {@link KeyboardEvent}s and {@link MouseEvent}s for 
 * an application.  This uses the input library from LWJGL and will require a
 * LWJGL window to be created in order to work.
 * 
 * @author russell
 */
public class InputSystem extends HotkeyTable implements Updateable {
  private static final long serialVersionUID = 1L;

  /**
   * Creates a new {@link InputSystem} with the given name.
   * 
   * @param name The name for the new {@link InputSystem}.
   */
  public InputSystem(String name) {
    super(name);
    
    keylist = new HashSet<>(0);
    mouselist = new HashSet<>(0);
    mouseglist = new HashSet<>(0);
  }
  
  /**
   * Sets whether the mouse should grab the cursor.  If false then the cursor
   * will not be moved by the mouse, and events will send deltas instead of positions.
   * 
   * @param cap Whether to capture the mouse.
   */
  public void setGrabMouse(boolean cap) {
    Mouse.setGrabbed(cap);
    
    if(cap)
      log.log(Level.FINER, "Mouse grabbed.");
    else
      log.log(Level.FINER, "Mouse released.");
  }
  
  public void addMouseListener(MouseListener list) {
    mouselist.add(list);
    
    log.log(Level.FINER, "Mouse listener added: {0}", list);
  }
  
  public void removeMouseListener(MouseListener list) {
    mouselist.remove(list);
    
    log.log(Level.FINER, "Mouse listener removed: {0}", list);
  }
  
  public void addGrabbedMouseListener(MouseListener list) {
    mouseglist.add(list);
    
    log.log(Level.FINER, "Mouse grabbed listener added: {0}", list);
  }
  
  public void removeGrabbedMouseListener(MouseListener list) {
    mouseglist.remove(list);
    
    log.log(Level.FINER, "Mouse grabbed listener removed: {0}", list);
  }

  @Override
  public void update() {
    try {
      handleKeyboard();
      handleMouse();
    } catch (LWJGLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  private void handleMouse() throws LWJGLException {
    //same for the mouse
    if(!Mouse.isCreated()) try {
      Mouse.create();
    } catch(LWJGLException ex) {
      log.log(Level.SEVERE, "Exception while registering for mouse events.", ex);
      throw ex;
    }
    
    while(Mouse.next()) {
      MouseButtonEvent event = new MouseButtonEvent(
        Mouse.getEventDWheel(),
        Mouse.getEventButton(),
        Mouse.getEventButtonState(),
        Mouse.getEventX(),
        Mouse.getEventY(),
        Mouse.getEventDX(),
        Mouse.getEventDY(),
        Mouse.getEventNanoseconds());
      
      if(Mouse.isGrabbed()) {
        mouseglist.stream().forEach((list) -> {
          list.handleEvent(event);
        });
      } else {
        mouselist.stream().forEach((list) -> {
          list.handleEvent(event);
        });
      }
      
      log.log(Level.FINER, "Mouse event occurred: {0}", event);
      handleEvent(event);
    }
  }
  
  private void handleKeyboard() throws LWJGLException {
    //create the keyboard if needed.  This is done here to insure that the window
    //has been created already.
    if(!Keyboard.isCreated()) try {
      Keyboard.create();
    } catch (LWJGLException ex) {
      log.log(Level.SEVERE, "Exception while registering for keyboard events.", ex);
      throw ex;
    }
    
    while(Keyboard.next()) {
      KeyboardEvent event = new KeyboardEvent(
        Keyboard.getEventKey(), 
        Keyboard.getEventCharacter(), 
        Keyboard.getEventNanoseconds(), 
        Keyboard.getEventKeyState(), 
        Keyboard.isRepeatEvent());
      
      keylist.stream().forEach((list) -> {
        list.handleEvent(event);
      });
      
      //if the event is consumed then go on to the next one.
      if(event.consumed) continue;
      
      log.log(Level.FINER, "Keyboard event occurred: {0}");
      handleEvent(event);
    }
  }
  
  private final Collection<KeyboardListener> keylist;
  private final Collection<MouseListener> mouselist;
  private final Collection<MouseListener> mouseglist;
  
  private static final String locprefix = InputSystem.class.getName().toLowerCase();
  
  private static final Logger log = Logger.getLogger(locprefix, 
    System.getProperty("tagia.code.logging.text"));
}
