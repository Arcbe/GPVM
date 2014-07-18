/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.opengl.gui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.opengl.Drawable;
import taiga.code.util.DataNode;

/**
 * A simple {@link Component} that has a background {@link Drawable}.  The
 * drawing of the {@link Component} is split into two methods, one for the foreground
 * and one for the background.
 * 
 * @author russell
 */
public class BackedComponent extends Component {
  
  public static final String FIELD_NAME_PADDING = "padding";
  public static final String FIELD_NAME_BACKGROUND = "background";
  public static final String FIELD_NAME_BG_CLASS = "class";

  public BackedComponent(String name) {
    super(name);
  }
  
  public BackedComponent(DataNode data) {
    super(data);
    
    data = (DataNode) data.data;
    
    Integer temp = data.getValueByName(FIELD_NAME_PADDING);
    if(temp != null) padding = temp;
    
    if(data.getDataNode(FIELD_NAME_BACKGROUND) != null) {
      String bgclass = data.getValueByName(FIELD_NAME_BACKGROUND, FIELD_NAME_BG_CLASS);
      
      if(bgclass == null) {
        log.log(Level.SEVERE, NO_BG_CLASS);
      } else {
        
        try {
          Class<? extends Drawable> bgc = 
            (Class<? extends Drawable>) getClass().getClassLoader().loadClass((String) bgclass);
          
          try {
            Constructor<? extends Drawable> cons;

            cons = bgc.getConstructor(DataNode.class);
            background = cons.newInstance(data.getDataNode(FIELD_NAME_BACKGROUND));
          } catch(InvocationTargetException |
            NoSuchMethodException | 
            SecurityException |
            InstantiationException |
            IllegalAccessException ex) {
            
            background = bgc.newInstance();
          }
        } catch(ClassNotFoundException |
          SecurityException |
          InstantiationException |
          IllegalAccessException ex) {
          
          log.log(Level.SEVERE, UNABLE_TO_LOAD_BG_CLASS, ex);
        }
      }
    }
  }
  
  public void setBackground(Drawable bg) {
    background = bg;
  }
  
  public Drawable getBackground() {
    return background;
  }
  
  public void setPadding(int pad) {
    padding = pad;
  }
  
  public int getPadding() {
    return padding;
  }

  @Override
  protected final void drawComponent() {
    drawBackground();
    
    drawForeground();
  }

  @Override
  protected void updateSelf() {}

  @Override
  protected void initializeSelf() {
    super.initializeSelf();
    
    if(background != null) {
      background.load();
    }
  }
  
  protected void drawForeground() {}
  
  protected void drawBackground() {
    if(background == null) return;
    
    background.draw(getX(), getY(), getWidth(), getHeight());
  }

  private Drawable background;
  private int padding;
  
  private static final String locprefix = BackedComponent.class.getName().toLowerCase();
  
  private static final String NO_BG_CLASS = locprefix + ".no_bg_class";
  private static final String UNABLE_TO_LOAD_BG_CLASS = locprefix + ".unable_to_load_bg_class";

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
