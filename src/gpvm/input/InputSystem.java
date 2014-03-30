package gpvm.input;

import gpvm.render.GraphicsSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.input.Keyboard;

/**
 * Handles the input for the game.  Due to the nature of LWJGL, this class
 * is dependent on the rendering system for polling the devices as well as
 * using the rendering thread for processing events.
 * 
 * @author russell
 */
public class InputSystem {
  /**
   * Returns the current instance of the InputSystem.
   * 
   * @return 
   */
  public static InputSystem getInstance() {
    return instance;
  }
  
  /**
   * Allows the {@link InputSystem} to process all events that are pending.
   * This method should be called each time the display is updated or the input
   * is polled through some method.  The {@link GraphicsSystem} will handle
   * calls to this method in most cases.
   */
  public void pump() {
    handleKeyboard();
  }
  
  /**
   * Checks to see if there is a {@link KeyListener} bound to a specific
   * key.
   * 
   * @param key The key to check.  The key must be one of the constants from {@link Keyboard}
   * @return Whether there is a {@link KeyListener} bound.
   */
  public boolean isKeyBound(int key) {
    if(!keylists.containsKey(key)) return false;
    return !keylists.get(key).isEmpty();
  }
  
  /**
   * Binds a {@link KeyListener} to a key.
   * 
   * @param key The to bind the {@link KeyListener} to.  This must be one of the
   * constants from the {@link Keyboard} class.
   * @param list The {@link KeyListener} to bind to the given key.
   */
  public void addKeyListener(int key, KeyListener list) {
    if(keylists.containsKey(key)) {
      keylists.get(key).add(list);
    } else {
      List<KeyListener> l = new ArrayList<>();
      l.add(list);
      
      keylists.put(key, l);
    }
  }
  
  private InputSystem() {
    keylists = new HashMap<>();
  }
  
  private void handleKeyboard() {
    //go through all of the events in the queue.
    while(Keyboard.next()) {
      int key = Keyboard.getEventKey();
      
      List<KeyListener> curlists = keylists.get(key);
      if(curlists == null) continue;
      
      //true for if the key was down, false if up.
      if(Keyboard.getEventKeyState()) {
        for(KeyListener list : curlists)
                list.keyPressed(key);
      } else {
        for(KeyListener list : curlists)
          list.keyReleased(key);
      }
    }
  }
  
  private Map<Integer, List<KeyListener>> keylists;
  
  private static InputSystem instance = new InputSystem();
}
