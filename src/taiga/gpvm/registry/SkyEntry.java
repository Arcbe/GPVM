/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.gpvm.registry;

import java.util.logging.Logger;
import taiga.gpvm.map.World;
import taiga.gpvm.render.SkyBoxRenderer;

/**
 * A {@link RegistryEntry} that associates a {@link SkyBoxRenderer} with a {@link World}.
 * 
 * @author russell
 */
public class SkyEntry extends RegistryEntry {

  /**
   * The {@link Class} of the {@link SkyBoxRenderer} for this {@link SkyEntry}.
   */
  public final Class<? extends SkyBoxRenderer> skyrenderer;
  
  /**
   * Creates a new {@link SkyEntry} with the given parameters.
   * 
   * @param name The name of the entry which should correspond to the name of the
   * {@link World} that this {@link SkyEntry} is for.
   * @param rend The class of the {@link SkyBoxRenderer} for this {@link SkyEntry}.
   */
  public SkyEntry(String name, Class<? extends SkyBoxRenderer> rend) {
    super(name);
    
    skyrenderer = rend;
  }
  
  /**
   * Returns the {@link SkyBoxRenderer} associated with this {@link SkyEntry}.  If there is
   * not currently an instance of a {@link SkyBoxRenderer} for this {@link SkyEntry}
   * then one will be created.
   * 
   * @return The {@link SkyBoxRenderer} for this {@link SkyEntry}.
   * @throws ReflectiveOperationException Thrown if the {@link SkyBoxRenderer} class
   * for this {@link SkyEntry} could not be instantiated.
   */
  public SkyBoxRenderer getRenderer() throws ReflectiveOperationException {
    if(rend == null)
      rend = skyrenderer.newInstance();
    
    return rend;
  }
  
  private SkyBoxRenderer rend;

  private static final String locprefix = SkyEntry.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
