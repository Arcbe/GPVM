package taiga.code.opengl;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import taiga.code.registration.RegisteredObject;
import taiga.code.registration.RegisteredSystem;
import taiga.code.text.TextLocalizer;
import taiga.code.util.Setting;
import taiga.code.util.SettingManager;

/**
 * Manages the OpenGL window and graphics thread.  Settings will automatically be
 * loaded from an attached {@link SettingManager}.
 * 
 * @author russell
 */
public abstract class GraphicsSystem extends RegisteredSystem implements Runnable {
  
  public static final boolean DEFAULT_VSYNC = false;
  public static final boolean DEFAULT_FULLSCREEN = false;
  public static final int DEFAULT_RES_HEIGHT = 600;
  public static final int DEFAULT_RES_WIDTH = 800;

  public GraphicsSystem(String name) {
    super(name);
    
    res_x = DEFAULT_RES_WIDTH;
    res_y = DEFAULT_RES_HEIGHT;
    fullscreen = DEFAULT_FULLSCREEN;
    vsync = DEFAULT_VSYNC;
    reset = true;
    running = false;
    gthread = null;
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
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        
        Display.update();
        
        //abstract methods for the implementing class to hook into
        update();
        renderScene();
        
      } while(running && !Display.isCloseRequested());
    } catch(LWJGLException ex) {
      
    } finally {
      running = false;
      Display.destroy();
    }
  }
  
  private boolean fullscreen;
  private boolean vsync;
  private int res_x;
  private int res_y;
  private volatile boolean reset;
  private volatile boolean running;
  private Thread gthread;
  
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
  }
  
  private void update() {
    //todo update updateable thigns on the registered tree
  }

  protected abstract void renderScene();
  
  private static final String locprefix = GraphicsSystem.class.getName().toLowerCase();
  
  public static final String RESOLUTION_HEIGHT = TextLocalizer.localize(locprefix + ".res_height");
  public static final String RESOLUTION_WIDTH = TextLocalizer.localize(locprefix + ".res_width");
  public static final String FULL_SCREEN = TextLocalizer.localize(locprefix + ".full_screen");
  public static final String VSYNC = TextLocalizer.localize(locprefix + ".vsync");
  public static final String GRAPHICS_THREAD_DIED = TextLocalizer.localize(locprefix + ".graphics_thread_died");
  public static final String ALREADY_STARTED = TextLocalizer.localize(locprefix + ".already_started");
  public static final String STARTING = TextLocalizer.localize(locprefix + ".starting");
  
  
  private static final String NO_SETTINGS = locprefix + ".no_settings";
  private static final String MISSING_SETTING = locprefix + ".missing_setting";
  private static final String WRONG_SETTING_TYPE = locprefix + ".wrong_setting_type";
  private static final String CREATING_WINDOW = locprefix + ".creating_window";
  
  private static final Logger log = Logger.getLogger(locprefix, System.getProperty("taiga.code.logging.text"));
}
