/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;
import taiga.code.interpolation.ColorInterpolator;
import taiga.code.util.ByteUtils;
import taiga.code.util.DataNode;

public final class ColoredDrawable implements Drawable {
  
  public static final String FIELD_NAME_COLORS = "colors";
  public static final String FIELD_NAME_COLOR = "color";
  public static final String FIELD_NAME_HOR_GRAD = "horizontal-gradient";
  public static final String FIELD_NAME_VERT_GRAD = "vertical-gradient";
  public static final String FIELD_NAME_CENT_GRAD = "center-gradient";
  
  public enum GradientType {
    vertical,
    horizontal,
    center
  }

  public ColoredDrawable() {
    colors = BufferUtils.createByteBuffer(9 * 4);
  }

  public ColoredDrawable(ReadableColor color) {
    this.colors = BufferUtils.createByteBuffer(9 * 4);
    
    setColor(color);
  }
  
  public ColoredDrawable(ReadableColor color1, ReadableColor color2, GradientType type) {
    this.colors = BufferUtils.createByteBuffer(9 * 4);
    
    setGradient(color1, color2, type);
  }

  public ColoredDrawable(DataNode data) {
    this.colors = BufferUtils.createByteBuffer(9 * 4);
    
    DataNode val;
    Number col;
    if((val = (DataNode) data.getValueByName(FIELD_NAME_COLORS)) != null) {
      processColors(val);
    } else if((val = data.getValueByName(FIELD_NAME_VERT_GRAD)) != null) {
      processGradient(val, GradientType.vertical);
    } else if((val = data.getValueByName(FIELD_NAME_HOR_GRAD)) != null) {
      processGradient(val, GradientType.horizontal);
    } else if((val = data.getValueByName(FIELD_NAME_CENT_GRAD)) != null) {
      processGradient(val, GradientType.center);
    } else if((col = data.getValueByName(FIELD_NAME_COLOR)) != null) {
      setColor(processColorData(col.intValue()));
    }
  }
  
  public void setColor(ReadableColor color) {
    for(int i = 0; i < 9; i++) {
      color.writeARGB(colors);
    }
    
    colors.flip();
  }
  
  public void setGradient(ReadableColor color1, ReadableColor color2, GradientType type) {
    switch(type) {
      case vertical:
        setVerticalGradient(color1, color2);
        break;
      case horizontal:
        setHorizontalGradient(color1, color2);
        break;
      case center:
        setCentralGradient(color1, color2);
        break;
    }
  }
  
  public void setVerticalGradient(ReadableColor col1, ReadableColor col2) {
    ReadableColor[] cols = new ReadableColor[9];
    
    cols[0] = col1;
    cols[1] = col1;
    cols[2] = col1;
    
    cols[6] = col2;
    cols[7] = col2;
    cols[8] = col2;
    
    Color inter = ColorInterpolator.interpolate(col1, col2, .5f);
    cols[3] = inter;
    cols[4] = inter;
    cols[5] = inter;
    
    setColors(cols);
  }
  
  public void setCentralGradient(ReadableColor col1, ReadableColor col2) {
    ReadableColor[] cols = new ReadableColor[9];
    
    cols[4] = col1;
    
    cols[0] = col2;
    cols[2] = col2;
    cols[6] = col2;
    cols[8] = col2;
    
    // ~1/sqrt(2), the factor to adjust the colors by to produce a circular pattern.
    Color temp = ColorInterpolator.interpolate(col1, col2, .707107f);
    
    cols[1] = temp;
    cols[3] = temp;
    cols[5] = temp;
    cols[7] = temp;
    
    setColors(cols);
  }
  
  public void setHorizontalGradient(ReadableColor col1, ReadableColor col2) {
    ReadableColor[] cols = new ReadableColor[9];
    
    cols[0] = col1;
    cols[3] = col1;
    cols[6] = col1;
    
    cols[1] = col2;
    cols[4] = col2;
    cols[7] = col2;
    
    Color inter = ColorInterpolator.interpolate(col1, col2, .5f);
    cols[2] = inter;
    cols[5] = inter;
    cols[8] = inter;
    
    setColors(cols);
  }
  
  public void setColors(ReadableColor[] cols) {
    if(cols.length != 9) {
      throw new IllegalArgumentException();
    }
    
    for(ReadableColor c : cols) {
      c.writeARGB(colors);
    }
  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public void draw(int x, int y, int w, int h) {
    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
    GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
    
    GL11.glVertexPointer(2, 0, createVertices(x, y, w, h));
    GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 0, colors);
    
    GL11.glDrawElements(GL11.GL_QUADS, indices);
  }

  @Override
  public void load() {
  }

  @Override
  public void unload() {
  }
  
  private ByteBuffer colors;
  
  private IntBuffer createVertices(int x, int y, int w, int h) {
    IntBuffer verts = BufferUtils.createIntBuffer(18);
    
    verts.put(x).put(y + h);
    verts.put(x + w / 2).put(y + h);
    verts.put(x + w).put(y + h);
    verts.put(x).put(y + h / 2);
    verts.put(x + w / 2).put(y + h / 2);
    verts.put(x + w).put(y + h / 2);
    verts.put(x).put(y);
    verts.put(x + w / 2).put(y);
    verts.put(x + w).put(y);
    verts.flip();
    
    return verts;
  }
  
  private static final IntBuffer indices;
  
  private Color processColorData(int col) {
    byte[] bytes = ByteUtils.toBytes(col);
    return new Color(bytes[1], bytes[2], bytes[3], bytes[0]);
  }
  
  private void processColors(DataNode data) {
    Color[] cols = new Color[9];
    
    List<Integer> colors = (List<Integer>) data.data;
    for(int i = 0; i < 9; i++)
      cols[i] = processColorData(colors.get(i));
    
    setColors(cols);
  }
  
  private void processGradient(DataNode data, GradientType type) {
    List<Integer> cols = (List<Integer>) data.data;
    
    Color color1 = processColorData(cols.get(0));
    Color color2 = processColorData(cols.get(1));
    
    setGradient(color1, color2, type);
  }
  
  static {
    indices = BufferUtils.createIntBuffer(16);
    
    indices.put(0).put(1).put(4).put(3);
    indices.put(1).put(2).put(5).put(4);
    indices.put(3).put(4).put(7).put(6);
    indices.put(4).put(5).put(8).put(7);
    indices.flip();
  }

  private static final String locprefix = ColoredDrawable.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
