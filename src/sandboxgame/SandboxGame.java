/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandboxgame;

import taiga.gpvm.GameManager;
import org.lwjgl.LWJGLException;
import taiga.gpvm.util.geom.Coordinate;
import hyperion.Generator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.lwjgl.util.vector.Vector3f;
import taiga.code.networking.LoopbackNetwork;
import taiga.code.networking.NetworkManager;
import taiga.code.registration.MissingObjectException;
import taiga.code.util.SettingManager;
import taiga.code.util.Updateable;
import taiga.gpvm.map.Universe;
import taiga.gpvm.map.World;
import taiga.gpvm.registry.RenderingRegistry;
import taiga.gpvm.registry.TileEntry;
import taiga.gpvm.registry.TileRegistry;
import taiga.gpvm.render.StationaryCamera;
import taiga.gpvm.render.WorldRenderer;
import taiga.gpvm.schedule.WorldChange;
import taiga.gpvm.schedule.WorldUpdater;

/**
 *
 * @author russell
 */
public class SandboxGame {

  /**
   * @param args the command line arguments
   * @throws LWJGLException
   * @throws java.lang.InterruptedException
   * @throws java.net.URISyntaxException
   * @throws java.io.IOException
   * @throws java.lang.ReflectiveOperationException
   * @throws taiga.code.registration.MissingObjectException
   * @throws java.io.FileNotFoundException
   * @throws java.lang.InstantiationException
   * @throws java.lang.reflect.InvocationTargetException
   * @throws java.lang.NoSuchMethodException
   * @throws java.lang.NoSuchFieldException
   * @throws java.lang.IllegalAccessException
   * @throws java.lang.ClassNotFoundException
   */
  public static void main(String[] args) throws LWJGLException, InterruptedException, URISyntaxException, IOException, FileNotFoundException, InstantiationException, NoSuchFieldException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ReflectiveOperationException, MissingObjectException {
    System.setProperty("taiga.code.logging.text", "text");
    System.setProperty("taiga.code.text.localization", "text");
    System.setProperty("java.util.logging.config.file", "logging.properties");
    
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
      Logger.getLogger(SandboxGame.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    GameManager game = new GameManager(true, true);
    NetworkManager net = new LoopbackNetwork("network");
    game.addChild(net);
    ((SettingManager)game.getObject("settings")).loadSettings("settings.yml");
    ((TileRegistry)game.getObject("tiles")).loadFile(new File("src/mods/tiles.yml"), "test");
    ((RenderingRegistry)game.getObject("renderinginfo")).loadRenderRegistryData(new File("src/mods/renderer.yml"), "test", SandboxGame.class.getClassLoader());
    
    ((Universe)game.getObject("universe")).addWorld("test", new Generator());
    
    game.start();
    net.scanRegisteredObjects();
    
    World testworld = (World)game.getObject("universe.test");
    WorldUpdater updater = (WorldUpdater)game.getObject("updater");
    testworld.loadRegion(new Coordinate());
    ((WorldRenderer)game.getObject("graphics.gamescreen.test")).setCamera(new StationaryCamera(new Vector3f(0, 0, 1), new Vector3f(0, 1, 0), new Vector3f(16, -32, 16), 60, .1f, 1000f));
    
    TileEntry ent = ((TileRegistry)game.getObject("tiles")).getEntry("test.Grass");
    testupdater test = new testupdater();
    test.ent = ent;
    test.updater = updater;
    test.world = testworld;
    
    updater.addUpdateable(test, 0, 1);
  }
  
  public static class testupdater implements Updateable {

    private int count;
    private TileEntry ent;
    private World world;
    private WorldUpdater updater;
    
    @Override
    public void Update() {
      Coordinate coor = new Coordinate(count % 32, (count / 32) % 32, (count / 1024) % 32);
      
      WorldChange change = new WorldChange(world, coor, ent, count + 100);
      updater.submitTask(change);
      
      count++;
    }
    
  }
}
