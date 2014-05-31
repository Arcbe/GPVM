/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    //get the resource bundle if need.
    if(text == null) {
      if(System.getProperty("taiga.code.text.localization") == null) {
        log.log(Level.WARNING, NO_LOCALIZATION);
        
        return input;
      }
      
      text = ResourceBundle.getBundle(System.getProperty("taiga.code.text.localization"));
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
  
  private static final String NO_LOCALIZATION = TextLocalizer.class.getName().toLowerCase() + ".no_localization";
  private static final String NO_ENTRY = TextLocalizer.class.getName().toLowerCase() + ".no_entry";
  
  private static final Logger log = Logger.getLogger(TextLocalizer.class.getName(), 
    System.getProperty("taiga.code.logging.text"));
}
