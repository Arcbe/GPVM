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

  /**
   * Creates a new {@link InputSystem} with the given name.
   * 
   * @param name The name for the new {@link InputSystem}.
   */
  public InputSystem(String name) {
    super(name);
    
    keylist = new HashSet<>();
    mouselist = new HashSet<>();
    mouseglist = new HashSet<>();
  }
  
  /**
   * Sets whether the mouse should grab the cursor.  If false then the cursor
   * will not be moved by the mouse, and events will send deltas instead of positions.
   * 
   * @param cap Whether to capture the mouse.
   */
  public void setGrabMouse(boolean cap) {
    Mouse.setGrabbed(cap);
  }
  
  public void addMouseListener(MouseListener list) {
    mouselist.add(list);
  }
  
  public void removeMouseListener(MouseListener list) {
    mouselist.remove(list);
  }
  
  public void addGrabbedMouseListener(MouseListener list) {
    mouseglist.add(list);
  }
  
  public void removeGrabbedMouseListener(MouseListener list) {
    mouseglist.remove(list);
  }

  @Override
  public void update() {
    handleKeyboard();
    handleMouse();
  }
  
  private void handleMouse() {
    //same for the mouse
    if(!Mouse.isCreated()) try {
      Mouse.create();
    } catch(LWJGLException ex) {
      log.log(Level.SEVERE, MOUSE_EX, ex);
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
        for(MouseListener list : mouseglist)
          list.handleEvent(event);
      } else {
        for(MouseListener list : mouselist)
          list.handleEvent(event);
      }
    }
  }
  
  private void handleKeyboard() {
    //create the keyboard if needed.  This is done here to insure that the window
    //has been created already.
    if(!Keyboard.isCreated()) try {
      Keyboard.create();
    } catch (LWJGLException ex) {
      log.log(Level.SEVERE, KEYBOARD_EX, ex);
    }
    
    while(Keyboard.next()) {
      KeyboardEvent event = new KeyboardEvent(
        Keyboard.getEventKey(), 
        Keyboard.getEventCharacter(), 
        Keyboard.getEventNanoseconds(), 
        Keyboard.getEventKeyState(), 
        Keyboard.isRepeatEvent());
      
      for(KeyboardListener list : keylist)
        list.handleEvent(event);
      
      //if the event is consumed then go on to the next one.
      if(event.consumed) continue;
      
      handleEvent(event);
    }
  }
  
  private final Collection<KeyboardListener> keylist;
  private final Collection<MouseListener> mouselist;
  private final Collection<MouseListener> mouseglist;
  
  private static final String locprefix = InputSystem.class.getName().toLowerCase();
  
  private static final String KEYBOARD_EX = locprefix + ".keyboard_ex";
  private static final String MOUSE_EX = locprefix + ".mouse_ex";
  
  private static final Logger log = Logger.getLogger(locprefix, 
    System.getProperty("tagia.code.logging.text"));
}
