package gpvm.editor.panels;

import gpvm.Registrar;
import gpvm.map.TileDefinition;
import gpvm.map.TileRegistry;
import gpvm.map.TileRegistryListener;
import gpvm.render.RenderInfo;
import gpvm.render.RenderRegistry;
import gpvm.render.RenderRegistryListener;
import gpvm.render.TileRenderer;
import gpvm.util.Settings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * A {@link JComponent} that displays the contents of the {@link RenderRegistry}
 * stored in the {@link Registrar}
 * 
 * @author russell
 */
public class RenderRegistryPanel extends JScrollPane {
  /**
   * Construct a new {@link RenderRegistryPanel} and begins listening for
   * events from the {@link RenderRegistry}.
   */
  public RenderRegistryPanel() {
    setName(Settings.getLocalString("panel_name_render_registry"));
    
    model = new RenderRegModel();
    content = new JTable(model);
    setViewportView(content);
    
    Registrar.getInstance().addTileRegistryListener(model);
    Registrar.getInstance().addRenderRegistryListener(model);
  }
  
  private JTable content;
  private RenderRegModel model;
  
  private class RenderRegModel extends AbstractTableModel implements RenderRegistryListener, TileRegistryListener {
    /**
     * Creates a new model for the {@link RenderRegistry}.
     */
    public RenderRegModel() {
      entries = Collections.synchronizedList(new ArrayList<Entry>());
    }
    
    public void rescanRegistry() {
      entries.clear();
      
      TileRegistry.ReadOnlyTileRegistry tiles = Registrar.getInstance().getTileRegistry();
      RenderRegistry.ReadOnlyRenderRegistry renderer = Registrar.getInstance().getRenderRegistry();
      
      //entries should be added for all tiles that have definitions regardless
      //of whether they are present in the RenderRegistry.
      long ids[] = tiles.getTileIDs();
      
      for(int i = 0; i < ids.length; i++) {
        Entry entry = new Entry();
        entry.id = ids[i];
        entry.name = tiles.getDefinition(ids[i]).canonname;
        
        RenderRegistry.RendererEntry rent = renderer.getEntry(i);
        if(rent != null) {
          entry.info = rent.info;
          entry.renderer = rent.renderer.getCanonicalName();
        }
        
        entries.add(entry);
      }
      
      fireTableDataChanged();
    }
    
    @Override
    public String getColumnName(int index) {
      switch(index) {
        default: return null;
        case 0: return Settings.getLocalString("col_name_canonname");
        case 1: return Settings.getLocalString("col_name_tileid");
        case 2: return Settings.getLocalString("col_name_renderer");
        case 3: return Settings.getLocalString("col_name_render_info");
      }
    }
    
    @Override
    public int getRowCount() {
      return entries.size();
    }

    @Override
    public int getColumnCount() {
      return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      switch(columnIndex) {
        default: return null;
        case 0: return entries.get(rowIndex).name;
        case 1: return entries.get(rowIndex).id;
        case 2: return entries.get(rowIndex).renderer;
        case 3: return entries.get(rowIndex).info;
      }
    }
    
    List<Entry> entries;

    @Override
    public void registryCleared() {
      rescanRegistry();
    }

    @Override
    public void entryAdded(long tileid, RenderRegistry.RendererEntry entry) {
       Entry ent = new Entry();
       
       ent.id = tileid;
       ent.info = entry.info;
       ent.renderer = entry.renderer.getCanonicalName();
       
       int ind;
       for(ind = 0; ind < entries.size(); ind++) {
         if(entries.get(ind).id == tileid) {
           ent.name = entries.get(ind).name;
           entries.set(ind, ent);
           
           fireTableRowsUpdated(ind, ind);
           return;
         }
       }
    }

    @Override
    public void entryAdded(TileDefinition def, long id) {
      //we will need a new entry for this.
      Entry entry = new Entry();
      
      entry.id = id;
      entry.name = def.canonname;
      
      RenderRegistry.RendererEntry rent = Registrar.getInstance().getRenderRegistry().getEntry(id);
      if(rent != null) {
        entry.info = rent.info;
        entry.renderer = rent.renderer.getCanonicalName();
      }
      
      entries.add(entry);
      fireTableRowsInserted(entries.size() - 1, entries.size() - 1);
    }
    
    private class Entry {
      public String renderer;
      public RenderInfo info;
      public String name;
      public long id;
    }
  }
}
