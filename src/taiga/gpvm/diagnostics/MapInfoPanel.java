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

package taiga.gpvm.diagnostics;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import taiga.code.registration.NamedObject;
import taiga.code.text.TextLocalizer;
import taiga.gpvm.GameManager;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.map.Universe;
import taiga.gpvm.map.UniverseListener;
import taiga.gpvm.map.World;

public class MapInfoPanel extends JPanel {

  public MapInfoPanel() {
    super(new BorderLayout());
    
    worldnames = new ArrayList<>(1);
    tablemodel = new TModel();
    worlds = new JTable(tablemodel);
    worldcnt = new JLabel();
    
    setup();
    updateWorldCount();
  }
  
  public MapInfoPanel(GameManager game) {
    super(new BorderLayout());
    man = game;
    
    worldnames = new ArrayList<>(1);
    tablemodel = new TModel();
    worlds = new JTable(tablemodel);
    worldcnt = new JLabel();
    
    setup();
    attachManager();
  }
  
  public void setGameManager(GameManager game) {
    if(man != null)
      clearManager();
    
    man = game;
    
    if(man != null)
      attachManager();
  }
  
  private void clearManager() {
  }
  
  private void attachManager() {
    Universe uni = man.getObject(HardcodedValues.NAME_UNIVERSE);
    
    uni.addListener(new UniverseListener() {

      @Override
      public void worldCreated(World world) {
        addWorld(world);
      }
    });
    
    for(NamedObject obj : uni)
      if(obj instanceof World)
        addWorld((World) obj);
  }
  
  private void addWorld(World world) {
    if(worldnames.contains(world.name)) {
      log.log(Level.WARNING, "Duplicate world added to table.");
      return;
    }
    
    worldnames.add(world.name);
    tablemodel.fireTableRowsInserted(worldnames.size() - 1, worldnames.size() - 1);
    
    updateWorldCount();
  }
  
  private void updateWorldCount() {
    worldcnt.setText(TextLocalizer.localize(LABEL_WORLD_COUNT, worldnames.size()));
  }
  
  private void setup() {
    Box top = Box.createHorizontalBox();
    top.add(worldcnt);
    
    add(top, BorderLayout.NORTH);
  }
  
  private GameManager man;
  
  private final Collection<String> worldnames;
  private final JTable worlds;
  private final JLabel worldcnt;
  private final TModel tablemodel;
  
  private class TModel extends AbstractTableModel {

    @Override
    public int getRowCount() {
      return worldnames.size();
    }

    @Override
    public int getColumnCount() {
      return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
  }

  private static final String locprefix = MapInfoPanel.class.getName().toLowerCase();
  
  private static final String LABEL_WORLD_COUNT = locprefix + ".label_world_count";

  private static final long serialVersionUID = 1L;
  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
