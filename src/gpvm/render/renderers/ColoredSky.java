///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package gpvm.render.renderers;
//
//import taiga.gpvm.render.SkyBoxRenderer;
//import org.lwjgl.opengl.GL11;
//
///**
// *
// * @author russell
// */
//public class ColoredSky extends SkyBoxRenderer {
//
//  
//  public ColoredSky() {
//    super(NAME);
//    
//    this.red = 0;
//    this.green = 0;
//    this.blue = 0;
//  }
//
//  public ColoredSky(float red, float green, float blue) {
//    super(NAME);
//    
//    this.red = red;
//    this.green = green;
//    this.blue = blue;
//  }
//
//  @Override
//  protected void renderSky() {
//    GL11.glClearColor(red, green, blue, 1f);
//    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
//  }
//  
//  private final float red;
//  private final float green;
//  private final float blue;
//  
//  private static final String NAME = "colored_sky";
//}
