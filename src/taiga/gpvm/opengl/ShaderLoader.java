/*
 * Copyright (C) 2014 Russell Smith.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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

package taiga.gpvm.opengl;

import java.util.logging.Logger;
import taiga.code.opengl.shaders.ShaderProgram;
import taiga.code.util.Resource;

public class ShaderLoader implements ResourceLoader {

  @Override
  public Resource load(String resname) {
    switch(resname) {
      case ShaderProgram.SHADER_NAME_SIMPLE_COLOR:
        return ShaderProgram.createSimpleColorShader();
    }
    
    return null;
  }

  private static final String locprefix = ShaderLoader.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
