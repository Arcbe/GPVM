package taiga.gpvm.render;

import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import taiga.code.io.DataNode;

/**
 * A {@link SkyBoxRenderer} that renders a simple sky of a solid color.
 * 
 * @author russell
 */
public class ColoredSky extends SkyBoxRenderer {
  
  public static final String COLOR_FIELD_NAME = "color";
  public static final String COLORED_SKY_NAME = "colored-sky";

  /**
   * Creates a new {@link ColoredSky} that renders a simple black sky.
   */
  public ColoredSky() {
    super(COLORED_SKY_NAME);
  }

  /**
   * Creates a new {@link ColoredSky} using the data from the given {@link DataNode}.
   * 
   * @param data The data to use for this {@link ColoredSky}.
   */
  public ColoredSky(DataNode data) {
    super(COLORED_SKY_NAME);
    
    data = data.getObject(COLOR_FIELD_NAME);
    
    if(data == null)
      setColor(0,0,0);
    else
      setColor(((Number) data.data).intValue());
  }
  
  /**
   * Sets the color for the sky that this {@link ColoredSky} will render.  The
   * color should be encoded as an integer with the RGB format.
   * @param color 
   */
  public void setColor(int color) {
    byte temp = (byte) color;
    float r = (float)temp / 255f;
    
    temp = (byte) (color >> 8);
    float g = (float)temp / 255f;
    
    temp = (byte) (color >> 16);
    float b = (float)temp / 255f;
    
    setColor(r, g, b);
  }
  
  /**
   * Sets the color for the sky that this {@link ColoredSky} will render.
   * @param r
   * @param g
   * @param b 
   */
  public void setColor(float r, float g, float b) {
    red = r;
    green = g;
    blue = b;
  }

  @Override
  protected void renderSky() {
    GL11.glClearColor(red, green, blue, 1f);
    GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
  }
  
  private float red;
  private float green;
  private float blue;

  private static final String locprefix = ColoredSky.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
