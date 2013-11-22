
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.editor.panels;

import gpvm.modding.Mod;
import gpvm.modding.Mod.ModIdentifier;
import gpvm.modding.ModManager;
import gpvm.util.Settings;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author russell
 */
public class ModInformationPanel extends JPanel{
  public static final int PADDING = 2;
  
  public ModInformationPanel() {
    super(new BorderLayout(PADDING, PADDING));
    setName(Settings.getLocalString("panel_name_mod_information"));
    loaded = false;
    
    //create the list of mods
    modlist = new JList<>(ModManager.getInstance().getFoundMods());
    JScrollPane scrolls = new JScrollPane();
    scrolls.getViewport().add(modlist);
    add(scrolls, BorderLayout.WEST);
    
    //setup the space for displaying information about the mod.
    JTabbedPane content = new JTabbedPane();
    display = new ModInfoDisplay();
    content.add(display);
    add(content, BorderLayout.CENTER);
    
    //create the listener for the selection events
    modlist.addListSelectionListener(new ListSelectionListener() {

      @Override
      public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) return;
        
        current = ModManager.getInstance().getMod(modlist.getSelectedValue());
        display.displayMod(current);
        
        if(current != null && !loaded) load.setEnabled(true);
      }
    });
    
    //create the panel for buttons
    load = new JButton(Settings.getLocalString("button_load_mod"));
    load.setEnabled(false);
    JPanel bot = new JPanel(new FlowLayout(FlowLayout.LEFT));
    
    bot.add(load);
    add(bot, BorderLayout.SOUTH);
  }
  
  private JButton load;
  private ModInfoDisplay display;
  private JList<ModIdentifier> modlist;
  private Mod current;
  private boolean loaded;
  
  private static class ModInfoDisplay extends JPanel {
    public static int NUM_COLS = 2;
    public static int NUM_ROWS = 2;
    
    private JLabel name;
    private JLabel version;
    private JLabel mapgenerator;
    
    public ModInfoDisplay() {
      super(new GridLayout(NUM_ROWS, NUM_COLS));
      setName(Settings.getLocalString("mod_info_display_name"));
      
      name = new JLabel();
      version = new JLabel();
      mapgenerator = new JLabel();
      add(name);
      add(version);
      add(mapgenerator);
    }
    
    public void displayMod(Mod m) {
      if(m == null) return;
      
      ModIdentifier id = m.getIdentifier();
      name.setText(String.format(Settings.getLocalString("mod_info_display_name_label"), id.name));
      version.setText(String.format(Settings.getLocalString("mod_info_display_version_label"), id.version));
    }
  }
}
