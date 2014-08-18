package taiga.code.opengl;

import java.awt.Window;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import taiga.code.math.Matrix4;
import taiga.code.registration.NamedObject;
import taiga.code.registration.NamedSystem;
import taiga.code.util.Setting;
import taiga.code.util.Updateable;

/**
 * Manages the OpenGL window and graphics thread.  Settings will automatically be
 * loaded from an attached {@link SettingManager}.
 * 
 * @author russell
 */
public class GraphicsSystem extends NamedSystem implements Runnable {
  /**
   * Default setting for vertical synchronization.
   */
  public static final boolean DEFAULT_VSYNC = false;
  
  /**
   * The name of that will be used to look for a vsync setting in the 
   * naming tree.
   */
  public static final String SETTING_VSYNC = "vsync";
  
  /**
   * Default setting for full screen.
   */
  public static final boolean DEFAULT_FULLSCREEN = false;
  
  /**
   * The name of that will be used to look for a full screen setting in 
   * the naming tree.
   */
  public static final String SETTING_FULLSCREEN = "fullscreen";
  
  /**
   * Default vertical resolution.
   */
  public static final int DEFAULT_RES_HEIGHT = 600;
  
  /**
   * The name of that will be used to look for a vertical resolution 
   * setting in the naming tree.
   */
  public static final String SETTING_RES_HEIGHT = "resolution-height";
  
  /**
   * Default horizontal resolution.
   */
  public static final int DEFAULT_RES_WIDTH = 800;
  
  /**
   * The name of that will be used to look for a horizontal resolution 
   * setting in the naming tree.
   */
  public static final String SETTING_RES_WIDTH = "resolution-width";
  
  /**
   * Default target frame rate for the screen.
   */
  public static final int DEFAULT_TARGET_FRAME_RATE = -1;

  /**
   * The name of that will be used to look for a fps setting in the naming tree.
   */
  public static final String SETTING_FPS = "fps";
  
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
    target_fps = DEFAULT_TARGET_FRAME_RATE;
    
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
  
  /**
   * Changes the resolution for the window if it is created, or sets the
   * resolution that will be used upon creation.  A value of 0 or less in
   * either argument will indicate that value should not be changed.  A change in
   * resolution will require the window to be reset.
   * 
   * @param width The horizontal resolution for the window.
   * @param height The vertical resolution for the window.
   */
  public void setResolution(int width, int height) {
    boolean change = false;
    
    if(width > 0 && width != res_x) {
      change = true;
      res_x = width;
    }
    
    if(height > 0 && height != res_y) {
      change = true;
      res_y = height;
    }
    
    if(change) {
      reset();
    }
  }
  
  /**
   * Sets whether the window should be full screen.  If a window is already
   * create this will reset the {@link GraphicsSystem}.
   * 
   * @param fs Whether the window should be full screen.
   */
  public void setFullscreen(boolean fs) {
    if(fs != fullscreen) {
      fullscreen = fs;
      reset();
    }
  }
  
  /**
   * Sets whether vertical synchronization should be used.  This has no
   * effect unless full screen mode is used. This will not cause a reset to the
   * {@link GraphicsSystem}.
   * 
   * @param vs Whether to enable vsync.
   */
  public void setVsync(boolean vs) {
    vsync = vs;
    Display.setVSyncEnabled(vsync);
  }
  
  /**
   * Sets the desired frames per second for the {@link GraphicsSystem}.
   * This is a best effort attempt and not guaranteed.  This will not cause a 
   * reset of the {@link GraphicsSystem}.  A value of zero or less indicates that
   * no frame rate limiting should be applied.
   * 
   * @param fps The target fps or -1 for unlimited.
   */
  public void setTargetFPS(int fps) {
    target_fps = fps;
  }

  @Override
  protected synchronized void startSystem() {
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
  
  /**
   * Loads settings for the {@link GraphicsSystem} from the naming tree.
   * Settings will be collected from the {@link NamedObject} with the given name.
   * Any setting that is not found will not be changed from its current value.
   * 
   * @param settings The name of the {@link NamedObject} that contains the settings.
   */
  public void loadSettings(String ... settings) {
    loadSettings(getObject(settings));
  }
  
  /**
   * Loads settings for the {@link GraphicsSystem} from the naming tree.
   * Settings will be collected from the {@link NamedObject} with the given name.
   * Any setting that is not found will not be changed from its current value.
   * This will cause a reset of the {@link GraphicsSystem} if any values that
   * require it change.
   * 
   * @param settings The name of the {@link NamedObject} that contains the settings.
   */
  public void loadSettings(String settings) {
    loadSettings(getObject(settings));
  }
  
  /**
   * Loads settings for the {@link GraphicsSystem} from the naming tree.
   * Settings will be collected from the {@link NamedObject} with the given name.
   * Any setting that is not found will not be changed from its current value.
   * This will cause a reset of the {@link GraphicsSystem} if any values that
   * require it change.
   * 
   * @param obj The {@link NamedObject} that contains the settings to load.
   */
  public void loadSettings(NamedObject obj) {
    if(obj == null) {
      log.log(Level.WARNING, NO_SETTINGS);
      return;
    }
    
    Setting cur;
    
    //Resolution width
    if((cur = obj.getObject(SETTING_RES_WIDTH)) == null) {
      log.log(Level.WARNING, MISSING_SETTING, SETTING_RES_WIDTH);
    } else if(cur.data instanceof Number) {
      res_x = ((Number)cur.data).intValue();
    } else {
      log.log(Level.WARNING, WRONG_SETTING_TYPE, SETTING_RES_WIDTH);
    }
    
    //Resolution height
    if((cur = obj.getObject(SETTING_RES_HEIGHT)) == null) {
      log.log(Level.WARNING, MISSING_SETTING, SETTING_RES_HEIGHT);
    } else if(cur.data instanceof Number) {
      res_y = ((Number)cur.data).intValue();
    } else {
      log.log(Level.WARNING, WRONG_SETTING_TYPE, SETTING_RES_HEIGHT);
    }
    
    //full screen
    if((cur = obj.getObject(SETTING_FULLSCREEN)) == null) {
      log.log(Level.WARNING, MISSING_SETTING, SETTING_FULLSCREEN);
    } else if(cur.data instanceof Boolean) {
      fullscreen = ((Boolean)cur.data);
    } else {
      log.log(Level.WARNING, WRONG_SETTING_TYPE, SETTING_FULLSCREEN);
    }
    
    //vsync
    if((cur = obj.getObject(SETTING_VSYNC)) == null) {
      log.log(Level.WARNING, MISSING_SETTING, SETTING_VSYNC);
    } else if(cur.data instanceof Boolean) {
      vsync = ((Boolean)cur.data);
    } else {
      log.log(Level.WARNING, WRONG_SETTING_TYPE, SETTING_VSYNC);
    }
    
    //fps
    if((cur = obj.getObject(SETTING_FPS)) == null) {
      log.log(Level.WARNING, MISSING_SETTING, SETTING_FPS);
    } else if(cur.data instanceof Number) {
      target_fps = ((Number)cur.data).intValue();
    } else {
      log.log(Level.WARNING, WRONG_SETTING_TYPE, SETTING_FPS);
    }
    
    //TODO: make this only called when needed.
    reset();
  }

  @Override
  protected synchronized void stopSystem() {
    Thread gt = gthread;
    
    running = false;
    gthread = null;
    
    try {
      if(Thread.currentThread() != gt)
        gt.join();
    } catch (InterruptedException ex) {
      log.log(Level.SEVERE, STOP_SYSTEM_EX, ex);
    }
  }

  @Override
  protected synchronized void resetObject() {
    reset = true;
    
    //if the graphics thread called this return now to prevent locking.
    if(Thread.currentThread() == gthread)
      return;
    
    //wait for the system to reset.
    while(running && !reset) {
      try {
        this.wait();
      } catch(InterruptedException ex) {}
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
        
        if(target_fps <= 0)
          Display.update();
        else
          Display.sync(target_fps);
        
      } while(running && !Display.isCloseRequested());
       
    } catch(LWJGLException ex) {
      log.log(Level.SEVERE, UNHANDLED_EX, ex);
    } finally {
      running = false;
      Display.destroy();
      
      fireWindowClosed();
    }
  }

  /**
   * Called before each frame between updating and rendering.
   * 
   * @return The {@link Matrix4} to use for projection of 3d coordinates. A null
   * value will indicate that the identity {@link Matrix4} should be used.
   */
  protected Matrix4 rendering() { return null; }
  
  private volatile boolean reset;
  private volatile boolean running;
  
  private boolean fullscreen;
  private boolean vsync;
  private int target_fps;
  private int res_x;
  private int res_y;
  
  private Thread gthread;
  private final Collection<WindowListener> listeners;
  private final Collection<Updateable> updaters;
  
  private void createWindow() throws LWJGLException {
    DisplayMode mode = new DisplayMode(res_x, res_y);
    
    Display.setFullscreen(fullscreen);
    Display.setVSyncEnabled(vsync);
    Display.setDisplayMode(mode);
    
    Display.create();
    
    fireWindowCreated();
  }
  
  private void update() {
    for(NamedObject obj : this) {
      if(obj != null && obj instanceof Renderable)
        ((Renderable)obj).update();
    }
    
    for(Updateable up : updaters)
      up.update();
  }
  
  private void render() {
    Matrix4 proj = rendering();
    if(proj == null) proj = new Matrix4();
    
    //get the number of passes
    int passes = 0;
    for(NamedObject obj : this) {
      if(obj != null && obj instanceof Renderable) {
        int npasses = ((Renderable)obj).getNumberOfPasses();
        if(passes < npasses) passes = npasses;
      }
    }
    
    //now do the passes
    for(int i = 0; i < passes; i++) {
      for(NamedObject obj : this) {
        if(obj != null && obj instanceof Renderable)
          ((Renderable)obj).render(i, proj);
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
  
  private static final String GRAPHICS_THREAD_DIED = locprefix + ".graphics_thread_died";
  private static final String ALREADY_STARTED = locprefix + ".already_started";
  private static final String STARTING = locprefix + ".starting";
  private static final String NO_SETTINGS = locprefix + ".no_settings";
  private static final String MISSING_SETTING = locprefix + ".missing_setting";
  private static final String WRONG_SETTING_TYPE = locprefix + ".wrong_setting_type";
  private static final String CREATING_WINDOW = locprefix + ".creating_window";
  private static final String STOP_SYSTEM_EX = locprefix + ".stop_system_ex";
  private static final String UNHANDLED_EX = locprefix + ".unhandled_ex";
  
  private static final Logger log = Logger.getLogger(locprefix, System.getProperty("taiga.code.logging.text"));
}
