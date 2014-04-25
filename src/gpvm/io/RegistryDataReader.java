/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.io;

import gpvm.Registrar;
import gpvm.ThreadingManager;
import gpvm.map.TileDefinition;
import taiga.gpvm.render.RenderRegistry;
import taiga.gpvm.render.TileRenderer;
import gpvm.util.StringManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loads data from {@link File}s into the registrar.  The data files can be
 * any format that is registered with the {@link DataLoader} and data will be
 * appended to the registries contained in the {@link Registrar}.
 * 
 * @author russell
 */
public class RegistryDataReader {
  //field names for Tile Registry names
  public static final String CANON_NAME_FIELD = "canonicalname";
  public static final String METADATA_FIELD = "metadata";
  public static final String OPAQUE_FIELD = "opaque";
  public static final String NAMESPACE_SEPERATOR = ".";
  
  //field names for Render Registry names
  public static final String RENDERER_CLASS_FIELD = "rendering-class";
  public static final String RENDERING_INFO_FIELD = "rendering-info";
  
  public static void loadTileRegistryData(URL url, String namespace) throws URISyntaxException, FileNotFoundException, InvalidDataFileException {
    File in = new File(url.toURI());
    
    loadTileRegistryData(in, namespace);
  }
  
  public static void loadTileRegistryData(File in, String namespace) throws FileNotFoundException, InvalidDataFileException {
    DataNode data = DataLoader.loadFile(in);
    
    ThreadingManager.getInstance().requestWrite();
    
    try {
      for(String val : data.getValues()) {
        if(!data.isType(val, DataNode.class)) {
          log.log(Level.WARNING, StringManager.getLocalString("warn_unknown_tile_data", val, in));
          continue;
        }

        DataNode cur = data.getValue(val);

        String canonname = namespace + NAMESPACE_SEPERATOR;
        if(cur.isType(CANON_NAME_FIELD, String.class)) canonname += cur.getValue(CANON_NAME_FIELD);
        else canonname += val; //use the tiles display name as canonical

        int metadata = 0;
        if(cur.isType(METADATA_FIELD, Integer.class)) metadata = cur.getValue(METADATA_FIELD);

        boolean opaque = true;
        if(cur.isType(OPAQUE_FIELD, Boolean.class)) opaque = cur.getValue(OPAQUE_FIELD);

        TileDefinition def = new TileDefinition(val, canonname, metadata, opaque);
        Registrar.getInstance().addTileEntry(def);
      }
    } finally {
      ThreadingManager.getInstance().releaseWrite();
    }
  }
  
  public static void loadRenderRegistryData(URL url, String namespace, ClassLoader loader) throws URISyntaxException, FileNotFoundException, InvalidDataFileException, InstantiationException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
    File in = new File(url.toURI());
    
    loadRenderRegistryData(in, namespace, loader);
  }
  
  public static void loadRenderRegistryData(File in, String namespace, ClassLoader loader) throws FileNotFoundException, InvalidDataFileException, InstantiationException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
    DataNode data = DataLoader.loadFile(in);
    
    ThreadingManager.getInstance().requestWrite();
    
    try {
      for(String val : data.getValues()) {
        if(!data.isType(val, DataNode.class)) {
          log.log(Level.WARNING, StringManager.getLocalString("warn_unknown_render_data", val, in));
          continue;
        }
        
        DataNode entrydata = data.getValue(val);
        DataNode renddata = null;
        Class<? extends TileRenderer> rendclass = null;
        
        //get the rendering class.
        if(entrydata.isType(RENDERER_CLASS_FIELD, String.class)) {
          rendclass = (Class<? extends TileRenderer>) loader.loadClass((String) entrydata.getValue(RENDERER_CLASS_FIELD));
        }
        
        //get the data for the rendering entry.
        if(entrydata.isType(RENDERING_INFO_FIELD, DataNode.class)) {
          renddata = entrydata.getValue(RENDERING_INFO_FIELD);
        }
          
        RenderRegistry.RendererEntry entry = new RenderRegistry.RendererEntry(rendclass, renddata);
        long tileid = Registrar.getInstance().getTileRegistry().getTileID(namespace + NAMESPACE_SEPERATOR + val);
        Registrar.getInstance().addRenderingEntry(entry, tileid);
      }
    } finally {
      ThreadingManager.getInstance().releaseWrite();
    }
  }
  
  private static final Logger log = Logger.getLogger(RegistryDataReader.class.getName());
}
