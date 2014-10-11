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

package taiga.code.opengl.shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBGeometryShader4;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL40;
import taiga.code.math.ReadableMatrix4;
import taiga.code.util.Resource;

/**
 * Contains the source code for shader programs and provides methods for
 * loading and using the program in OpenGL.
 * 
 * @author russell
 */
public final class ShaderProgram implements Resource {
  
  public final class Location {
    protected int index;
    
    public final String name;
    public final boolean uniform;
    
    public Location(String name, boolean uni) {
      this.name = name;
      uniform = uni;
    }
    
    public int getIndex() {
      return index;
    }
  }
  
  /**
   * The name for the {@link ShaderProgram} gotten by the {@link #createSimpleColorShader() }
   * method.
   */
  public static final String SHADER_NAME_SIMPLE_COLOR = "colored";
  
  /**
   * Creates a simple {@link ShaderProgram} that draws polygons with per
   * vertex colors.
   * 
   * @return A {@link ShaderProgram} for simple colored polygons.
   */
  public static ShaderProgram createSimpleColorShader() {
    ShaderProgram prog = new ShaderProgram();
    try {
      prog.loadVertexShader(ShaderProgram.class.getResourceAsStream(
        "color.vert"));
      prog.loadFragmentShader(ShaderProgram.class.getResourceAsStream(
        "color.frag"));
      
      prog.load();
    } catch (IOException ex) {
      log.log(Level.SEVERE, SIMPLE_COLOR_EX);
      
      throw new RuntimeException(ex);
    }
    
    return prog;
  }
  
  /**
   * The source code for an OpenGL fragment shader
   */
  public final List<String> fragsrc;
  
  /**
   * The source code for an OpenGL vertex shader.
   */
  public final List<String> vertsrc;
  
  /**
   * The source code for an OpenGL geometry shader.
   */
  public final List<String> geomsrc;

  /**
   * Creates a new {@link ShaderProgram} without any source code attached.
   */
  public ShaderProgram() {
    locations = new HashMap<>();
    
    this.fragsrc = new ArrayList<>();
    this.vertsrc = new ArrayList<>();
    this.geomsrc = new ArrayList<>();
    
    shaderprog = -1;
  }
  
  /**
   * Loads source code from the given {@link InputStream} into the 
   * fragment shader of this {@link ShaderProgram}.
   * 
   * @param in An {@link InputStream} where the source code can be read.
   * @throws IOException Thrown if there is a problem extracting data from the
   * {@link InputStream}.
   */
  public void loadFragmentShader(InputStream in) throws IOException {
    loadSource(in, fragsrc);
  }

  /**
   * Loads source code from the given {@link InputStream} into the 
   * vertex shader of this {@link ShaderProgram}.
   * 
   * @param in An {@link InputStream} where the source code can be read.
   * @throws IOException Thrown if there is a problem extracting data from the
   * {@link InputStream}.
   */
  public void loadVertexShader(InputStream in) throws IOException {
    loadSource(in, vertsrc);
  }
  
  /**
   * Loads source code from the given {@link InputStream} into the 
   * geometry shader of this {@link ShaderProgram}.
   * 
   * @param in An {@link InputStream} where the source code can be read.
   * @throws IOException Thrown if there is a problem extracting data from the
   * {@link InputStream}.
   */
  public void loadGeometryShader(InputStream in) throws IOException {
    loadSource(in, geomsrc);
  }
  
  public void setUniformMatrix(Location loc, ReadableMatrix4 mat) {
    setUniformMatrix(loc.index, mat);
  }
  
  public void setUniformMatrix(int loc, ReadableMatrix4 mat) {
    FloatBuffer buf = matbuffer.get();
    
    buf.rewind();
    mat.store(buf);
    buf.flip();
    
    ARBShaderObjects.glUniformMatrix4ARB(loc, true, buf);
  }
  
  /**
   * Returns the index for a given uniform in this {@link ShaderProgram}.
   * The program must be loaded before this method is called.
   * 
   * @param name The name of the uniform to look up.
   * @return The index for the given uniform, or -1 if there was a problem.
   */
  public Location getUniformLocation(String name) {
    Location output = new Location(name, true);
    output.index =  ARBShaderObjects.glGetUniformLocationARB(shaderprog, name);
    locations.put(name, output);
    
    return output;
  }
  
  /**
   * Returns the index for a given attribute in this {@link ShaderProgram}.
   * Th program must be loaded before this method is called.
   * 
   * @param name The name of the attribute to look up.
   * @return The index of the attribute , or -1 if there was a problem.
   */
  public Location getAttributeLocation(String name) {
    Location output = new Location(name, false);
    output.index =  ARBVertexShader.glGetAttribLocationARB(shaderprog, name);
    locations.put(name, output);
    
    return output;
  }
  
  /**
   * Returns the handle for this {@link ShaderProgram}.  If there was a
   * problem loading this {@link ShaderProgram} then this will return 0, and
   * if it has not yet been loaded then this method will return -1.
   * @return 
   */
  public int getProgramHandle() {
    return shaderprog;
  }
  
  /**
   * Binds this {@link ShaderProgram} to the OpenGL context for use in rendering.
   */
  public void bind() {
    if(shaderprog > 0) {
      ARBShaderObjects.glUseProgramObjectARB(shaderprog);
    }
  }
  
  @Override
  public void load() {
    try {
      shaderprog = ARBShaderObjects.glCreateProgramObjectARB();

      if(!fragsrc.isEmpty()) {
        String[] src = new String[fragsrc.size()];
        fragsrc.toArray(src);

        attachShader(shaderprog, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB, src);
      }

      if(!vertsrc.isEmpty()) {
        String[] src = new String[vertsrc.size()];
        vertsrc.toArray(src);

        attachShader(shaderprog, ARBVertexShader.GL_VERTEX_SHADER_ARB, src);
      }

      if(!geomsrc.isEmpty()) {
        String[] src = new String[geomsrc.size()];
        geomsrc.toArray(src);

        attachShader(shaderprog, ARBGeometryShader4.GL_GEOMETRY_SHADER_ARB, src);
      }

      ARBShaderObjects.glLinkProgramARB(shaderprog);
      if(ARBShaderObjects.glGetObjectParameteriARB(shaderprog, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
        throw new RuntimeException(getLog(shaderprog));
      }

      ARBShaderObjects.glValidateProgramARB(shaderprog);
      if(ARBShaderObjects.glGetObjectParameteriARB(shaderprog, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
        throw new RuntimeException(getLog(shaderprog));
      }
    } catch(RuntimeException ex) {
      ARBShaderObjects.glDeleteObjectARB(shaderprog);
      
      throw ex;
    }
    
    for(Location loc : locations.values()) {
      if(loc.uniform)
        loc.index = ARBShaderObjects.glGetUniformLocationARB(shaderprog, loc.name);
      else
        loc.index = ARBVertexShader.glGetAttribLocationARB(shaderprog, loc.name);
    }
  }

  @Override
  public void unload() {
    ARBShaderObjects.glDeleteObjectARB(shaderprog);
    shaderprog = -1;
  }
  
  private int shaderprog;
  private Map<String, Location> locations;
  
  private void loadSource(InputStream in, List<String> dest) throws IOException {
    InputStreamReader ireader = new InputStreamReader(in);
    BufferedReader reader = new BufferedReader(ireader);
    
    String line;
    while((line = reader.readLine()) != null)
      dest.add(line + "\n");
  }
  
  private int attachShader(int prog, int type, String[] src) {
    int shader = ARBShaderObjects.glCreateShaderObjectARB(type);
    
    ARBShaderObjects.glShaderSourceARB(shader, src);
    ARBShaderObjects.glCompileShaderARB(shader);
    
    if(ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {
      throw new RuntimeException(getLog(shader));
    }
    
    ARBShaderObjects.glAttachObjectARB(prog, shader);
    
    return shader;
  }
  
  private String getLog(int obj) {
    int len = ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB);
    
    return ARBShaderObjects.glGetInfoLogARB(obj, len);
  }
  
  private static final ThreadLocal<FloatBuffer> matbuffer = new ThreadLocal<FloatBuffer>() {
    @Override
    protected FloatBuffer initialValue() {
      return BufferUtils.createFloatBuffer(16);
    }
  };

  private static final String locprefix = ShaderProgram.class.getName().toLowerCase();
  
  private static final String SIMPLE_COLOR_EX = locprefix + ".simple_color_ex";

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
