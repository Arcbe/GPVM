/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.opengl.gui;

import java.util.logging.Logger;
import taiga.code.graphics.Drawable;

/**
 * A simple {@link Component} that has a background {@link Drawable}.  The
 * drawing of the {@link Component} is split into two methods, one for the foreground
 * and one for the background.
 * 
 * @author russell
 */
public class BackedComponent extends Component {

  public BackedComponent(String name) {
    super(name);
  }
  
  public void setBackground(Drawable bg) {
    background = bg;
  }
  
  public Drawable getBackground() {
    return background;
  }

  @Override
  protected final void drawComponent() {
    drawBackground();
    
    drawForeground();
  }

  @Override
  protected void updateSelf() {}
  
  protected void drawForeground() {}
  
  protected void drawBackground() {
    if(background == null) return;
    
    background.draw(getX(), getY(), getWidth(), getHeight());
  }

  private Drawable background;
  
  private static final String locprefix = BackedComponent.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
