
import taiga.code.registration.RegisteredSystem;
import taiga.td.GameScreen;
import taiga.td.Graphics;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author russell
 */
public class GameMain extends RegisteredSystem {
  public static String NAME = "td";
  
  public static void main(String[] arg) {
    GameMain game = new GameMain();
    
    game.start();
  }

  public GameMain() {
    super(NAME);
    
    Graphics graphics = new Graphics();
    addChild(graphics);
    
    graphics.addChild(new GameScreen());
  }

  @Override
  protected void startSystem() {
  }

  @Override
  protected void stopSystem() {
  }

  @Override
  protected void resetObject() {
  }
}
