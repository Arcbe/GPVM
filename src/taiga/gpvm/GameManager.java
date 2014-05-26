/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package taiga.gpvm;

import taiga.gpvm.HardcodedValues;
import taiga.code.registration.RegisteredSystem;

import static taiga.gpvm.HardcodedValues.GAMEMANAGER_NAME;
import taiga.gpvm.render.GraphicsRoot;
import taiga.code.io.DataFileManager;
import taiga.code.registration.RegisteredObject;
import taiga.code.util.SettingManager;
import taiga.code.yaml.YAMLDataReader;
import taiga.gpvm.map.Universe;
import taiga.gpvm.registry.RenderingRegistry;
import taiga.gpvm.registry.TileRegistry;
import taiga.gpvm.screens.GameScreen;

/**
 * Controls the overall state of the game.  This class will initialize the
 * various system of the game and clean them up as needed.  For most cases 
 * interaction should go through the this class when possible so that all systems
 * are notified of changes.
 * 
 * @author russell
 */
public final class GameManager extends RegisteredSystem {

  @Override
  public void resetObject() {}

  @Override
  protected void startSystem() {}

  @Override
  protected void stopSystem() {}

  public GameManager(boolean server, boolean client) {
    super(GAMEMANAGER_NAME);
    
    addChild(new DataFileManager());
    getObject(DataFileManager.DATAFILEMANAGER_NAME).addChild(new YAMLDataReader());
    
    SettingManager settings = new SettingManager();
    addChild(settings);
    
    addChild(new TileRegistry());
    addChild(new Universe());
    
    setServerMode(server);
    setClientMoe(client);
  }
  
  public void setServerMode(boolean server) {
    if(server) {
      
    }
  }
  
  public void setClientMoe(boolean client) {
    RegisteredObject rendreg = getObject(HardcodedValues.RENDERING_REGISTRY_NAME);
    RegisteredObject graphics = getObject(HardcodedValues.GRAPHICSSYSTEM_NAME);
    RegisteredObject gamescreen = getObject(HardcodedValues.GAME_SCREEN_NAME);
    
    if(client) {
      if(rendreg == null) addChild(new RenderingRegistry());
      if(graphics == null) {
        addChild(new GraphicsRoot());
        graphics = getObject(HardcodedValues.GRAPHICSSYSTEM_NAME);
      }
      if(gamescreen == null) graphics.addChild(new GameScreen());
    }
  }
}
