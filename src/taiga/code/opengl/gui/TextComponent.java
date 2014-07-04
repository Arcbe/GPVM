/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.opengl.gui;

import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.util.FontUtils;

public class TextComponent extends BackedComponent {

  public TextComponent(String name) {
    super(name);
  }
  
  @Override
  public void drawForeground() {
    if(font == null) return;
    
    for(int i = 0; i < text.length; i++) {
      int x = getLineX(i);
      int y = getLineY(i);
      int w = getLineWidth(i);
      
      FontUtils.drawString(font, name, alignment, x, y, w, color);
    }
  }
  
  protected int getLineX(int line) {
    return getPadding();
  }
  
  protected int getLineY(int line) {
    return getPadding() + line * (linespacing + font.getLineHeight()) + font.getLineHeight();
  }
  
  protected int getLineWidth(int line) {
    return getWidth() - getPadding() * 2;
  }
  
  protected String[] text;
  protected Color color;
  protected Font font;
  protected int alignment;
  protected int linespacing;

  private static final String locprefix = TextComponent.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
