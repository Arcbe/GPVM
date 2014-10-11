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

package taiga.gpvm.opengl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import taiga.code.util.Resource;

public class TextureResource implements Resource {
  
  public TextureResource(URL texfile) {
    tfile = texfile;
    
    String path = tfile.getFile();
    type = path.substring(path.lastIndexOf("."));
  }
  
  public void bind() {
    if(texture != null) texture.bind();
  }
  
  @Override
  public void load() {
    try (InputStream input = tfile.openStream()) {
      texture = TextureLoader.getTexture(type, input);
    } catch (IOException ex) {
      log.log(Level.SEVERE, "Unable to load texture from " + tfile, ex);
    }
  }

  @Override
  public void unload() {
    if(texture != null) texture.release();
    texture = null;
  }
  
  private Texture texture;
  private final URL tfile;
  private final String type;

  private static final String locprefix = TextureResource.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
