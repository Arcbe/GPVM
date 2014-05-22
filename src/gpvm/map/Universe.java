/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.map;

import taiga.gpvm.map.MapGenerator;
import taiga.gpvm.map.GameMap;
import taiga.gpvm.render.GraphicsRoot;
import gpvm.util.StringManager;
import java.util.Collection;
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
//    MapGenerator gen;
//    
//    try {
//      gen = world.generator.newInstance();
//    } catch (InstantiationException | IllegalAccessException ex) {
//      log.log(Level.SEVERE, StringManager.getLocalString("err_create_world", world.name), ex);
//      return;
//    }
//    
//    GameMap nmap = new GameMap(gen);
//    worlds.put(world.name, nmap);
//    
//    log.log(Level.INFO, StringManager.getLocalString("info_world_added", world.name));
  }
  
  public void addWorlds(Collection<World> worlds) {
    for(World world : worlds)
      addWorld(world);
  }
  
  public GameMap getWorld(String name) {
    return worlds.get(name);
  }
  
  public void setActiveWorld(String name) {
    GameMap map = getWorld(name);
    
    //GraphicsRoot.getInstance().setMap(map);
    
    activemap = map;
  }
  
  public GameMap getActiveWorld() {
    return activemap;
  }
  
  private Map<String, GameMap> worlds;
  private GameMap activemap;
  
  private Universe() {
    worlds = new HashMap<>();
  }
  
  private static final Universe instance = new Universe();
  private static final Logger log = Logger.getLogger(Universe.class.getName());
}
