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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import taiga.code.registration.NamedObject;
import taiga.code.text.TextLocalizer;
import taiga.gpvm.GameManager;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.map.Region;
import taiga.gpvm.map.Universe;
import taiga.gpvm.map.UniverseListener;
import taiga.gpvm.map.World;
import taiga.gpvm.map.WorldListener;

public class MapInfoPanel extends JPanel {
  
  public static final int GUI_SPACING = 5;

  public MapInfoPanel() {
    super(new BorderLayout(GUI_SPACING, GUI_SPACING));
    
    worlds = new ArrayList<>(1);
    tablemodel = new TModel();
    worldtable = new JTable(tablemodel);
    worldcnt = new JLabel();
    regioncnt = new JLabel();
    
    setup();
    updateWorldCount();
  }
  
  public MapInfoPanel(GameManager game) {
    super(new BorderLayout());
    
    worlds = new ArrayList<>(1);
    tablemodel = new TModel();
    worldtable = new JTable(tablemodel);
    worldcnt = new JLabel();
    regioncnt = new JLabel();
    
    setup();
    attachManager(game);
  }
  
  public void setGameManager(GameManager game) {
    if(uni != null)
      clearManager();
    
    if(uni != null)
      attachManager(game);
  }
  
  private void clearManager() {
    tablemodel.fireTableRowsDeleted(0, worlds.size() - 1);
    worlds.clear();
    updateWorldCount();
  }
  
  private void attachManager(GameManager man) {
    uni = man.getObject(HardcodedValues.NAME_UNIVERSE);
    
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
    if(worlds.contains(world)) {
      log.log(Level.WARNING, "Duplicate world added to table.");
      return;
    }
    
    worlds.add(world);
    tablemodel.fireTableRowsInserted(worlds.size() - 1, worlds.size() - 1);
    
    world.addListener(new WorldListener() {

      @Override
      public void regionLoaded(Region reg) {
        updateRegionCount();
      }

      @Override
      public void regionUnloaded(Region reg) {
        updateRegionCount();
      }
    });
    
    updateWorldCount();
  }
  
  private void updateWorldCount() {
    worldcnt.setText(TextLocalizer.localize(LABEL_WORLD_COUNT, worlds.size()));
    
    updateRegionCount();
  }
  
  private void updateRegionCount() {
    int cnt = 0;
    
    for(World world : worlds)
      cnt += world.getRegions().size();
    
    regioncnt.setText(TextLocalizer.localize(LABEL_REGION_COUNT, cnt));
  }
  
  private void setup() {
    Box top = Box.createHorizontalBox();
    top.add(worldcnt);
    top.add(Box.createHorizontalStrut(GUI_SPACING));
    top.add(regioncnt);
    
    JScrollPane pane = new JScrollPane(worldtable);
    add(pane, BorderLayout.CENTER);
    
    add(top, BorderLayout.NORTH);
  }
  
  private Universe uni;
  
  private final List<World> worlds;
  private final JTable worldtable;
  private final JLabel regioncnt;
  private final JLabel worldcnt;
  private final TModel tablemodel;
  
  private class TModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
      return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
      switch(columnIndex) {
        case 0: return String.class;
        case 1: return Short.class;
        case 2: return Integer.class;
        default: return null;
      }
    }

    @Override
    public String getColumnName(int column) {
      switch(column) {
        case 0: return TextLocalizer.localize(TABLE_COLUMN_WORLD_NAME);
        case 1: return TextLocalizer.localize(TABLE_COLUMN_WORLD_ID);
        case 2: return TextLocalizer.localize(TABLE_COLUMN_REGION_COUNT);
        default: return null;
      }
    }
    
    @Override
    public int getRowCount() {
      return worlds.size();
    }

    @Override
    public int getColumnCount() {
      return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      World w = worlds.get(rowIndex);
      
      switch(columnIndex) {
        case 0: return w.name;
        case 1: return w.getWorldID();
        case 2: return w.getRegions().size();
      }
      
      log.log(Level.WARNING, "Bad column index for world table: {0}.", columnIndex);
      return null;
    }
    
  }

  private static final String locprefix = MapInfoPanel.class.getName().toLowerCase();
  
  private static final String LABEL_REGION_COUNT = locprefix + ".label_region_count";
  private static final String LABEL_WORLD_COUNT = locprefix + ".label_world_count";
  private static final String TABLE_COLUMN_WORLD_NAME = locprefix + ".table_column_world_name";
  private static final String TABLE_COLUMN_WORLD_ID = locprefix + ".table_column_world_id";
  private static final String TABLE_COLUMN_REGION_COUNT = locprefix + ".table_column_region_count";

  private static final long serialVersionUID = 1L;
  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
