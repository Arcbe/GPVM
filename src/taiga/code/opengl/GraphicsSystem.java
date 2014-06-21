package taiga.code.opengl;

import java.awt.Window;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import taiga.code.graphics.Renderable;
import taiga.code.registration.RegisteredObject;
import taiga.code.registration.RegisteredSystem;
import taiga.code.text.TextLocalizer;
import taiga.code.util.Setting;
import taiga.code.util.SettingManager;
import taiga.code.util.Updateable;

/**
 * Manages the OpenGL window and graphics thread.  Settings will automatically be
 * loaded from an attached {@link SettingManager}.
 * 
 * @author russell
 */
public abstract class GraphicsSystem extends RegisteredSystem implements Runnable {
  /**
   * Default setting for vertical synchronization.
   */
  public static final boolean DEFAULT_VSYNC = false;
  
  /**
   * Default setting for full screen.
   */
  public static final boolean DEFAULT_FULLSCREEN = false;
  
  /**
   * Default vertical resolution.
   */
  public static final int DEFAULT_RES_HEIGHT = 600;
  /**
   * Default horizontal resolution.
   */
  public static final int DEFAULT_RES_WIDTH = 800;

  /**
   * Creates a new {@link GraphicsSystem} with the given name and default settings.
   * 
   * @param name The name for the new {@link GraphicsSystem}.
   */
  public GraphicsSystem(String name) {
    super(name);
    
    res_x = DEFAULT_RES_WIDTH;
    res_y = DEFAULT_RES_HEIGHT;
    fullscreen = DEFAULT_FULLSCREEN;
    vsync = DEFAULT_VSYNC;
    reset = true;
    running = false;
    gthread = null;
    listeners = new HashSet<>();
    updaters = new HashSet<>();
  }
  
  /**
   * Adds a {@link WindowListener} to the parent {@link Window} of this
   * {@link GraphicsSystem}.  Listeners will be preserved across changes to the
   * parent {@link Window}.
   * 
   * @param list The {@link WindowListener} to add.
   */
  public void addWindowListener(WindowListener list) {
    listeners.add(list);
  }
  
  /**
   * Removes a previously added {@link WindowListener} form the parent {@link Window}
   * of this {@link GraphicsSystem}.
   * 
   * @param list The {@link WindowListener} to remove.
   */
  public void removeWindowListener(WindowListener list) {
    listeners.remove(list);
  }
  
  /**
   * Adds a new {@link Updateable} to this {@link GraphicsSystem}.  The {@link Updateable}
   * will be updated at the beginning of every update of the {@link GraphicsSystem}.
   * 
   * @param up THe {@link Updateable} to add.
   */
  public void addUpdateable(Updateable up) {
    updaters.add(up);
  }
  
  /**
   * Removes a previously added {@link Updateable} from this {@link GraphicsSystem}.
   * 
   * @param up The {@link Updateable} to remove.
   */
  public void removeUpdateable(Updateable up) {
    updaters.remove(up);
  }

  @Override
  protected void startSystem() {
    if(running) {
      if(!gthread.isAlive()) {
        log.log(Level.SEVERE, GRAPHICS_THREAD_DIED);
      } else {
        log.log(Level.WARNING, ALREADY_STARTED);
        return;
      }
    }
    
    log.log(Level.INFO, STARTING);
    gthread = new Thread(this, getFullName());
    gthread.start();
  }

  @Override
  protected void stopSystem() {
  }

  @Override
  protected void resetObject() {
    synchronized (this) {
      reset = true;
      
      if(!running) {
        updateSettings();
      } else {
        while(running && !reset) {
          try {
            this.wait();
          } catch (InterruptedException ex) {}
        }
      }
    }
  }

  /**
   * Starting point for the graphics thread.
   */
  @Override
  public void run() {
    //this will create the window and graphics context.
    reset = true;
    running = true;
    
    try {
       do {
        //create the window if needed.
        if(reset) {
          log.log(Level.INFO, CREATING_WINDOW);

          updateSettings();
          createWindow();
          reset = false;
          
          //make sure to notify when resetting is complete
          synchronized(this) {
            this.notifyAll();
          }
        }
        
        //abstract methods for the implementing class to hook into
        update();
        render();
        
        Display.update();
        
      } while(running && !Display.isCloseRequested());
    } catch(LWJGLException ex) {
      
    } finally {
      running = false;
      Display.destroy();
      
      fireWindowClosed();
    }
  }

  /**
   * Called before each frame between updating and rendering.
   */
  protected abstract void rendering();
  
  private boolean fullscreen;
  private boolean vsync;
  private int res_x;
  private int res_y;
  private volatile boolean reset;
  private volatile boolean running;
  private Thread gthread;
  private final Collection<WindowListener> listeners;
  private final Collection<Updateable> updaters;
  
  private void updateSettings() {
    RegisteredObject set = getObject(SettingManager.SETTINGMANAGER_NAME);
    if(set == null || !(set instanceof SettingManager)) {
      log.log(Level.WARNING, NO_SETTINGS);
      return;
    }
    
    SettingManager settings = (SettingManager)set;
    //Resolution height
    Setting cur = settings.getSetting(RESOLUTION_HEIGHT);
    if(cur == null) {
      log.log(Level.WARNING, MISSING_SETTING, RESOLUTION_HEIGHT);
    } else if(cur.getValue() instanceof Number) {
      res_y = ((Number)cur.getValue()).intValue();
    } else {
      log.log(Level.WARNING, WRONG_SETTING_TYPE, RESOLUTION_HEIGHT);;
    }
    
    //Resolution width
    cur = settings.getSetting(RESOLUTION_WIDTH);
    if(cur == null) {
      log.log(Level.WARNING, MISSING_SETTING, RESOLUTION_WIDTH);
    } else if(cur.getValue() instanceof Number) {
      res_x = ((Number)cur.getValue()).intValue();
    } else {
      log.log(Level.WARNING, WRONG_SETTING_TYPE, RESOLUTION_WIDTH);;
    }
    
    //Fullscreen
    cur = settings.getSetting(FULL_SCREEN);
    if(cur == null) {
      log.log(Level.WARNING, MISSING_SETTING, FULL_SCREEN);
    } else if(cur.getValue() instanceof Boolean) {
      fullscreen = (Boolean)cur.getValue();
    } else {
      log.log(Level.WARNING, WRONG_SETTING_TYPE, FULL_SCREEN);;
    }
  }
  
  private void createWindow() throws LWJGLException {
    DisplayMode mode = new DisplayMode(res_x, res_y);
    
    Display.setFullscreen(fullscreen);
    Display.setVSyncEnabled(vsync);
    Display.setDisplayMode(mode);
    
    Display.create();
    
    fireWindowCreated();
  }
  
  private void update() {
    for(RegisteredObject obj : this) {
      if(obj != null && obj instanceof Renderable)
        ((Renderable)obj).update();
    }
    
    for(Updateable up : updaters)
      up.Update();
  }
  
  private void render() {
    rendering();
    
    //get the number of passes
    int passes = 0;
    for(RegisteredObject obj : this) {
      if(obj != null && obj instanceof Renderable) {
        int npasses = ((Renderable)obj).getNumberOfPasses();
        if(passes < npasses) passes = npasses;
      }
    }
    
    //now do the passes
    for(int i = 0; i < passes; i++) {
      for(RegisteredObject obj : this) {
        if(obj != null && obj instanceof Renderable)
          ((Renderable)obj).render(i);
      }
    }
  }
  
  private void fireWindowClosed() {
    for(WindowListener list : listeners)
      list.windowDestroyed();
  }
  
  private void fireWindowCreated() {
    for(WindowListener list : listeners)
      list.windowCreated();
  }
  
  private static final String locprefix = GraphicsSystem.class.getName().toLowerCase();
  
  /**
   * Identifiers for the vertical resolution setting within the registration tree.
   */
  public static final String RESOLUTION_HEIGHT = TextLocalizer.localize(locprefix + ".res_height");
  /**
   * Identifiers for the horizontal resolution setting within the registration tree.
   */
  public static final String RESOLUTION_WIDTH = TextLocalizer.localize(locprefix + ".res_width");
  /**
   * Identifiers for the fullscreen setting within the registration tree.
   */
  public static final String FULL_SCREEN = TextLocalizer.localize(locprefix + ".full_screen");
  /**
   * Identifiers for the vertical synchronization setting within the registration tree.
   */
  public static final String VSYNC = TextLocalizer.localize(locprefix + ".vsync");
  
  private static final String GRAPHICS_THREAD_DIED = TextLocalizer.localize(locprefix + ".graphics_thread_died");
  private static final String ALREADY_STARTED = TextLocalizer.localize(locprefix + ".already_started");
  private static final String STARTING = TextLocalizer.localize(locprefix + ".starting");
  private static final String NO_SETTINGS = locprefix + ".no_settings";
  private static final String MISSING_SETTING = locprefix + ".missing_setting";
  private static final String WRONG_SETTING_TYPE = locprefix + ".wrong_setting_type";
  private static final String CREATING_WINDOW = locprefix + ".creating_window";
  
  private static final Logger log = Logger.getLogger(locprefix, System.getProperty("taiga.code.logging.text"));
}
