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

import java.util.logging.Logger;
import taiga.code.math.Matrix4;

/**
 * As the root of a scene graph this {@link SceneRoot} contains a {@link
 * Camera} that it uses to override the projection and view matrices for
 * any children {@link Renderable}s.
 * 
 * @author Russell Smith
 */
public abstract class SceneRoot extends Renderable {

  public Camera cam;
  
  public SceneRoot(String name) {
    super(name);
  }
  
  public SceneRoot(String name, Camera camera) {
    super(name);
    
    cam = camera;
  }

  @Override
  protected Matrix4 processProjection(Matrix4 proj, int pass) {
    if(cam == null) return super.processProjection(proj, pass);
    else return cam.getProjection();
  }

  @Override
  protected void updateRenderable() {
    Matrix4 view;
    if(cam != null && (view = cam.getViewMatrix()) != getLocalTransform())
      setLocalTransformation(view);
  }

  private static final String locprefix = SceneRoot.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
