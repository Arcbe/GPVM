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

  public InputSystem(String name) {
    super(name);
    
    listeners = new HashSet<>();
  }

  @Override
  public void Update() {
    //create the keyboard if needed.  This is done here to insure that the window
    //has been created already.
    if(!Keyboard.isCreated()) try {
      Keyboard.create();
    } catch (LWJGLException ex) {
      log.log(Level.SEVERE, KEYBOARD_EX, ex);
    }
    
    //same for the mouse
    if(!Mouse.isCreated()) try {
      Mouse.create();
    } catch(LWJGLException ex) {
      log.log(Level.SEVERE, MOUSE_EX, ex);
    }
    
    while(Keyboard.next()) {
      KeyboardEvent event = new KeyboardEvent(
        Keyboard.getEventKey(), 
        Keyboard.getEventCharacter(), 
        Keyboard.getEventNanoseconds(), 
        Keyboard.getEventKeyState(), 
        Keyboard.isRepeatEvent());
      
      for(KeyboardListener list : listeners)
        list.handleEvent(event);
      
      //if the event is consumed then go on to the next one.
      if(event.consumed) continue;
      
      handleEvent(event);
    }
  }
  
  private final Collection<KeyboardListener> listeners;
  
  private static final String locprefix = InputSystem.class.getName().toLowerCase();
  
  private static final String KEYBOARD_EX = locprefix + ".keyboard_ex";
  private static final String MOUSE_EX = locprefix + ".mouse_ex";
  
  private static final Logger log = Logger.getLogger(locprefix, 
    System.getProperty("tagia.code.logging.text"));
}