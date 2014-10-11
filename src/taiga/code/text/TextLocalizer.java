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

package taiga.code.text;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A convenience class for localizing text within a program.  This class will use
 * the 'taiga.code.text.localization' system property to localize text.
 * @author russell
 */
public class TextLocalizer {
  /**
   * Attempts to localize a given {@link String}.  If it cannot localize the string
   * this method will simply return the input {@link String} instead.
   * @param input
   * @return 
   */
  public static String localize(String input) {
    if(text == null) {
      return input;
    }
    
    if(text.containsKey(input)) return text.getString(input);
    
    log.log(Level.WARNING, NO_ENTRY, input);
    return input;
  }
  
  /**
   * Attempts to localize a given string along with attempting to format it use
   * the {@link MessageFormat} class.  
   * 
   * @param input The string to localize
   * @param args The arguments to pass to the {@link MessageFormat#format(java.lang.String, java.lang.Object...) } method.
   * @return The localized and formatted version of the given string, or the input
   *  if no localization can be found.
   */
  public static String localize(String input, Object ... args) {
    String local = localize(input);
    
    if(local == null || local == input) return local;
    return MessageFormat.format(local, args);
  }
  
  private static ResourceBundle text;
  
  private static final String locprefix = TextLocalizer.class.getName().toLowerCase();
  
  private static final String NO_ENTRY = locprefix + ".no_entry";
  
  private static final Logger log = Logger.getLogger(locprefix, 
    System.getProperty("taiga.code.logging.text"));
  
  static {
    try {
      text = ResourceBundle.getBundle(System.getProperty("taiga.code.logging.text"));
    } catch (Exception ex) {
      log.log(Level.WARNING, "Could not load resource bundle.", ex);
    }
  }
}
