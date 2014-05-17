/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandboxgame;

import gpvm.GameManager;
import gpvm.editor.panels.ModInformationPanel;
import gpvm.editor.panels.RenderRegistryPanel;
import gpvm.editor.panels.TileRegistryPanel;
import gpvm.input.InputSystem;
import gpvm.input.KeyListener;
import gpvm.io.DataLoader;
import gpvm.io.YAMLLoader;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import gpvm.modding.ModManager;
import taiga.gpvm.render.GraphicsRoot;
import gpvm.util.StringManager;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;
import taiga.code.networking.LoopbackNetwork;
import taiga.code.networking.NetworkManager;

/**
 *
 * @author russell
 */
public class SandboxGame {

  /**
   * @param args the command line arguments
   * @throws LWJGLException
   */
  public static void main(String[] args) throws LWJGLException, InterruptedException, URISyntaxException {
    System.setProperty("taiga.code.logging.text", "text");
    System.setProperty("taiga.code.text.localization", "text");
    System.setProperty("java.util.logging.config.file", "logging.properties");
    
    StringManager.loadStringBundle("text");
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
      Logger.getLogger(SandboxGame.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    //set up the data loaders for the game
    //YAMLLoader loader = new YAMLLoader();
    //DataLoader.registerLoader(loader, "yml", "yaml");
    
    //find the mods.
    //ModManager.getInstance().findMods();
    
    //set up launcher
    //launcherInit();
    
//    DisplayMode mode = new DisplayMode(800, 600);
//    
//    GameMap map = new GameMap(new Generator());
//    
//    RenderingSystem.createSystem(mode);
//    RenderingSystem.getInstance().setMap(map);
    
    moving = new Vector3f();
    direction = new Matrix3f();
    
    GameManager game = GameManager.getInstance();
    NetworkManager net = new LoopbackNetwork("network");
    game.addChild(net);
    net.scanRegisteredObjects();
    
    game.start();
  }
  
  private static Vector3f moving;
  private static Matrix3f direction;
  
  public static void launcherInit() {
    JFrame editorframe = new JFrame("Registrar");
    JTabbedPane content = new JTabbedPane();
    editorframe.add(content);
    
    TileRegistryPanel trpanel = new TileRegistryPanel();
    content.add(trpanel);
    
    RenderRegistryPanel rendreg = new RenderRegistryPanel();
    content.add(rendreg);
    
    ModInformationPanel mods = new ModInformationPanel();
    content.add(mods);
    
    editorframe.setSize(800, 400);
    editorframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    editorframe.setVisible(true);
  }
}
