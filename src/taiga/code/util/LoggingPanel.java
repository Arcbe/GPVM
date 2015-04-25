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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import taiga.code.text.TextLocalizer;

/**
 * A {@link JPanel} that displays information from the java logging api.
 * 
 * @author Russell Smith
 */
@SuppressWarnings("ClassWithMultipleLoggers")
public class LoggingPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  
  public LoggingPanel() {
    records = new ArrayList<>(10);
    maxrecords = -1;
    handler = new MessageHandler();
    source = Logger.getLogger("");
    thandler = new TableHandler();
    table = new JTable();
    loglevel = new JComboBox<>(new Level[]{
      Level.ALL,
      Level.SEVERE,
      Level.WARNING,
      Level.INFO, 
      Level.CONFIG,
      Level.FINE,
      Level.FINER,
      Level.FINEST
    });
    
    init();
  }

  /**
   * Creates a new {@link LoggingPanel} that will receive {@link LogRecord}s
   * from the given logger, and store up to the given amount.
   * 
   * @param log The {@link Logger} to get {@link LogRecord}s from.
   * @param msgs The maximum amount of {@link LogRecord}s to keep.
   */
  public LoggingPanel(Logger log, int msgs) {
    records = Collections.synchronizedList(new ArrayList<>(10));
    maxrecords = msgs;
    handler = new MessageHandler();
    source = log;
    thandler = new TableHandler();
    table = new JTable();
    loglevel = new JComboBox<>(new Level[]{
      Level.ALL,
      Level.SEVERE,
      Level.WARNING,
      Level.INFO, 
      Level.CONFIG,
      Level.FINE,
      Level.FINER,
      Level.FINEST
    });
    
    init();
  }
  
  private void init() {
    table.setAutoCreateRowSorter(true);
    table.setModel(thandler);
    source.addHandler(handler);
    
    setLayout(new BorderLayout(SPACING_AMOUNT, SPACING_AMOUNT));
    add(new JScrollPane(table), BorderLayout.CENTER);
    
    Box bottom = Box.createHorizontalBox();
    bottom.add(new JLabel(TextLocalizer.localize(LABEL_LOG_LEVEL)));
    bottom.add(loglevel);
    bottom.add(Box.createGlue());
    add(bottom, BorderLayout.SOUTH);
  }
  
  private final class MessageHandler extends Handler {

    @Override
    public synchronized void publish(LogRecord record) {
      if(record == null) return;
      
      records.add(record);
      if(records.isEmpty()) return;
      
      thandler.fireTableRowsInserted(records.size() - 1, records.size() - 1);
    }

    @Override
    public void flush() {}

    @Override
    public void close() throws SecurityException {}
    
  }
  
  private final class TableHandler extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    @Override
    public int getRowCount() {
      return records.size();
    }

    @Override
    public int getColumnCount() {
      return 3;
    }

    @Override
    public String getColumnName(int column) {
      switch(column) {
        case 0: return TextLocalizer.localize(COL_NAME_LEVEL);
        case 1: return TextLocalizer.localize(COL_NAME_MESSAGE);
        case 2: return TextLocalizer.localize(COL_NAME_CLASS);
        default: return "";
      }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      LogRecord record = records.get(rowIndex);
      
      switch(columnIndex) {
        case 0: return record.getLevel();
        case 1: return getMessage(record);
        case 2: return record.getSourceClassName();
        default: return null;
      }
    }
    
    public String getMessage(LogRecord record) {
      ResourceBundle bun = record.getResourceBundle();
      if(bun == null) return record.getMessage();
      
      if(!bun.containsKey(record.getMessage())) return record.getMessage();
      
      String result = bun.getString(record.getMessage());
      
      if(record.getParameters() != null) 
        result = MessageFormat.format(result, record.getParameters());
      
      return result;
    }
  }
  
  private final JComboBox<Level> loglevel;
  private final JTable table;
  
  private final Logger source;
  private final TableHandler thandler;
  private final MessageHandler handler;
  private final List<LogRecord> records;
  private final int maxrecords;
  
  private static final int SPACING_AMOUNT = 5;

  private static final String locprefix = LoggingPanel.class.getName().toLowerCase();

  private static final String COL_NAME_LEVEL = locprefix + ".col_name_level";
  private static final String COL_NAME_MESSAGE = locprefix + ".col_name_message";
  private static final String COL_NAME_CLASS = locprefix + ".col_name_class";
  private static final String LABEL_LOG_LEVEL = locprefix + ".label_log_level";
  
  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
