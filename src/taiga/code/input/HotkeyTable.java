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

import java.awt.event.KeyListener;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import taiga.code.registration.ReusableObject;

/**
 * Manages a list of {@link KeyListener}s and {@link MouseListener}s that can be
 * bound to specific events.  Listeners can be set to listen to only a select few
 * keys and such bindings are configurable by external {@link File}.
 * 
 * @author russell
 */
public class HotkeyTable extends ReusableObject implements KeyboardListener, MouseListener {
  private static final long serialVersionUID = 1L;

  /**
   * Creates a new {@link HotkeyTable} without any key bindings and with the
   * given name.
   * 
   * @param name The name for this {@link HotkeyTable}.
   */
  public HotkeyTable(String name) {
    super(name);
    
    keyactions = new HashMap<>(0);
    keybindings = new HashMap<>(0);
  }
  
  /**
   * Adds {@link KeyListener} to the key with the given name.
   * 
   * @param key The name of the key to add {@link KeyListener}s to.
   * @param actions The name for the {@link KeyListener}s to add.
   */
  public void addKeyBinding(String key, String ... actions) {
    log.log(Level.FINEST, () -> MessageFormat.format("addKeyBinding({0}, {1})", key, Arrays.toString(actions)));
    
    Collection<ButtonAction> bindings = getBindings(key);
    if(bindings == null) return;
    
    for(String act : actions) {
      ButtonAction action = keyactions.get(act);
      
      //make sure that it is a valid action
      if(action == null) {
        log.log(Level.WARNING, "'{0}' action not found, unable to bind key.", act);
        continue;
      }
      
      //Make sure the action is not already bound to this key
      if(action.keysbound.contains(key)) {
        log.log(Level.WARNING, "Action '{0}' is already bound to key '{1}'.", new Object[] {act, key});
        continue;
      }
      
      //finally bind the action.
      action.keysbound.add(key);
      bindings.add(action);
      
      log.log(Level.CONFIG, "Action '{0}' bound to key '{1}'.", new Object[] {act, key});
    }
  }
  
  /**
   * Removes {@link KeyListener}s with the given names from the key with the given
   * name.  If the key does not exist or there is no {@link KeyListener} for one
   * of the names this method will skip that name.
   * 
   * @param key The name of the key to remove bindings from.
   * @param actions The name given to the {@link KeyListener}s to remove.
   */
  public void removeKeyBinding(String key, String ... actions) {
    log.log(Level.FINEST, () -> MessageFormat.format("removeKeyBinding({0}, {1})", key, Arrays.toString(actions)));
    
    Collection<ButtonAction> bindings = getBindings(key);
    if(bindings == null) return;
    
    bindings.stream().forEach((act) -> {
      if(act.keysbound.contains(key)) {
        act.keysbound.remove(key);
        bindings.remove(act);
        
        log.log(Level.CONFIG, "Action '{0}' removed from key '{1}'.", new Object[]{act, key});
      } else {
        log.log(Level.WARNING, "Action '{0}' could not be removed from key '{1}', action not bound.");
      }
    });
  }
  
  /**
   * Removes all {@link ButtonListener}s bound to the key with the given name.  If
   * the key does not exist or there are no {@link ButtonListener}s bound then this
   * method will do nothing.
   * 
   * @param key The name of the key to clear of bound {@link ButtonListener}s.
   */
  public void removeAllBindings(String key) {
    log.log(Level.FINEST, "removeAllBindings({0})", key);
    
    Collection<ButtonAction> bindings = getBindings(key);
    if(bindings == null) return;
    
    bindings.stream().forEach((act) -> {
      act.keysbound.remove(key);
    });
    
    bindings.clear();
    
    log.log(Level.CONFIG, "All key bindings removed from key '{0}'.", key);
  }
  
  /**
   * Adds a {@link KeyListener} to the list of available {@link KeyListener}s with
   * the given name.  The {@link ButtonListener} can then be bound to a key using the
   * {@link HotkeyTable#addKeyBinding(java.lang.String, java.lang.String...) }
   * method.
   * 
   * @param name The name that can be used to reference the new {@link ButtonListener}.
   * @param list The {@link KeyListener} to add to this {@link HotkeyTable}.
   */
  public void addAction(String name, ButtonListener list) {
    log.log(Level.FINEST, "addAction({0}, {1})", new Object[]{name, list});
    
    ButtonAction action = new ButtonAction(list, name);
    keyactions.put(name, action);
    
    log.log(Level.CONFIG, "Action '{0}' added.", name);
  }
  
  /**
   * Removes the {@link KeyListener} with the given name after un-binding it from
   * any keys it is bound to. If there is no {@link ButtonListener} with the given
   * name then this method will not do anything.
   * 
   * @param name The name of the {@link ButtonListener} to remove.
   */
  public void removeAction(String name) {
    log.log(Level.FINEST, "removeAction({0})", name);
    
    ButtonAction action = keyactions.remove(name);
    if(action == null) return;
    
    action.keysbound.stream()
      .map((key) -> {
        Collection<ButtonAction> bindings = getBindings(key);
        assert bindings != null;
        return bindings;
      })
      .forEach((bindings) -> {
        bindings.remove(action);
      });
    
    log.log(Level.CONFIG, "Action '{0}' removed.", name);
  }
  
  public void addAction(String name, MouseListener list) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void handleEvent(KeyboardEvent event) {
    //ignore repeat events for bindings
    if(event.repeat) return;
    
    String key = Keyboard.getKeyName(event.key);
    Collection<ButtonAction> bindings = getBindings(key);
    if(bindings.isEmpty()) return;
    
    if(event.state)
      bindings.stream().forEach((act) -> {
        act.list.buttonPressed(key);
      });
    else
      bindings.stream().forEach((act) -> {
        act.list.buttonReleased(key);
      });
  }

  @Override
  public void handleEvent(MouseButtonEvent event) {
    String key = Mouse.getButtonName(event.button);
    Collection<ButtonAction> bindings = getBindings(key);
    if(bindings.isEmpty()) return;
    
    if(event.state)
      bindings.stream().forEach((act) -> {
        act.list.buttonPressed(key);
      });
    else
      bindings.stream().forEach((act) -> {
        act.list.buttonReleased(key);
      });
  }

  @Override
  protected void resetObject() {
    keyactions.clear();
    keybindings.clear();
  }
  
  private Collection<ButtonAction> getBindings(String key) {
    log.log(Level.FINEST, "getBindings({0})", key);
    
    //get or create a collection of actions for binding.
    Collection<ButtonAction> bindings = keybindings.get(key);
    if(bindings == null) {
      bindings = new HashSet<>(0);
      keybindings.put(key, bindings);
    }
    
    return bindings;
  }
  
  private final Map<String, ButtonAction> keyactions;
  private final Map<String, Collection<ButtonAction>> keybindings;
  
  private static class ButtonAction {
    public final ButtonListener list;
    public final Collection<String> keysbound;
    public final String name;

    public ButtonAction(ButtonListener list, String name) {
      this.list = list;
      this.keysbound = new ArrayList<>(0);
      this.name = name;
    }
  }

  private static final String locprefix = HotkeyTable.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
      System.getProperty("taiga.code.logging.text"));
}
