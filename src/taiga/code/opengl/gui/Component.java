/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.opengl.gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.logging.Logger;
import taiga.code.math.Matrix4;
import taiga.code.opengl.Renderable;
import taiga.code.util.DataNode;

/**
 * This is the base class for objects within a GUI.
 * 
 * @author russell
 */
public abstract class Component extends Renderable {
  
  public static final String FIELD_NAME_TARGET_PASS = "rendering-pass";
  public static final String FIELD_NAME_PREF_HEIGHT = "preferred-height";
  public static final String FIELD_NAME_PREF_WIDTH = "preferred-width";
  public static final String FIELD_NAME_MIN_HEIGHT = "minimum-height";
  public static final String FIELD_NAME_MIN_WIDTH = "minimum-width";
  public static final String FIELD_NAME_HEIGHT = "height";
  public static final String FIELD_NAME_WIDTH = "width";
  public static final String FIELD_NAME_X = "x";
  public static final String FIELD_NAME_Y = "y";

  /**
   * Constructs a new {@link Component} with the given name.
   * 
   * @param name THe name for this {@link Component}.
   */
  public Component(String name) {
    super(name);
    
    bounds = new Rectangle();
    minsize = new Dimension();
    prefsize = new Dimension();
    
    tarpass = -1;
  }
  
  /**
   * Constructs a new {@link COmponent} with the given name and with parameters
   * based on the given {@link DataNode}.
   * 
   * @param params The parameters to apply to the {@link Component}.
   */
  public Component(DataNode params) {
    super(params.name);
    
    params = (DataNode) params.data;
    
    bounds = new Rectangle();
    minsize = new Dimension();
    prefsize = new Dimension();
    
    tarpass = -1;
    
    Integer temp;
    if((temp = params.getValueByName(FIELD_NAME_TARGET_PASS)) != null) tarpass = temp;
    if((temp = params.getValueByName(FIELD_NAME_PREF_HEIGHT)) != null) prefsize.height = temp;
    if((temp = params.getValueByName(FIELD_NAME_PREF_WIDTH)) != null) prefsize.width = temp;
    if((temp = params.getValueByName(FIELD_NAME_MIN_HEIGHT)) != null) minsize.height = temp;
    if((temp = params.getValueByName(FIELD_NAME_MIN_WIDTH)) != null) minsize.width = temp;
    if((temp = params.getValueByName(FIELD_NAME_HEIGHT)) != null) bounds.height = temp;
    if((temp = params.getValueByName(FIELD_NAME_WIDTH)) != null) bounds.width = temp;
    if((temp = params.getValueByName(FIELD_NAME_X)) != null) bounds.x = temp;
    if((temp = params.getValueByName(FIELD_NAME_Y)) != null) prefsize.height = temp;
  }
  
  /**
   * Resizes this {@link Component}.
   * 
   * @param w The new width.
   * @param h The new height.
   */
  public void setSize(int w, int h) {
    bounds.setSize(w, h);
    
    fireBoundsChanged();
  }
  
  /**
   * Changes the location for this {@link Component}.
   * 
   * @param x The x coordinate for the {@link Component}.
   * @param y The y coordinate for the {@link Component}.
   */
  public void setLocation(int x, int y) {
    bounds.setLocation(x, y);
  }
  
  /**
   * Resizes and moves this {@link Component}.
   * 
   * @param x The new x coordinate for this {@link Component}.
   * @param y The new y coordinate for this {@link Component}.
   * @param w The new width for this {@link Component}.
   * @param h The new height for this {@link Component}.
   */
  public void setBounds(int x, int y, int w, int h) {
    bounds.setBounds(x, y, w, h);
    
    fireBoundsChanged();
  }
  
  /**
   * Sets the preferred size of this {@link Component}.  This is the target
   * size for this {@link Component} while laying out {@link Component}s.
   * 
   * @param w The preferred width.
   * @param h The preferred height.
   */
  public void setPreferredSize(int w, int h) {
    prefsize.setSize(w, h);
    
    firePrefSizeChanged();
  }
  
  /**
   * Sets the minimum size that this {@link Component} can be.
   * 
   * @param w The minimum width of the {@link Component}.
   * @param h The minimum height of the {@link Component}.
   */
  public void setMinimumSize(int w, int h) {
    minsize.setSize(w, h);
  }
  
  /**
   * The x coordinate of this {@link Component}.
   * 
   * @return The x coordinate.
   */
  public int getX() {
    return bounds.x;
  }
  
  /**
   * The y coordinate of this {@link Component}.
   * @return 
   */
  public int getY() {
    return bounds.y;
  }
  
  /**
   * Returns the current width of this {@link Component}.
   * 
   * @return The width.
   */
  public int getWidth() {
    return bounds.width;
  }
  
  /**
   * Returns the current height of this {@link Component}.
   * 
   * @return The height.
   */
  public int getHeight() {
    return bounds.height;
  }
  
  /**
   * Returns the minimum width of this {@link Component}.  This is for use
   * in laying out {@link Component}.
   * 
   * @return The minimum width.
   */
  public int getMinWidth() {
    return minsize.width;
  }
  
  /**
   * Returns the minimum height of this {@link Component}.  This is for use
   * in laying out {@link Component}s.
   * 
   * @return The minimum height.
   */
  public int getMinHeight() {
    return minsize.height;
  }
  
  /**
   * Returns the ideal width for this {@link Component}.  This is for use
   * in laying out {@link Component}s.
   * 
   * @return The preferred width.
   */
  public int getPreferredWidth() {
    return prefsize.width;
  }
  
  /**
   * Returns the ideal height for this {@link Component}.  This is for use
   * in laying out {@link Component}s.
   * 
   * @return The preferred height.
   */
  public int getPreferredHeight() {
    return prefsize.height;
  }
  
  /**
   * The rendering pass that this {@link Component} will be rendered on.
   * If set to -1 then this will use the target pass of its parent, or 0 if the
   * parent is not a {@link Component}.
   * 
   * @param pass The target rendering pass.
   */
  public void setTargetPass(int pass) {
    tarpass = pass;
    
    setPasses(pass + 1);
  }
  
  /**
   * Returns the rendering pass that this {@link Component} will be rendered
   * on.
   * 
   * @return The target rendering pass for this {@link Component}.
   */
  public int getTargetPass() {
    if(tarpass > 0) return tarpass;
    
    if(getParent() instanceof Component) {
      return ((Component)getParent()).getTargetPass();
    } else {
      return 0;
    }
  }
  
  private final Rectangle bounds;
  private final Dimension minsize;
  private final Dimension prefsize;
  
  private int tarpass;
  
  @Override
  protected final void renderSelf(int pass, Matrix4 proj) {
    if(pass != getTargetPass()) return;
    
    drawComponent(proj);
  }
  
  /**
   * Renders the {@link Component} during the targeted rendering pass.
   * 
   * @param proj The projection to use for this {@link Component}.
   */
  protected abstract void drawComponent(Matrix4 proj);
  
  private void fireBoundsChanged() {
    
  }
  
  private void firePrefSizeChanged() {
    
  }
  
  private void fireMinSizeChanged() {
    
  }

  private static final String locprefix = Component.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
