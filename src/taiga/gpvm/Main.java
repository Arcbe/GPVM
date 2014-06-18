package taiga.gpvm;

import java.io.IOException;
import org.lwjgl.util.vector.Vector3f;
import taiga.code.networking.LoopbackNetwork;
import taiga.code.registration.RegisteredSystem;
import taiga.code.registration.SystemListener;
import taiga.code.util.SettingManager;
import taiga.gpvm.map.FixedSizeManager;
import taiga.gpvm.map.FlatWorldGenerator;
import taiga.gpvm.map.Universe;
import taiga.gpvm.map.UniverseListener;
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
    
//    final GameManager server = createServer();
//    final GameManager client = createClient();
//    
//    
//    //connect the client and server.
//    final LoopbackNetwork servernet = new LoopbackNetwork("network", true, false);
//    final LoopbackNetwork clientnet = new LoopbackNetwork("network", false, true);
//    
//    //create a shutdown hook for the server
//    client.addSystemListener(new SystemListener() {
//
//      @Override
//      public void systemStarted(RegisteredSystem sys) {}
//
//      @Override
//      public void systemStopped(RegisteredSystem sys) {
//        server.stop();
//        servernet.stop();
//      }
//    });
//    
//    //add the servers network and set it up
//    server.addChild(servernet);
//    servernet.scanRegisteredObjects();
//    
//    //add the clients network and set it up
//    client.addChild(clientnet);
//    clientnet.scanRegisteredObjects();
//    clientnet.connect(servernet);
    
    GameManager game = createGame();
    GraphicsRoot graphics = game.getObject(HardcodedValues.GRAPHICSSYSTEM_NAME);
    GameScreen screen = graphics.getObject(HardcodedValues.GAME_SCREEN_NAME);
    
    //create the camera for the game screen
    WorldRenderer worldview = screen.getObject("test-world");
    worldview.setCamera(new StationaryCamera(new Vector3f(0,0,1),
      new Vector3f(1, 1, -1f),
      new Vector3f(-5, -5, 5), 
      100, 1, 100));
  }
  
  public static GameManager createGame() throws Exception {
    GameManager game = new GameManager(true, true);
    
    Universe universe = game.getObject(HardcodedValues.UNIVERSE_NAME);
    SettingManager settings = game.getObject(HardcodedValues.SETTING_MANAGER_NAME);
    TileRegistry tiles = game.getObject(HardcodedValues.TILE_REGISTRY_NAME);
    RenderingRegistry rendreg = game.getObject(HardcodedValues.RENDERING_REGISTRY_NAME);
    
    settings.loadSettings("settings.yml");
    tiles.loadFile("tiles.yml", "default");
    rendreg.loadRenderingRegistryData("renderer.yml", "default");
    
    universe.addListener(new UniverseListener() {

      @Override
      public void worldCreated(World world) {
        world.addChild(new FixedSizeManager(new Coordinate(), 128, 128, 32));
      }
    });
    
    TileEntry ent = tiles.getEntry("default.Grass");
    World test = universe.addWorld("test-world", new FlatWorldGenerator(ent, 2));
    
    game.start();
    return game;
  }
  
  public static GameManager createServer() throws Exception {
    //create a server for the game
    final GameManager server = new GameManager(true, false);
    Universe        suniverse = server.getObject(HardcodedValues.UNIVERSE_NAME);
    SettingManager  ssettings = server.getObject(HardcodedValues.SETTING_MANAGER_NAME);
    TileRegistry    stiles = server.getObject(HardcodedValues.TILE_REGISTRY_NAME);
    
    //load the server settings
    ssettings.loadSettings("settings.yml");
    //load the server tiles
    stiles.loadFile("tiles.yml", "default");
    
    //create a new world.
    TileEntry ent = stiles.getEntry("default.Grass");
    suniverse.addWorld("test-world", new FlatWorldGenerator(ent, 2));
    ((World)suniverse.getObject("test-world")).addChild(new FixedSizeManager(new Coordinate(), 128, 128, 32));
    
    //start the server.
    server.start();
    
    return server;
  }
  
  public static GameManager createClient() throws Exception {//create a new game and retreive the systems from it
    GameManager       client = new GameManager(false, true);
    Universe          universe = client.getObject(HardcodedValues.UNIVERSE_NAME);
    SettingManager    settings = client.getObject(HardcodedValues.SETTING_MANAGER_NAME);
    TileRegistry      tiles = client.getObject(HardcodedValues.TILE_REGISTRY_NAME);
    RenderingRegistry rendreg = client.getObject(HardcodedValues.RENDERING_REGISTRY_NAME);
    GraphicsRoot      graphics = client.getObject(HardcodedValues.GRAPHICSSYSTEM_NAME);
    GameScreen        screen = graphics.getObject(HardcodedValues.GAME_SCREEN_NAME);
    
    //load the settings file
    settings.loadSettings("settings.yml");
    //load tile definitions
    tiles.loadFile("tiles.yml", "default");
    //load rendering information
    rendreg.loadRenderingRegistryData("renderer.yml", "default");
    
    //add a region manager to load the map
    universe.addListener(new UniverseListener() {

      @Override
      public void worldCreated(World world) {
        world.addChild(new FixedSizeManager(new Coordinate(), 128, 128, 32));
      }
    });
    
    client.start();
    
    return client;
  }
}
