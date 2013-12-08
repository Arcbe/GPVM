/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandboxgame;

import gpvm.editor.panels.ModInformationPanel;
import gpvm.editor.panels.RenderRegistryPanel;
import gpvm.editor.panels.TileRegistryPanel;
import gpvm.input.InputSystem;
import gpvm.input.KeyListener;
import gpvm.io.DataLoader;
import gpvm.io.YAMLLoader;
import java.awt.Color;
import java.util.ArrayList;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import gpvm.map.Region;
import gpvm.modding.ModManager;
import gpvm.render.Camera;
import gpvm.render.RawBatch;
import gpvm.render.RenderingSystem;
import gpvm.render.TileInfo;
import gpvm.render.VertexArrayBatch;
import gpvm.render.renderers.ColorInfo;
import gpvm.render.renderers.ColorRenderer;
import gpvm.render.vertices.ColorVertex;
import gpvm.util.StringManager;
import gpvm.util.geometry.Coordinate;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;

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
    StringManager.loadStringBundle("text");
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
      Logger.getLogger(SandboxGame.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    //set up the data loaders for the game
    YAMLLoader loader = new YAMLLoader();
    DataLoader.registerLoader(loader, "yml", "yaml");
    
    //find the mods.
    ModManager.getInstance().findMods();
    
    //set up launcher
    launcherInit();
    
//    DisplayMode mode = new DisplayMode(800, 600);
//    
//    GameMap map = new GameMap(new Generator());
//    
//    RenderingSystem.createSystem(mode);
//    RenderingSystem.getInstance().setMap(map);
    
    moving = new Vector3f();
    direction = new Matrix3f();
    
    KeyListener list = new KeyListener() {

      @Override
      public void keyPressed(int code) {
        if(RenderingSystem.getInstance() == null) return;
        
        Vector3f vec = RenderingSystem.getInstance().getCamera().position;
        
        switch(code) {
          case Keyboard.KEY_W: vec.y = .1f; break;
          case Keyboard.KEY_A: vec.x = -.1f; break;
          case Keyboard.KEY_S: vec.y = -.1f; break;
          case Keyboard.KEY_D: vec.x = .1f; break;
          case Keyboard.KEY_Q: vec.z = .1f; break;
          case Keyboard.KEY_E: vec.z = -.1f; break;
        }
      }

      @Override
      public void keyReleased(int code) {
        
      }
    };
    
    InputSystem.getInstance().addKeyListener(Keyboard.KEY_W, list);
    InputSystem.getInstance().addKeyListener(Keyboard.KEY_A, list);
    InputSystem.getInstance().addKeyListener(Keyboard.KEY_S, list);
    InputSystem.getInstance().addKeyListener(Keyboard.KEY_D, list);
    InputSystem.getInstance().addKeyListener(Keyboard.KEY_Q, list);
    InputSystem.getInstance().addKeyListener(Keyboard.KEY_E, list);
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
