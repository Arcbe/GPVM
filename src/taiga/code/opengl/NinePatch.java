/*
 * Copyright (C) 2014 Russell Smith.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package taiga.code.opengl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import taiga.code.util.DataNode;

public final class NinePatch implements Drawable {
  
  public static final String FIELD_NAME_TEXTURE = "image";
  public static final String FIELD_NAME_BORDER = "border";
  public static final String FIELD_NAME_TOP = "top";
  public static final String FIELD_NAME_BOTTOM = "bottom";
  public static final String FIELD_NAME_LEFT = "left";
  public static final String FIELD_NAME_RIGHT = "right";

  /**
   * Creates an empty {@link NinePatch}.  A {@link Texture} will have to be
   * set before this {@link NinePatch} can be rendered with the method {@link #setImage(org.newdawn.slick.opengl.Texture, int, int, int, int) }
   */
  public NinePatch() {
    initBuffers();
  }
  
  /**
   * Creates a {@link NinePatch} using the given {@link Texture} and with
   * uniform sizes for the outer sections.
   * 
   * @param tex THe {@link Texture} to use.
   * @param border The size of the outer sections in pixels.
   */
  public NinePatch(Texture tex, int border) {
    initBuffers();
    
    setImage(tex, border);
  }
  
  /**
   * Creates a new {@link DataNode} with the parameters derived from the
   * given {@link DataNode}.
   * 
   * @param data The {@link DataNode} to construct the {@link NinePatch} with.
   */
  public NinePatch(DataNode data) {
    String texname = data.getValueByName(FIELD_NAME_TEXTURE);
    if(texname == null) return;
    
    File tfile;
    try {
      tfile = new File(getClass().getClassLoader().getResource(texname).toURI());
    } catch (URISyntaxException ex) {
      log.log(Level.SEVERE, null, ex);
      return;
    }
    
    int t = 0;
    int b = 0;
    int l = 0;
    int r = 0;
    
    Integer temp;
    if((temp = data.getValueByName(FIELD_NAME_BORDER)) != null) {
      t = temp;
      b = temp;
      l = temp;
      r = temp;
    }
    
    if((temp = data.getValueByName(FIELD_NAME_TOP)) != null) t = temp;
    if((temp = data.getValueByName(FIELD_NAME_BOTTOM)) != null) b = temp;
    if((temp = data.getValueByName(FIELD_NAME_LEFT)) != null) l = temp;
    if((temp = data.getValueByName(FIELD_NAME_RIGHT)) != null) r = temp;
    
    setImage(tfile, l, r, t, b);
  }
  
  /**
   * Creates a {@link NinePatch} using the given {@link Texture} and with
   * the given sizes for the sections.
   * 
   * @param tex The {@link Texture} to use for this {@link NinePatch}.
   * @param left The width in pixels of the left three sections.
   * @param right The width in pixels of the right three sections.
   * @param top The height in pixels of the top three sections.
   * @param bottom The height in pixels of the bottom three sections.
   */
  public NinePatch(Texture tex, int left, int right, int top, int bottom) {
    initBuffers();
    
    setImage(tex, left, right, top, bottom);
  }
  
  /**
   * Sets the {@link Texture} to use for this {@link NinePatch} and sets
   * the sections to be a uniform border of the given size.
   * 
   * @param tex The {@link Texture} to use.
   * @param border The size of the outer sections in pixels.
   */
  public void setImage(Texture tex, int border) {
    setImage(tex, border, border, border, border);
  }
  
  /**
   * Sets the {@link Texture} to use for this {@link NinePatch} along with
   * parameters for how to divide the {@link Texture} into its nine sections
   * 
   * @param tex The {@link Texture} to use for this {@link NinePatch}.
   * @param left The width in pixels of the left three sections.
   * @param right The width in pixels of the right three sections.
   * @param top The height in pixels of the top three sections.
   * @param bottom The height in pixels of the bottom three sections.
   */
  public void setImage(Texture tex, int left, int right, int top, int bottom) {
    this.texture = tex;
    this.left = left;
    this.right = left;
    this.top = left;
    this.bottom = left;
    
    createTexCoords();
  }
  
  /**
   * Sets the {@link File} that the {@link Texture} for this {@link NinePatch}.
   * The actual {@link Texture} will not be loaded until the {@link #load() } method
   * is called.
   * 
   * @param imgfile The {@link File} with the {@link Texture} to load.
   * @param left The width in pixels of the left three sections.
   * @param right The width in pixels of the right three sections.
   * @param top The height in pixels of the top three sections.
   * @param bottom The height in pixels of the bottom three sections.
   */
  public void setImage(File imgfile, int left, int right, int top, int bottom) {
    this.top = top;
    this.left = left;
    this.bottom = bottom;
    this.right = right;
    
    this.texfile = imgfile;
  }

  @Override
  public void load() {
    if(texture != null) {
      texture.release();
    }
    
    if(texfile == null || !texfile.canRead()) {
      log.log(Level.SEVERE, BAD_TEX_FILE);
      return;
    }
    
    String filename = texfile.getName();
    try {
      texture = TextureLoader.getTexture(filename.substring(filename.lastIndexOf('.')), new FileInputStream(texfile));
    } catch (IOException ex) {
      //this should never happen probably
      log.log(Level.SEVERE, null, ex);
    }
    
    createTexCoords();
  }

  @Override
  public void unload() {
    texture.release();
    texture = null;
  }
  
  /**
   * Returns the height of the top sections of this {@link NinePatch}.
   * 
   * @return The height of the top sections.
   */
  public int top() {
    return top;
  }
  
  /**
   * The minimum height that this {@link NinePatch} should be drawn with.
   * 
   * @return The minimum height.
   */
  public int minHeight() {
    return top + bottom;
  }
  
  /**
   * The minimum width that this {@link NinePatch} should be drawn with.
   * 
   * @return The minimum width.
   */
  public int minWidth() {
    return left + right;
  }
  
  @Override
  public int getWidth() {
    return minWidth();
  }
  
  @Override
  public int getHeight() {
    return minHeight();
  }
  
  /**
   * Returns the width of the bottom sections of this {@link NinePatch}.
   * 
   * @return The width of the bottom sections.
   */
  public int bottom() {
    return bottom;
  }
  
  /**
   * Returns the height of the left sections of this {@link NinePatch}.
   * 
   * @return The height of the left sections.
   */
  public int left() {
    return left;
  }
  
  /**
   * Returns the width of the right sections of this {@link NinePatch}.
   * 
   * @return The width of the right sections.
   */
  public int right() {
    return right;
  }
  
  /**
   * Draws this {@link NinePatch} in a rectangle of the given size at the
   * origin.  The {@link NinePatch} will expand to fill the entire area, but the
   * behavior for a rectangle smaller than the {@link NinePatch} is undefined.
   * 
   * @param w The width of the drawing area.
   * @param h The height of the drawing area.
   */
  public void draw(int w, int h) {
    draw(0, 0, w, h);
  }
  
  /**
   * Draws this {@link NinePatch} in the given rectangle.  The {@link NinePatch}
   * will expand to fill the entire area, but the behavior for a rectangle smaller
   * than the {@link NinePatch} is undefined.
   * 
   * @param x The x coordinate to begin drawing at.
   * @param y The y coordinate to begin drawing at.
   * @param w The width of the drawing area.
   * @param h The height of the drawing area.
   */
  public void draw(int x, int y, int w, int h) {
    if(texture == null) return;
    
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
    GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    
    texture.bind();
    
    setCoords(x, y, w, h);
    
    GL11.glVertexPointer(2, 0, coordinates);
    GL11.glTexCoordPointer(2, 0, texcoords);
    
    GL11.glDrawElements(GL11.GL_QUADS, indices);
  }
  
  
  private File texfile;
  private Texture texture;
  private int left;
  private int right;
  private int top;
  private int bottom;
  
  private IntBuffer indices;
  private FloatBuffer texcoords;
  private IntBuffer coordinates;
  
  private void initBuffers() {
    //nine sections with 4 2d vertices each.
    indices = BufferUtils.createIntBuffer(72);
    texcoords = BufferUtils.createFloatBuffer(32);
    coordinates = BufferUtils.createIntBuffer(32);
    
    for(int i = 0; i < 3; i++) {
      for(int j = 0; j < 3; j++) {
        indices.put(j + i * 4); //top left corner
        indices.put(j + 1 + i * 4); //top right corner
        indices.put(j + 1 + (i+ 1) * 4); //bottom right corner
        indices.put(j + (i + 1) * 4);
      }
    }
    
    indices.flip();
  }
  
  private void createTexCoords() {
    for(int i = 0; i < 4; i++) {
      
      float x = 0;
      switch(i) {
        case 1:
          x = left / (float) texture.getImageWidth();
          break;
        case 2:
          x = 1f - right / (float) texture.getImageWidth();
          break;
        case 3:
          x = 1;
          break;
      }
      
      for(int j = 0; j < 4; j++) {
        
        float y = 0;
        switch(j) {
          case 1:
            y = (float) top / (float) texture.getImageHeight();
            break;
          case 2:
            y = 1f - (float) bottom / (float) texture.getImageHeight();
            break;
          case 3:
            y = 1f;
            break;
        }
        
        texcoords.put(x);
        texcoords.put(y);
      }
    }
    
    texcoords.flip();
  }
  
  private void setCoords(int x, int y, int w, int h) {
    for(int i = 0; i < 4; i++) {
      for(int j = 0; j < 4; j++) {
        switch(i) {
          case 0: coordinates.put(x); break;
          case 1: coordinates.put(x + left); break;
          case 2: coordinates.put(x + w - right); break;
          case 3: coordinates.put(x + w); break;
        }
        
        switch(j) {
          case 0: coordinates.put(y); break;
          case 1: coordinates.put(y + bottom); break;
          case 2: coordinates.put(y + h - top); break;
          case 3: coordinates.put(y + h); break;
        }
      }
    }
    
    coordinates.flip();
  }

  private static final String locprefix = NinePatch.class.getName().toLowerCase();
  
  private static final String BAD_TEX_FILE = locprefix + ".bad_tex_file";

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
