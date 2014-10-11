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

package taiga.gpvm.registry;

import taiga.code.text.TextLocalizer;
import taiga.code.util.DataNode;
import taiga.gpvm.render.EntityRenderer;
import taiga.gpvm.render.TileRenderer;

/**
 * An entry that contains all of the information needed to render a given type
 * of {@link Tile}.
 * 
 * @author russell
 */
public class EntityRenderingEntry extends RegistryEntry {
  
  public static final String FIELD_NAME_RENDERER = "renderer";
  
  /**
   * The {@link Class} of the {@link TileRenderer} that will be used to render the
   * given {@link Tile}s.
   */
  public final EntityRenderer renderer;
  
  /**
   * The {@link EntityType} for the entities that will use this {@link EntityRenderingEntry}
   * for rendering.
   */
  public final EntityType entity;

  /**
   * Creates a new {@link RenderingEntry} with the given attributes.
   * 
   * @param rend The class of the {@link TileRenderer} for this {@link RenderingEntry}.
   * @param e The {@link TileEntry} that this {@link RenderingEntry} is for.
   */
  public EntityRenderingEntry(EntityRenderer rend, EntityType e) {
    super(e.name);
    
    renderer = rend;
    entity = e;
  }
  
  /**
   * Creates a new {@link EntityRenderingEntry} for the given {@link EntityType}
   * using values from the given {@link DataNode}.
   * 
   * @param type The {@link EntityType} that this {@link EntityRenderingEntry} is for.
   * @param data The {@link DataNode} containing values for this {@link EntityRenderingEntry}.
   * 
   * @throws ReflectiveOperationException Thrown if the {@link EntityRenderer} class cannot be found
   * and instantiated.
   */
  public EntityRenderingEntry(EntityType type, DataNode data) throws ReflectiveOperationException {
    super(type.name);
    entity = type;
    
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    Class<? extends EntityRenderer> rendclass = 
      (Class<? extends EntityRenderer>) loader.loadClass((String) data.getValueByName(FIELD_NAME_RENDERER));
    
    EntityRenderer temp;
    try {
      //first try to construct it with a datanode
       temp = rendclass.getConstructor(DataNode.class).newInstance(data);
    } catch(NoSuchMethodException e) {
      try {
        //then try the empty constructor if the datanode dos not work.
        temp = rendclass.newInstance();
      } catch(ReflectiveOperationException ex) {
        throw new ReflectiveOperationException(TextLocalizer.localize(RENDERER_CLASS_EX, type), ex);
      }
    }
    
    renderer = temp;
  }
  
  private static final String locprefix = EntityRenderingEntry.class.getSimpleName().toLowerCase();
  
  private static final String RENDERER_CLASS_EX = locprefix + ".renderer_class_ex";
}
