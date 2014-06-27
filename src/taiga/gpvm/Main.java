package taiga.gpvm;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import taiga.code.input.InputSystem;
import taiga.code.input.KeyboardEvent;
import taiga.code.input.KeyboardListener;
import taiga.code.util.SettingManager;
import taiga.gpvm.map.FixedSizeManager;
import taiga.gpvm.map.FlatWorldGenerator;
import taiga.gpvm.map.Universe;
import taiga.gpvm.map.UniverseListener;
import taiga.gpvm.map.World;
import taiga.gpvm.registry.TileRenderingRegistry;
import taiga.gpvm.registry.SkyEntry;
import taiga.gpvm.registry.SkyRegistry;
import taiga.gpvm.registry.TileEntry;
import taiga.gpvm.registry.TileRegistry;
import taiga.gpvm.render.ColoredSky;
import taiga.gpvm.render.GraphicsRoot;
import taiga.gpvm.render.MobileCamera;
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
    InputSystem input = game.getObject(HardcodedValues.INPUT_SYSTEM_NAME);
    
    //create the camera for the game screen
    WorldRenderer worldview = screen.getObject("test-world");
    final MobileCamera cam = new MobileCamera(new Vector3f(),
      new Vector3f(0,0,1),
      new Vector3f(1, 1, -2f),
      new Vector3f(-1, -1, 20), 
      100, 1, 1000);
    graphics.addUpdateable(cam);
    worldview.setCamera(cam);
    
    input.addAction("move-z", new KeyboardListener() {

      @Override
      public void handleEvent(KeyboardEvent event) {
        if(event.state)
          cam.velocity.z = -.1f;
        else
          cam.velocity.z = 0;
      }
    });
    input.addAction("move+z", new KeyboardListener() {

      @Override
      public void handleEvent(KeyboardEvent event) {
        if(event.state)
          cam.velocity.z = +.1f;
        else
          cam.velocity.z = 0;
      }
    });
    input.addAction("move-y", new KeyboardListener() {

      @Override
      public void handleEvent(KeyboardEvent event) {
        if(event.state)
          cam.velocity.y = -.1f;
        else
          cam.velocity.y = 0;
      }
    });
    input.addAction("move+y", new KeyboardListener() {

      @Override
      public void handleEvent(KeyboardEvent event) {
        if(event.state)
          cam.velocity.y = +.1f;
        else
          cam.velocity.y = 0;
      }
    });
    input.addAction("move-x", new KeyboardListener() {

      @Override
      public void handleEvent(KeyboardEvent event) {
        if(event.state)
          cam.velocity.x = -.1f;
        else
          cam.velocity.x = 0;
      }
    });
    input.addAction("move+x", new KeyboardListener() {

      @Override
      public void handleEvent(KeyboardEvent event) {
        if(event.state)
          cam.velocity.x = +.1f;
        else
          cam.velocity.x = 0;
      }
    });
    
    input.addKeyBinding("Q", "move+x");
    input.addKeyBinding("E", "move-x");
    input.addKeyBinding("A", "move-y");
    input.addKeyBinding("D", "move+y");
    input.addKeyBinding("W", "move+z");
    input.addKeyBinding("S", "move-z");
  }
  
  public static GameManager createGame() throws Exception {
    GameManager game = new GameManager(true, true);
    
    Universe universe = game.getObject(HardcodedValues.UNIVERSE_NAME);
    SettingManager settings = game.getObject(HardcodedValues.SETTING_MANAGER_NAME);
    TileRegistry tiles = game.getObject(HardcodedValues.TILE_REGISTRY_NAME);
    TileRenderingRegistry rendreg = game.getObject(HardcodedValues.TILE_RENDERING_REGISTRY_NAME);
    SkyRegistry skies = game.getObject(HardcodedValues.SKY_REGISTRY_NAME);
    
    settings.loadSettings("settings.yml");
    tiles.loadFile("tiles.yml", "default");
    rendreg.loadRenderingRegistryData("renderer.yml", "default");
    
    universe.addListener(new UniverseListener() {

      @Override
      public void worldCreated(World world) {
        world.addChild(new FixedSizeManager(new Coordinate(), 128, 128, 32));
      }
    });
    
    TileEntry ent = tiles.getEntry("default.grass");
    skies.addEntry(new SkyEntry("test-world", ColoredSky.class));
    World test = universe.addWorld("test-world", new FlatWorldGenerator(ent, 1));
    
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
    TileRenderingRegistry rendreg = client.getObject(HardcodedValues.TILE_RENDERING_REGISTRY_NAME);
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
