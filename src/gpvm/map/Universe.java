/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.map;

import gpvm.util.StringManager;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author russell
 */
public class Universe {
  public static class World {
    public String name;
    public Class<? extends MapGenerator> generator;
  }
  
  public static Universe getInstance() {
    return instance;
  }
  
  public void addWorld(World world) {
    MapGenerator gen;
    
    try {
      gen = world.generator.newInstance();
    } catch (InstantiationException | IllegalAccessException ex) {
      log.log(Level.SEVERE, StringManager.getLocalString("err_create_world", world.name), ex);
      return;
    }
    
    GameMap nmap = new GameMap(gen);
    worlds.put(world.name, nmap);
  }
  
  public GameMap getWorld(String name) {
    return worlds.get(name);
  }
  
  private Map<String, GameMap> worlds;
  
  private Universe() {
    worlds = new HashMap<>();
  }
  
  private static final Universe instance = new Universe();
  private static final Logger log = Logger.getLogger(Universe.class.getName());
}
