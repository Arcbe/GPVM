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
