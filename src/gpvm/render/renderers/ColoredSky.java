/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gpvm.render.renderers;

import gpvm.render.SkyBoxRenderer;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author russell
 */
public class ColoredSky implements SkyBoxRenderer {

  public ColoredSky() {
    this.red = 0;
    this.green = 0;
    this.blue = 0;
  }

  public ColoredSky(float red, float green, float blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  @Override
  public void render() {
    GL11.glClearColor(red, green, blue, 1f);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
  }
  
  private final float red;
  private final float green;
  private final float blue;
}
