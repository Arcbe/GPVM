/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandboxgame;

import hyperion.Generator;
import gpvm.Registrar;
import gpvm.ThreadingManager;
import gpvm.editor.panels.ModInformationPanel;
import gpvm.editor.panels.RenderRegistryPanel;
import gpvm.editor.panels.TileRegistryPanel;
import gpvm.input.InputSystem;
import gpvm.input.KeyListener;
import gpvm.io.DataLoader;
import gpvm.io.YAMLLoader;
import gpvm.map.GameMap;
import java.awt.Color;
import java.util.ArrayList;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import gpvm.map.Region;
import gpvm.map.TileDefinition;
import gpvm.modding.ModManager;
import gpvm.render.Camera;
import gpvm.render.RawBatch;
import gpvm.render.RenderRegistry;
import gpvm.render.RenderingSystem;
import gpvm.render.TileInfo;
import gpvm.render.VertexArrayBatch;
import gpvm.render.renderers.ColorInfo;
import gpvm.render.renderers.ColorRenderer;
import gpvm.render.vertices.ColorVertex;
import gpvm.util.Settings;
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
    Settings.loadStringBundle("text");
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
        switch(code) {
          case Keyboard.KEY_W: moving.y = .1f; break;
          case Keyboard.KEY_A: moving.x = -.1f; break;
          case Keyboard.KEY_S: moving.y = -.1f; break;
          case Keyboard.KEY_D: moving.x = .1f; break;
          case Keyboard.KEY_Q: moving.z = .1f; break;
          case Keyboard.KEY_E: moving.z = -.1f; break;
        }
      }

      @Override
      public void keyReleased(int code) {
        switch(code) {
          case Keyboard.KEY_W: moving.y = 0; break;
          case Keyboard.KEY_A: moving.x = 0; break;
          case Keyboard.KEY_S: moving.y = 0; break;
          case Keyboard.KEY_D: moving.x = 0; break;
          case Keyboard.KEY_Q: moving.z = 0; break;
          case Keyboard.KEY_E: moving.z = 0; break;
        }
      }
    };
    
    InputSystem.getInstance().addKeyListener(Keyboard.KEY_W, list);
    InputSystem.getInstance().addKeyListener(Keyboard.KEY_A, list);
    InputSystem.getInstance().addKeyListener(Keyboard.KEY_S, list);
    InputSystem.getInstance().addKeyListener(Keyboard.KEY_D, list);
    InputSystem.getInstance().addKeyListener(Keyboard.KEY_Q, list);
    InputSystem.getInstance().addKeyListener(Keyboard.KEY_E, list);
    
    
    while(RenderingSystem.getInstance() == null || RenderingSystem.getInstance().isRunning()) {
      Camera cam = RenderingSystem.getInstance().getCamera();
      if(cam != null) {
        cam.position.z += moving.z;
        cam.position.y += moving.y;
        cam.position.x += moving.x;
      }
      
      Thread.sleep(10);
    }
    
    System.exit(0);
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
    
  /**
   *
   */
  public static void stuff() {
    
    
    //create the drawing batch
    RawBatch bat = new RawBatch();
    
    bat.rendermode = GL11.GL_LINES;
    
    //draw two faces
    ArrayList<ColorVertex> vertices = new ArrayList<>();
   
    for(int i = 0; i <= Region.REGION_SIZE; i++) {
      //bottom
      vertices.add(new ColorVertex(i, 0, 0, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(i, Region.REGION_SIZE, 0, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(0, i, 0, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(Region.REGION_SIZE, i, 0, Color.GREEN.getRGB()));
      
      //top
      vertices.add(new ColorVertex(i, 0, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(i, Region.REGION_SIZE, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(0, i, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(Region.REGION_SIZE, i, Region.REGION_SIZE, Color.GREEN.getRGB()));
      
      //front
      vertices.add(new ColorVertex(i, 0, 0, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(i, 0, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(0, 0, i, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(Region.REGION_SIZE, 0, i, Color.GREEN.getRGB()));
      
      //back
      vertices.add(new ColorVertex(i, Region.REGION_SIZE, 0, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(i, Region.REGION_SIZE, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(0, Region.REGION_SIZE, i, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(Region.REGION_SIZE, Region.REGION_SIZE, i, Color.GREEN.getRGB()));
      
      //left
      vertices.add(new ColorVertex(0, i, 0, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(0, i, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(0, 0, i, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(0, Region.REGION_SIZE, i, Color.GREEN.getRGB()));
      
      //right
      vertices.add(new ColorVertex(Region.REGION_SIZE, i, 0, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(Region.REGION_SIZE, i, Region.REGION_SIZE, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(Region.REGION_SIZE, 0, i, Color.GREEN.getRGB()));
      vertices.add(new ColorVertex(Region.REGION_SIZE, Region.REGION_SIZE, i, Color.GREEN.getRGB()));
    }
    
    bat.vertices = new ColorVertex[vertices.size()];
    vertices.toArray(bat.vertices);
    VertexArrayBatch renderer = new VertexArrayBatch();
    renderer.compile(bat);
    
    //create some tiles
    TileInfo info = new TileInfo();
    info.relativepos = new Coordinate(1, 1, 1);
    info.info = new ColorInfo();
    ((ColorInfo)info.info).color = Color.LIGHT_GRAY.getRGB();
    
    ArrayList<TileInfo> tiles = new ArrayList<TileInfo>();
    tiles.add(info);
    ColorRenderer cr = new ColorRenderer();
    VertexArrayBatch rend2 = new VertexArrayBatch();
    //rend2.compile(cr.batchTiles(tiles)[0]);
    
    //Display.setDisplayMode(new DisplayMode(800, 600));
    //Display.create();
    
    
    float x = 0;
    float y = 0;
    float z = 0;
    float delta = .005f;
    while(!Display.isCloseRequested()) {
      if(Keyboard.isKeyDown(Keyboard.KEY_Q)) x += delta;
      if(Keyboard.isKeyDown(Keyboard.KEY_A)) x -= delta;
      if(Keyboard.isKeyDown(Keyboard.KEY_W)) y += delta;
      if(Keyboard.isKeyDown(Keyboard.KEY_S)) y -= delta;
      if(Keyboard.isKeyDown(Keyboard.KEY_E)) z += delta;
      if(Keyboard.isKeyDown(Keyboard.KEY_D)) z -= delta;
      
      GL11.glClearColor(1, 0, 1, 1);
      GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
      
      GL11.glMatrixMode(GL11.GL_PROJECTION);
      GL11.glLoadIdentity();
      GLU.gluPerspective(60, (float) Display.getWidth() / (float) Display.getHeight(), 1, 10000);
      GL11.glMatrixMode(GL11.GL_MODELVIEW);
      GL11.glLoadIdentity();
      GLU.gluLookAt(x, y, z, 8, 8, 8, 0, 0, 1);
      
      renderer.draw();
      rend2.draw();
      
      Display.update();
    }
  }
}
