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

package taiga.gpvm;

import javax.swing.JFrame;
import taiga.gpvm.diagnostics.DiagnosticsWindow;

public class Main {
  public static final void main(String[] args) throws Exception{
    //sets the localization file for logging messages
    if(System.getProperty("taiga.code.logging.text") == null) System.setProperty("taiga.code.logging.text", "taiga/gpvm/localized-text");
    //sets the localization file for the text localizer
    if(System.getProperty("taiga.code.text.localization") == null) System.setProperty("taiga.code.text.localization", "taiga/gpvm/localized-text");
    //sets the properties for logging
    if(System.getProperty("java.util.logging.config.file") == null) System.setProperty("java.util.logging.config.file", "logging.properties");
    
    JFrame diag = new DiagnosticsWindow();
    diag.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    diag.setVisible(true);
  }
}
