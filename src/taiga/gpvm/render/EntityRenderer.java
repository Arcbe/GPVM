package taiga.gpvm.render;

import taiga.code.util.DataNode;
import taiga.gpvm.entity.Entity;

/**
 * Handles the rendering of a set of {@link Entity}.  A single {@link EntityRenderer}
 * will be used to render multiple {@link Entity}s of the same type.  Also this
 * class should either have a constructor that accepts a {@link DataNode} or
 * one that has no parameters in order to be constructed properly.
 * 
 * @author russell
 */
public interface EntityRenderer {
  /**
   * Renders the given {@link Entity}.
   * 
   * @param ent The {@link Entity} to render.
   * @param pass The current rendering pass.
   */
  public void render(Entity ent, int pass);
  
  /**
   * Loads data specific to the type of {@link Entity} that will be rendered
   * with this {@link EntityRenderer}.
   * 
   * @param data The {@link DataNode} containing the data.
   */
  public void loadData(DataNode data);
}
