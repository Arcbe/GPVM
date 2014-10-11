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
import java.util.ArrayList;
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

  /**
   * Creates a new {@link HotkeyTable} without any key bindings and with the
   * given name.
   * 
   * @param name The name for this {@link HotkeyTable}.
   */
  public HotkeyTable(String name) {
    super(name);
    
    keyactions = new HashMap<>();
    keybindings = new HashMap<>();
  }
  
  /**
   * Adds {@link KeyListener} to the key with the given name.
   * 
   * @param key The name of the key to add {@link KeyListener}s to.
   * @param actions The name for the {@link KeyListener}s to add.
   */
  public void addKeyBinding(String key, String ... actions) {
    Collection<ButtonAction> bindings = getBindings(key);
    if(bindings == null) return;
    
    for(String act : actions) {
      ButtonAction action = keyactions.get(act);
      
      //make sure that it is a valid action
      if(action == null) {
        log.log(Level.WARNING, INVALID_KEY_ACTION_NAME, act);
        continue;
      }
      
      //Make sure the action is not already bound to this key
      if(action.keysbound.contains(key)) {
        log.log(Level.WARNING, KEY_ACTION_ALREADY_BOUND, new Object[] {act, key});
        continue;
      }
      
      //finally bind the action.
      action.keysbound.add(key);
      bindings.add(action);
      
      log.log(Level.CONFIG, KEY_BINDING_ADDED, new Object[] {act, key});
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
    Collection<ButtonAction> bindings = getBindings(key);
    if(bindings == null) return;
    
    for(ButtonAction act : bindings) {
      if(act.keysbound.contains(key)) {
        act.keysbound.remove(key);
        bindings.remove(act);
        
        log.log(Level.CONFIG, REMOVED_KEY_BINDING, new Object[]{key, act});
      }
    }
  }
  
  /**
   * Removes all {@link ButtonListener}s bound to the key with the given name.  If
   * the key does not exist or there are no {@link ButtonListener}s bound then this
   * method will do nothing.
   * 
   * @param key The name of the key to clear of bound {@link ButtonListener}s.
   */
  public void removeAllBindings(String key) {
    
    Collection<ButtonAction> bindings = getBindings(key);
    if(bindings == null) return;
    
    for(ButtonAction act : bindings) {
      act.keysbound.remove(key);
    }
    
    bindings.clear();
    
    log.log(Level.CONFIG, CLEARED_KEY_BINDINGS, key);
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
    ButtonAction action = new ButtonAction(list, name);
    keyactions.put(name, action);
    
    log.log(Level.CONFIG, KEY_ACTION_ADDED, name);
  }
  
  /**
   * Removes the {@link KeyListener} with the given name after un-binding it from
   * any keys it is bound to. If there is no {@link ButtonListener} with the given
   * name then this method will not do anything.
   * 
   * @param name The name of the {@link ButtonListener} to remove.
   */
  public void removeAction(String name) {
    ButtonAction action = keyactions.remove(name);
    if(action == null) return;
    
    for(String key : action.keysbound) {
      Collection<ButtonAction> bindings = getBindings(key);
      assert bindings != null;
      
      bindings.remove(action);
    }
    
    log.log(Level.CONFIG, KEY_ACTION_REMOVED, name);
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
      for(ButtonAction act : bindings)
        act.list.buttonPressed(key);
    else
      for(ButtonAction act: bindings)
        act.list.buttonReleased(key);
  }

  @Override
  public void handleEvent(MouseButtonEvent event) {
    String key = Mouse.getButtonName(event.button);
    Collection<ButtonAction> bindings = getBindings(key);
    if(bindings.isEmpty()) return;
    
    if(event.state)
      for(ButtonAction act : bindings)
        act.list.buttonPressed(key);
    else
      for(ButtonAction act : bindings)
        act.list.buttonReleased(key);
  }

  @Override
  protected void resetObject() {
    keyactions.clear();
    keybindings.clear();
  }
  
  private Collection<ButtonAction> getBindings(String key) {
    
    //get or create a collection of actions for binding.
    Collection<ButtonAction> bindings = keybindings.get(key);
    if(bindings == null) {
      bindings = new HashSet();
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
      this.keysbound = new ArrayList<>();
      this.name = name;
    }
  }

  private static final String locprefix = HotkeyTable.class.getName().toLowerCase();
  
  private static final String INVALID_KEY_NAME = locprefix + ".invalid_key_name";
  private static final String INVALID_KEY_ACTION_NAME = locprefix + ".invalid_key_action_name";
  private static final String KEY_ACTION_ALREADY_BOUND = locprefix + ".key_action_already_bound";
  private static final String KEY_ACTION_ADDED = locprefix + ".key_action_added";
  private static final String KEY_ACTION_REMOVED = locprefix + ".key_action_removed";
  private static final String KEY_BINDING_ADDED = locprefix + ".key_binding_added";
  private static final String REMOVED_KEY_BINDING = locprefix + ".removed_key_binding";
  private static final String CLEARED_KEY_BINDINGS = locprefix + ".cleared_key_bindings";

  private static final Logger log = Logger.getLogger(locprefix,
      System.getProperty("taiga.code.logging.text"));
}
