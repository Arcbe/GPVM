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

package taiga.code.opengl.gui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import taiga.code.math.Matrix4;
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
  protected final void drawComponent(Matrix4 proj) {
    final FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
    
//    applyProjection(proj);
//    applyGlobalModelView();
    
    drawBackground();
    
    drawForeground();
  }

  @Override
  protected void updateRenderable() {}

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
    
    background.draw(0, 0, getWidth(), getHeight());
  }

  private Drawable background;
  private int padding;
  
  private static final String locprefix = BackedComponent.class.getName().toLowerCase();
  
  private static final String NO_BG_CLASS = locprefix + ".no_bg_class";
  private static final String UNABLE_TO_LOAD_BG_CLASS = locprefix + ".unable_to_load_bg_class";

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
