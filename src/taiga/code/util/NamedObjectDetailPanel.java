/*
 * Copyright (C) 2014 Russell Smith
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package taiga.code.util;

import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import taiga.code.registration.NamedObject;
import taiga.code.text.TextLocalizer;

public final class NamedObjectDetailPanel extends Box {

  public NamedObjectDetailPanel() {
    super(BoxLayout.Y_AXIS);
    
    text = new JTextArea();
    text.setEditable(false);
    text.setColumns(COLUMNS);
    
    add(text);
    add(Box.createVerticalGlue());
  }
  
  public NamedObjectDetailPanel(NamedObject tar) {
    super(BoxLayout.Y_AXIS);
    
    text = new JTextArea();
    text.setEditable(false);
    text.setColumns(COLUMNS);
    
    add(text);
    add(Box.createVerticalGlue());
    
    setObject(tar);
  }
  
  public void setObject(NamedObject obj) {
    target = obj;
    
    populateText();
  }
  
  private void populateText() {
    StringBuilder build = new StringBuilder();
    
    String fullname = target.getFullName();
    build.append(TextLocalizer.localize(FIELD_NAME, fullname));
    
    text.setText(build.toString());
  }
  
  private final JTextArea text;
  
  private NamedObject target;
  
  private static final int COLUMNS = 15;

  private static final String locprefix = NamedObjectDetailPanel.class.getName().toLowerCase();
  
  private static final String FIELD_NAME = locprefix + ".field_name";

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
