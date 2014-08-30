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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import taiga.code.registration.NamedSystem;
import taiga.code.registration.SystemListener;
import taiga.code.text.TextLocalizer;
import taiga.gpvm.GameManager;

/**
 * A window that provides internal information about an instance of the 
 * GPVM framework.
 * 
 * @author Russell Smith
 */
public class DiagnosticsWindow extends JFrame implements SystemListener { 
  
  public DiagnosticsWindow() {
    setTitle(TextLocalizer.localize(WINDOW_TITLE));
    
    toggle_game = new JButton(TextLocalizer.localize(BUTTON_CREATE));
    reset_game = new JButton(TextLocalizer.localize(BUTTON_RESTART));
    
    game_status = new JLabel(TextLocalizer.localize(LABEL_NO_INSTANCE));
    
    setButtonActions();
    
    add(createButtonPanel());
    
    pack();
  }
  
  public void setInstance(GameManager target) {
    //cleanup any old listener registration
    if(instance != null) {
      instance.removeSystemListener(this);
    }
    
    instance = target;
    
    //add new listeners
    instance.addSystemListener(this);
    
    if(instance.isRunning()) {
      toggle_game.setText(TextLocalizer.localize(BUTTON_STOP));
      game_status.setText(TextLocalizer.localize(LABEL_RUNNING));
    } else {
      toggle_game.setText(TextLocalizer.localize(BUTTON_START));
      game_status.setText(TextLocalizer.localize(LABEL_STOPPED));
    }
  }
  
  private JComponent createButtonPanel() {
    Box pan = Box.createHorizontalBox();
    
    pan.add(toggle_game);
    pan.add(Box.createHorizontalStrut(SPACING_AMOUNT));
    pan.add(reset_game);
    pan.add(Box.createHorizontalStrut(SPACING_AMOUNT));
    pan.add(game_status);
    
    return pan;
  }
  
  private void setButtonActions() {
    toggle_game.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        //create a new instance if needed.
        if(instance == null) {
          setInstance(new GameManager(false, false));
        } else if(instance.isRunning()) {
          instance.stop();
        } else {
          instance.start();
        }
      }
    });
  }

  @Override
  public void systemStarted(NamedSystem sys) {
    toggle_game.setText(TextLocalizer.localize(BUTTON_STOP));
    game_status.setText(TextLocalizer.localize(LABEL_RUNNING));
  }

  @Override
  public void systemStopped(NamedSystem sys) {
    toggle_game.setText(TextLocalizer.localize(BUTTON_START));
    game_status.setText(TextLocalizer.localize(LABEL_STOPPED));
  }
  
  private GameManager instance;
  
  private final JButton toggle_game;
  private final JButton reset_game;
  
  private final JLabel game_status;
  
  private static final int SPACING_AMOUNT = 5;

  private static final String locprefix = DiagnosticsWindow.class.getName().toLowerCase();
  
  private static final String WINDOW_TITLE = locprefix + ".window_title";
  private static final String BUTTON_STOP = locprefix + ".button_stop";
  private static final String BUTTON_START = locprefix + ".button_start";
  private static final String BUTTON_CREATE = locprefix + ".button_create";
  private static final String BUTTON_RESTART = locprefix + ".button_restart";
  private static final String LABEL_NO_INSTANCE = locprefix + ".label_no_instance";
  private static final String LABEL_RUNNING = locprefix + ".label_running";
  private static final String LABEL_STOPPED = locprefix + ".label_stopped";

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
