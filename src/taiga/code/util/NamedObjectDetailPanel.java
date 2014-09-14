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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import taiga.code.registration.NamedObject;
import taiga.code.text.TextLocalizer;

public final class NamedObjectDetailPanel extends Box {
  
  public static final int TAB_SIZE = 2;

  public NamedObjectDetailPanel() {
    super(BoxLayout.Y_AXIS);
    
    text = new JLabel();
    
    add(text);
    add(Box.createVerticalGlue());
  }
  
  public NamedObjectDetailPanel(NamedObject tar) {
    super(BoxLayout.Y_AXIS);
    
    text = new JLabel();
    
    add(text);
    add(Box.createVerticalGlue());
    
    setObject(tar);
  }
  
  public void setObject(NamedObject obj) {
    target = obj;
    
    StringBuilder build = new StringBuilder();
    build.append("<html><b>");
    populateName(build);
    populateInfo(build);
    
    build.append("</html>");
    text.setText(build.toString());
  }
  
  private void populateName(StringBuilder builder) {
    builder.append(TextLocalizer.localize(FIELD_NAME));
    builder.append(target.getFullName());
  }
  
  private void populateInfo(StringBuilder builder) {
    BeanInfo info;
    
    try {
      info = Introspector.getBeanInfo(target.getClass());
    } catch (IntrospectionException ex) {
      log.log(Level.SEVERE, "Error inspecting object class.", ex);
      return;
    }
    
    builder.append(TextLocalizer.localize(FIELD_DESC));
    builder.append(info.getBeanDescriptor().getShortDescription());
    
    builder.append(TextLocalizer.localize(FIELD_PROP));
    builder.append("<ul>");
    
    for(PropertyDescriptor prop : info.getPropertyDescriptors()) {
      builder.append("<li>");
      
      if(prop.isBound()) builder.append('B');
      if(prop.isConstrained()) builder.append('C');
      if(prop.getReadMethod() != null) builder.append('R');
      if(prop.getWriteMethod() != null) builder.append('W');
      
      builder.append(" - ");
      builder.append(prop.getDisplayName());
    }
    
    builder.append("</ul>");
  }
  
  private final JLabel text;
  
  private NamedObject target;
  
  private static final int COLUMNS = 15;

  private static final String locprefix = NamedObjectDetailPanel.class.getName().toLowerCase();
  
  private static final String FIELD_NAME = locprefix + ".field_name";
  private static final String FIELD_DESC = locprefix + ".field_desc";
  private static final String FIELD_PROP = locprefix + ".field_prop";

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
