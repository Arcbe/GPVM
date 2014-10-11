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
