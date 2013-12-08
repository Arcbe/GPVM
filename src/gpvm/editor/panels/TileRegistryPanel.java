package gpvm.editor.panels;

import gpvm.Registrar;
import gpvm.ThreadingManager;
import gpvm.map.TileDefinition;
import gpvm.map.TileRegistry;
import gpvm.map.TileRegistryListener;
import gpvm.util.StringManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * A component to display the contents of the {@link TileRegistry} contained
 * in the {@link Registrar}.
 * 
 * @author russell
 */
public class TileRegistryPanel extends JScrollPane {
  public TileRegistryPanel() {
    setName(StringManager.getLocalString("panel_name_tile_registry"));
    
    model = new TileRegTableModel();
    content = new JTable(model);
    this.setViewportView(content);
    Registrar.getInstance().addTileRegistryListener(model);
  }
  
  private JTable content;
  private TileRegTableModel model;
  
  /**
   * A data model for a table displaying the {@link TileDefinition}s contained
   * in within the {@link Registrar}. The columns will be as follows:
   *  -name
   *  -tile id
   *  -canonical name
   *  -metadata
   *  -opaque
   */
  private static final class TileRegTableModel extends AbstractTableModel implements TileRegistryListener {
    public TileRegTableModel() {
      entries = Collections.synchronizedList(new ArrayList<Entry>());
      rescanRegistry();
    }
    
    public void rescanRegistry() {
      entries.clear();
      
      //make sure to properly multithread
      ThreadingManager.getInstance().requestRead();
      
      try {
        //get all of the entries from the registry.
        TileRegistry.ReadOnlyTileRegistry reg = Registrar.getInstance().getTileRegistry();
        
        long[] ids = reg.getTileIDs();
        for(int i = 0; i < ids.length; i++) {
          Entry e = new Entry();
          e.def = reg.getDefinition(ids[i]);
          e.id = ids[i];
          entries.add(e);
        }
      } finally {
        ThreadingManager.getInstance().releaseRead();
      }
      
      //fire all of the events.
      this.fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
      return entries.size();
    }

    @Override
    public int getColumnCount() {
      return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      switch(columnIndex) {
        default: return null;
        case 0: return entries.get(rowIndex).def.name;
        case 1: return entries.get(rowIndex).id;
        case 2: return entries.get(rowIndex).def.canonname;
        case 3: return entries.get(rowIndex).def.metadata;
        case 4: return entries.get(rowIndex).def.opaque;
      }
    }
    
    @Override
    public String getColumnName(int index) {
      switch (index){
        default: return null;
        case 0: return StringManager.getLocalString("col_name_tilename");
        case 1: return StringManager.getLocalString("col_name_tileid");
        case 2: return StringManager.getLocalString("col_name_canonname");
        case 3: return StringManager.getLocalString("col_name_metadata");
        case 4: return StringManager.getLocalString("col_name_opaque");
      }
    }
    
    private List<Entry> entries;

    @Override
    public void registryCleared() {
      rescanRegistry();
    }

    @Override
    public void entryAdded(TileDefinition def, long id) {
      Entry entry = new Entry();
      entry.def = def;
      entry.id = id;
      
      entries.add(entry);
      fireTableRowsInserted(entries.size() - 1, entries.size() - 1);
    }
    
    private static class Entry {
      public TileDefinition def;
      public long id;
    }
  }
}
