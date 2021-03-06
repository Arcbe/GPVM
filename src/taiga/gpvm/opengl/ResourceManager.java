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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import taiga.code.opengl.GraphicsSystem;
import taiga.code.opengl.WindowListener;
import taiga.code.registration.NamedObject;
import taiga.code.registration.ReusableObject;
import taiga.code.util.Resource;

public class ResourceManager extends ReusableObject implements WindowListener {

  public ResourceManager(String name) {
    super(name);
    
    cache = new HashMap<>();
    loaders = new HashMap<>();
  }
  
  /**
   * Adds a new {@link ResourceLoader} to this {@link ResourceManager} that will
   * be used to load resources of the given {@link Class}.  If there is already
   * a {@link ResourceLoader} for the given {@link Class} then this will replace
   * it.
   * 
   * @param loader The {@link ResourceLoader} to add.
   * @param type The {@link Class} of {@link Resource}sthat it will load.
   */
  public void addLoader(ResourceLoader loader, Class<? extends Resource> type) {
    loaders.put(type, loader);
  }
  
  /**
   * Returns the {@link Resource} for the given name.  The meaning of the name is
   * dependent on the loader that is used.  This method will also try to reuse
   * resources if a {@link Resource} with the given name has already been loaded,
   * but there are no guarantee.
   * 
   * @param <T>
   * @param name
   * @param type
   * @return 
   */
  public <T extends Resource> T getResource(String name, Class<T> type) {
    if(cache.containsKey(name)) {
      return (T) cache.get(name);
    }
    
    ResourceLoader loader = loaders.get(type);
    if(loader == null) {
      throw new IllegalArgumentException("No resource loader found for resource type " + type + ".");
    }
    
    T output = (T) loader.load(name);
    if(output == null) return null;
    cache.put(name, output);
    
    if(loading) output.load();
    return output;
  }

  @Override
  protected void resetObject() {
  }

  @Override
  protected void dettached(NamedObject parent) {
    super.dettached(parent);
    
    if(parent instanceof GraphicsSystem) {
      ((GraphicsSystem)parent).removeWindowListener(this);
    }
  }

  @Override
  protected void attached(NamedObject parent) {
    super.attached(parent);
    
    if(parent instanceof GraphicsSystem) {
      ((GraphicsSystem)parent).addWindowListener(this);
    }
  }
  
  
  
  private final Map<String, Resource> cache;
  private final Map<Class<? extends Resource>, ResourceLoader> loaders;
  private boolean loading;

  private static final String locprefix = ResourceManager.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));

  @Override
  public void windowCreated() {
    loading = true;
    
    for(Resource res : cache.values())
      res.load();
  }

  @Override
  public void windowDestroyed() {
    loading = false;
  }

  @Override
  public void windowResized() {
  }
}
