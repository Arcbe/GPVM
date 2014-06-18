package taiga.code.input;

import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Keyboard;
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
    keybindings = new TreeMap<>();
  }
  
  /**
   * Adds {@link KeyListener} to the key with the given name.
   * 
   * @param key The name of the key to add {@link KeyListener}s to.
   * @param actions The name for the {@link KeyListener}s to add.
   */
  public void addKeyBinding(String key, String ... actions) {
    Collection<KeyAction> bindings = getBindings(key);
    if(bindings == null) return;
    
    int index = Keyboard.getKeyIndex(key);
    for(String act : actions) {
      KeyAction action = keyactions.get(act);
      
      //make sure that it is a valid action
      if(action == null) {
        log.log(Level.WARNING, INVALID_KEY_ACTION_NAME, act);
        continue;
      }
      
      //Make sure the action is not already bound to this key
      if(action.keysbound.contains(index)) {
        log.log(Level.WARNING, KEY_ACTION_ALREADY_BOUND, new Object[] {act, key});
        continue;
      }
      
      //finally bind the action.
      action.keysbound.add(index);
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
    Collection<KeyAction> bindings = getBindings(key);
    if(bindings == null) return;
    
    int index = Keyboard.getKeyIndex(key);
    for(KeyAction act : bindings) {
      if(act.keysbound.contains(index)) {
        act.keysbound.remove(index);
        bindings.remove(act);
        
        log.log(Level.CONFIG, REMOVED_KEY_BINDING, new Object[]{key, act});
      }
    }
  }
  
  /**
   * Removes all {@link KeyListener}s bound to the key with the given name.  If
   * the key does not exist or there are no {@link KeyListener}s bound then this
   * method will do nothing.
   * 
   * @param key The name of the key to clear of bound {@link KeyListener}s.
   */
  public void removeAllBindings(String key) {
    
    Collection<KeyAction> bindings = getBindings(key);
    if(bindings == null) return;
    
    int index = Keyboard.getKeyIndex(key);
    for(KeyAction act : bindings) {
      act.keysbound.remove(index);
    }
    
    bindings.clear();
    
    log.log(Level.CONFIG, CLEARED_KEY_BINDINGS, key);
  }
  
  /**
   * Adds a {@link KeyListener} to the list of available {@link KeyListener}s with
   * the given name.  The {@link KeyListener} can then be bound to a key using the
   * {@link HotkeyTable#addKeyBinding(java.lang.String, java.lang.String...) }
   * method.
   * 
   * @param name The name that can be used to reference the new {@link KeyListener}.
   * @param list The {@link KeyListener} to add to this {@link HotkeyTable}.
   */
  public void addAction(String name, KeyboardListener list) {
    KeyAction action = new KeyAction(list, name);
    keyactions.put(name, action);
    
    log.log(Level.CONFIG, KEY_ACTION_ADDED, name);
  }
  
  /**
   * Removes the {@link KeyListener} with the given name after un-binding it from
   * any keys it is bound to. If there is no {@link KeyListener} with the given
   * name then this method will not do anything.
   * 
   * @param name The name of the {@link KeyListener} to remove.
   */
  public void removeAction(String name) {
    KeyAction action = keyactions.remove(name);
    if(action == null) return;
    
    for(int index : action.keysbound) {
      Collection<KeyAction> bindings = getBindings(index);
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
    int index = event.key;
    Collection<KeyAction> bindings = getBindings(index);
    if(bindings == null) return;
    
    for(KeyAction action : bindings) {
      action.list.handleEvent(event);
    }
  }

  @Override
  protected void resetObject() {
    keyactions.clear();
    keybindings.clear();
  }
  
  private Collection<KeyAction> getBindings(String key) {
    return getBindings(Keyboard.getKeyIndex(key), key);
  }
  
  private Collection<KeyAction> getBindings(int index) {
    return getBindings(index, Keyboard.getKeyName(index));
  }
  
  private Collection<KeyAction> getBindings(int index, String key) {
    if(index == Keyboard.KEY_NONE) {
      log.log(Level.WARNING, INVALID_KEY_NAME, key);
      return null;
    }
    
    //get or create a collection of actions for binding.
    Collection bindings = keybindings.get(index);
    if(bindings == null) {
      bindings = new HashSet();
      keybindings.put(index, bindings);
    }
    
    return bindings;
  }
  
  private final Map<String, KeyAction> keyactions;
  private final Map<Integer, Collection<KeyAction>> keybindings;
  
  private static class KeyAction {
    public final KeyboardListener list;
    public final Collection<Integer> keysbound;
    public final String name;

    public KeyAction(KeyboardListener list, String name) {
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
