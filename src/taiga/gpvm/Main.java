package taiga.gpvm;

import org.lwjgl.util.vector.Vector3f;
import taiga.code.util.SettingManager;
import taiga.gpvm.map.FixedSizeManager;
import taiga.gpvm.map.FlatWorldGenerator;
import taiga.gpvm.map.Universe;
import taiga.gpvm.map.World;
import taiga.gpvm.registry.RenderingRegistry;
import taiga.gpvm.registry.TileEntry;
import taiga.gpvm.registry.TileRegistry;
import taiga.gpvm.render.GraphicsRoot;
import taiga.gpvm.render.StationaryCamera;
import taiga.gpvm.render.WorldRenderer;
import taiga.gpvm.screens.GameScreen;
import taiga.gpvm.util.geom.Coordinate;

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
    Universe universe = game.getObject(HardcodedValues.UNIVERSE_NAME);
    SettingManager settings = game.getObject(HardcodedValues.SETTING_MANAGER_NAME);
    TileRegistry tiles = game.getObject(HardcodedValues.TILE_REGISTRY_NAME);
    RenderingRegistry rendreg = game.getObject(HardcodedValues.RENDERING_REGISTRY_NAME);
    GraphicsRoot graphics = game.getObject(HardcodedValues.GRAPHICSSYSTEM_NAME);
    GameScreen screen = graphics.getObject(HardcodedValues.GAME_SCREEN_NAME);
    
    //load the settings file
    settings.loadSettings("settings.yml");
    //load tile definitions
    tiles.loadFile("tiles.yml", "default");
    //load rendering information
    rendreg.loadRenderingRegistryData("renderer.yml", "default");
    
    //create a new world.
    TileEntry ent = tiles.getEntry("default.Grass");
    universe.addWorld("test-world", new FlatWorldGenerator(ent, 2));
    World testworld = universe.getObject("test-world");
    //add a region manager to load the map
    testworld.addChild(new FixedSizeManager(new Coordinate(), 128, 128, 32));
    
    //create the camera for the game screen
    WorldRenderer worldview = screen.getObject("test-world");
    worldview.setCamera(new StationaryCamera(new Vector3f(0,0,1),
      new Vector3f(1, 1, -.2f), 
      new Vector3f(-5, -5, 5), 
      60, 1, 100));
    
    game.start();
  }
}
