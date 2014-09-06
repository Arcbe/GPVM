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

package taiga.gpvm.diagnostics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import taiga.code.util.NamedObjectDetailPanel;
import taiga.code.util.NamingTreeViewer;
import taiga.gpvm.GameManager;

public class SystemViewer extends JPanel {

  public SystemViewer() {
    super(new BorderLayout(SPACING_AMOUNT, SPACING_AMOUNT));
    view = new NamingTreeViewer();
    details = new NamedObjectDetailPanel();
    
    init();
  }
  
  public SystemViewer(GameManager man) {
    super(new BorderLayout());
    view = new NamingTreeViewer(man);
    details = new NamedObjectDetailPanel(man);
    
    init();
  }
  
  private void init() {
    JScrollPane pane = new JScrollPane();
    pane.setViewportView(view);
    
    details.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
    details.setBackground(Color.lightGray);
    
    add(pane, BorderLayout.CENTER);
    add(details, BorderLayout.EAST);
  }
  
  public void setGame(GameManager man) {
    view.setRoot(man);
    details.setObject(man);
  }
  
  private final NamingTreeViewer view;
  private final NamedObjectDetailPanel details;
  
  private static final int SPACING_AMOUNT = 5;

  private static final String locprefix = SystemViewer.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
