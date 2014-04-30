/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.td;

import taiga.code.graphics.Renderable;

/**
 *
 * @author russell
 */
public class GameScreen extends Renderable {
  
  public static final String NAME = "gamescreen";
  public static final String PLAYER_NAME = "playership";
  
  public GameScreen() {
    super(NAME);
    
    addChild(new ship(PLAYER_NAME));
  }

  @Override
  protected void updateSelf() {
  }

  @Override
  protected void renderSelf(int pass) {
  }
  
}
