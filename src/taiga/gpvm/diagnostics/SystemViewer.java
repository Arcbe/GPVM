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
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import taiga.code.util.NamingTreeViewer;
import taiga.gpvm.GameManager;

public class SystemViewer extends JPanel {

  public SystemViewer() {
    super(new BorderLayout());
    view = new NamingTreeViewer();
    
    init();
  }
  
  public SystemViewer(GameManager man) {
    super(new BorderLayout());
    view = new NamingTreeViewer(man);
    
    init();
  }
  
  private void init() {
    JScrollPane pane = new JScrollPane();
    pane.setViewportView(view);
    
    add(pane, BorderLayout.CENTER);
  }
  
  public void setGame(GameManager man) {
    view.setRoot(man);
  }
  
  private final NamingTreeViewer view;

  private static final String locprefix = SystemViewer.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
