/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.graphics;

/**
 * Used to switch between {@link Renderable} so that at most one child {@link Renderable}
 * is visible at a time.  For example the {@link Renderable}s could be various
 * screens in an application, and this class will allow for switching between them.
 * The displayed {@link Renderable} can be changed either through this class or
 * through the {@link Renderable#setVisible(boolean)} method.
 * 
 * @author russell
 */
public class RenderableSwitcher extends Renderable {

  public RenderableSwitcher(String name) {
    super(name);
  }

  @Override
  protected void updateSelf() {}

  @Override
  protected void renderSelf(int pass) {}
  
}
