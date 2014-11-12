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

package taiga.code.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import taiga.code.text.TextLocalizer;

/**
 * A {@link JPanel} that displays basic information about the state of the
 * system running the current program.
 * 
 * @author Russell Smith
 */
public class EnvironmentViewer extends JPanel {
  
  /**
   * The number of pixels in between GUI elements.
   */
  public static final int GUI_SPACING = 5;
  
  /**
   * The time between updates of the displayed state information in milliseconds.
   */
  public static final int UPDATE_INTERVAL = 500;

  /**
   * Constructs a new {@link EnvironmentViewer} and populates it with the current
   * system information.
   */
  public EnvironmentViewer() {
    super(new BorderLayout(5, 5));
    maxmem = new JLabel();
    procs = new JLabel();
    memusage = new JProgressBar(JProgressBar.HORIZONTAL);
    
    memusage.setBorderPainted(true);
    memusage.setStringPainted(true);
    
    //create the content for the environment variable list.
    StringBuilder str = new StringBuilder(100);
    str.append("<html><body><ul>");
    
    for(Map.Entry<Object, Object> ent : System.getProperties().entrySet()) {
      str.append("<li><u>");
      str.append(ent.getKey());
      str.append(":</u> ");
      
      //get hte variable and split the string into multiple lines at semicolons.
      String tmp = ent.getValue().toString();
      tmp = tmp.replaceAll("([:;])(.[^/])", "$1<br>$2");
      
      str.append(tmp);
      str.append("</li>");
    }
    
    str.append("</ul></body></html>");
    
    JLabel text = new JLabel(str.toString());
    text.setHorizontalTextPosition(SwingConstants.LEFT);
    
    JScrollPane scroll = new JScrollPane(text);
    scroll.setBorder(new TitledBorder(TextLocalizer.localize(NAME_PROPERTIES)));
    scroll.setPreferredSize(new Dimension(0,0));
    
    add(scroll, BorderLayout.CENTER);
    
    Box bot = Box.createVerticalBox();
    bot.setBorder(new TitledBorder(TextLocalizer.localize(NAME_RESOURCES)));
    add(bot, BorderLayout.SOUTH);
    
    bot.add(procs);
    bot.add(Box.createVerticalStrut(5));
    bot.add(maxmem);
    bot.add(Box.createVerticalStrut(5));
    bot.add(memusage);
    
    updateMem();
    
    Timer time = new Timer(500, new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        updateMem();
      }
    });
    time.start();
  }
  
  private void updateMem() {
    Runtime rt = Runtime.getRuntime();
    
    procs.setText(TextLocalizer.localize(LABEL_AVA_PROCS, rt.availableProcessors()));
    maxmem.setText(TextLocalizer.localize(LABEL_MAX_MEM, rt.maxMemory()));
    
    int tmem = (int) (rt.totalMemory() / 1024);
    int fmem = (int) (rt.freeMemory() / 1024);
    
    memusage.setMaximum(tmem);
    memusage.setValue(fmem);
    memusage.setString(TextLocalizer.localize(LABEL_FREE_MEM, fmem, tmem));
  }
  
  private final JLabel procs;
  private final JLabel maxmem;
  private final JProgressBar memusage;

  private static final String locprefix = EnvironmentViewer.class.getName().toLowerCase();
  private static final long serialVersionUID = 1L;
  
  private static final String NAME_PROPERTIES = locprefix + ".name_properties";
  private static final String NAME_RESOURCES = locprefix + ".name_resources";
  private static final String LABEL_FREE_MEM = locprefix + ".label_free_mem";
  private static final String LABEL_MAX_MEM = locprefix + ".label_max_mem";
  private static final String LABEL_AVA_PROCS = locprefix + ".label_ava_procs";

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
