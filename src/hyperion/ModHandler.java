/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperion;

import gpvm.input.InputSystem;
import gpvm.input.KeyListener;
import gpvm.modding.ModController;
import gpvm.render.GraphicsSystem;
import gpvm.util.Updateable;
import gpvm.util.geometry.Quaternion;
import gpvm.util.geometry.Vector;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author russell
 */
public class ModHandler extends ModController {

  @Override
  public void initialize() {
  }

  @Override
  public void load() {
    ViewUpdater updater = new ViewUpdater();
    GraphicsSystem.getInstance().addUpdater(updater);
    InputSystem.getInstance().addKeyListener(Keyboard.KEY_A, updater);
    InputSystem.getInstance().addKeyListener(Keyboard.KEY_D, updater);
    InputSystem.getInstance().addKeyListener(Keyboard.KEY_Q, updater);
    InputSystem.getInstance().addKeyListener(Keyboard.KEY_E, updater);
    InputSystem.getInstance().addKeyListener(Keyboard.KEY_W, updater);
    InputSystem.getInstance().addKeyListener(Keyboard.KEY_S, updater);
  }

  @Override
  public void unload() {
  }

  @Override
  public void destroy() {
  }
  
  private class ViewUpdater implements Updateable, KeyListener {
    private static final float dangle = .05f;
    private static final float speed = .1f;
    
    private Quaternion current;
    private Quaternion up;
    private Quaternion down;
    private Quaternion left;
    private Quaternion right;
    
    private int z;
    private int x;
    private float f;

    public ViewUpdater() {
      current = new Quaternion();
      
      up = Quaternion.rotateY(dangle);
      down = Quaternion.rotateY(-dangle);
      left = Quaternion.rotateZ(-dangle);
      right = Quaternion.rotateZ(dangle);
    }

    @Override
    public void Update() {
      switch(x) {
        case 1:
          current.mult(left, current);
          break;
        case -1:
          current.mult(right, current);
          break;
      }
      
      switch(z) {
        case 1:
          up.mult(current, current);
          break;
        case -1:
          down.mult(current, current);
          break;
      }
      
      Vector dir = current.getDirection();
      GraphicsSystem.getInstance().getCamera().direction = dir;
      
      Vector pos = GraphicsSystem.getInstance().getCamera().position;
      pos.x += dir.x * f;
      pos.y += dir.y * f;
      pos.z += dir.z * f;
    }

    @Override
    public void keyPressed(int code) {
      switch(code) {
        case Keyboard.KEY_A:
          x = 1;
          break;
        case Keyboard.KEY_D:
          x = -1;
          break;
        case Keyboard.KEY_Q:
          z = 1;
          break;
        case Keyboard.KEY_E:
          z = -1;
          break;
        case Keyboard.KEY_W:
          f = speed;
          break;
        case Keyboard.KEY_S:
          f = -speed;
          break;
      }
    }

    @Override
    public void keyReleased(int code) {
      switch(code) {
        case Keyboard.KEY_A:
          x = 0;
          break;
        case Keyboard.KEY_D:
          x = 0;
          break;
        case Keyboard.KEY_Q:
          z = 0;
          break;
        case Keyboard.KEY_E:
          z = 0;
          break;
        case Keyboard.KEY_W:
          f = 0;
          break;
        case Keyboard.KEY_S:
          f = 0;
          break;
      }
    }
  }
}
