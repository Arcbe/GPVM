package taiga.gpvm;

import taiga.code.util.SettingManager;
import taiga.gpvm.map.Universe;
import taiga.gpvm.registry.TileRegistry;

public class Main {
  public static final void main(String[] args) throws Exception{
    //sets the localization file for logging messages
    if(System.getProperty("taiga.code.logging.text") == null) System.setProperty("taiga.code.logging.text", "localized-text");
    //sets the localization file for the text localizer
    if(System.getProperty("taiga.code.text.localization") == null) System.setProperty("taiga.code.text.localization", "localized-text");
    //sets the properties for logging
    if(System.getProperty("java.util.logging.config.file") == null) System.setProperty("java.util.logging.config.file", "logging.properties");
    
    //create a new game and retreive the systems from it
    GameManager game = new GameManager(true, true);
    Universe universe = (Universe) game.getObject(HardcodedValues.UNIVERSE_NAME);
    SettingManager settings = (SettingManager) game.getObject(HardcodedValues.SETTING_MANAGER_NAME);
    TileRegistry tiles = (TileRegistry) game.getObject(HardcodedValues.TILE_REGISTRY_NAME);
    
    //load the settings file
    settings.loadSettings("settings.yml");
    //load tile definitions
    tiles.loadFile("tiles.yml", "default");
  }
}
