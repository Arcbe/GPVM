/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.screens;

import taiga.gpvm.HardcodedValues;
import taiga.code.graphics.RenderableSwitcher;

/**
 *
 * @author russell
 */
public class GameScreen extends RenderableSwitcher {

  public GameScreen() {
    super(HardcodedValues.GAME_SCREEN_NAME);
  }

  @Override
  protected void updateSelf() {
  }

  @Override
  protected void renderSelf(int pass) {
  }
}
