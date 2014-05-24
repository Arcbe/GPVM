package taiga.gpvm.registry;

import taiga.gpvm.HardcodedValues;
import static taiga.gpvm.HardcodedValues.NAMESPACE_SEPERATOR;
import static taiga.gpvm.HardcodedValues.RENDERER_CLASS_FIELD;
import static taiga.gpvm.HardcodedValues.RENDERING_INFO_FIELD;
import gpvm.ThreadingManager;
import gpvm.io.DataLoader;
import gpvm.io.DataNode;
import gpvm.io.InvalidDataFileException;
import gpvm.util.StringManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.gpvm.render.Renderer;

/**
 *
 * @author russell
 */
public class RenderingRegistry extends Registry<RenderingEntry>{
  public RenderingRegistry() {
    super(HardcodedValues.RENDERING_REGISTRY_NAME);
    
    tileindex = new HashMap<>();
  }

  @Override
  public void addEntry(RenderingEntry ent) {
    super.addEntry(ent);
    
    tileindex.put(ent.tile, ent);
  }
  
  public RenderingEntry getEntry(TileEntry tile) {
    return tileindex.get(tile);
  }
  
  public void loadRenderRegistryData(File in, String namespace, ClassLoader loader) throws FileNotFoundException, InvalidDataFileException, InstantiationException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
    DataNode data = DataLoader.loadFile(in);
    
    TileRegistry tiles = (TileRegistry) getObject(HardcodedValues.TILE_REGISTRY_NAME);
    
    try {
      for(String val : data.getValues()) {
        if(!data.isType(val, DataNode.class)) {
          log.log(Level.WARNING, StringManager.getLocalString("warn_unknown_render_data", val, in));
          continue;
        }
        
        DataNode entrydata = data.getValue(val);
        DataNode renddata = null;
        Class<? extends Renderer> rendclass = null;
        
        //get the rendering class.
        if(entrydata.isType(RENDERER_CLASS_FIELD, String.class)) {
          rendclass = (Class<? extends Renderer>) loader.loadClass((String) entrydata.getValue(RENDERER_CLASS_FIELD));
        }
        
        //get the data for the rendering entry.
        if(entrydata.isType(RENDERING_INFO_FIELD, DataNode.class)) {
          renddata = entrydata.getValue(RENDERING_INFO_FIELD);
        }
        
        Renderer temp = rendclass.newInstance();
        Class<? extends RenderingInfo> infoclass = temp.getInfoClass();
        
        RenderingInfo info = null;
        if(infoclass != null) {
          info = infoclass.getConstructor(DataNode.class).newInstance(renddata);
        }
          
        RenderingEntry entry = new RenderingEntry(rendclass, info, tiles.getEntry(namespace + NAMESPACE_SEPERATOR + val));
        addEntry(entry);
      }
    } finally {
      ThreadingManager.getInstance().releaseWrite();
    }
  }
  
  private Map<TileEntry, RenderingEntry> tileindex;
  
  private static final String locprefix = RenderingRegistry.class.getName().toLowerCase();
  
  private static final Logger log = Logger.getLogger(locprefix, 
    System.getProperty("taiga.code.logging.text"));
}
