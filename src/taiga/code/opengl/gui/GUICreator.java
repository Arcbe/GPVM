/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.opengl.gui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import taiga.code.util.DataNode;
import taiga.code.registration.RegisteredObject;

/**
 * Creates GUIs using a {@link DataNode} allowing them to be loaded from
 * a file instead of created programmatically.
 * 
 * @author russell
 */
public class GUICreator {
  
  public static final String FIELD_NAME_CLASS = "class";
  public static final String FIELD_NAME_META_COMPONENT = "meta";
  public static final String FIELD_NAME_CHILDREN = "children";
  public static final String FIELD_NAME_ALIAS = "alias";
  
  /**
   * Create a GUI based on the data contained in the given {@link DataNode}.
   * 
   * @param data The {@link DataNode} to construct the GUI with.
   * @return A GUI corresponding to the given data as closely as possible.
   */
  public static Component createGUI(DataNode data) throws GUIException {
    return createGUI(data, ClassLoader.getSystemClassLoader());
  }
  
  /**
   * Creates a GUI based on the data contained in the given {@link DataNode}.
   * This will also use the given {@link ClassLoader} instead of the default one
   * to load the {@link Component}s for the GUI.  If the GUI could not be created
   * then this will return null.
   * 
   * @param data The data to construct the GUI with.
   * @param loader The {@link ClassLoader} to load the {@link Component}s with.
   * @return A GUI corresponding to the given data as closely as possible.
   */
  public static Component createGUI(DataNode data, ClassLoader loader) throws GUIException {
    DataNode aliasnode = data.getChild(FIELD_NAME_ALIAS);
    
    for(RegisteredObject obj : data) {
      if(!obj.name.equals(FIELD_NAME_ALIAS) && obj instanceof DataNode)
        try {
          return createElement((DataNode) obj, loader);
      } catch (
        NoSuchMethodException | 
          ClassNotFoundException | 
          InstantiationException | 
          IllegalAccessException | 
          IllegalArgumentException | 
          InvocationTargetException ex) {
        throw new GUIException(GUI_LOADING_ERROR, ex);
      }
    }
    
    return null;
  }
  
  private static Component createElement(DataNode data, ClassLoader loader) 
    throws NoSuchMethodException, 
    ClassNotFoundException,
    InstantiationException,
    IllegalAccessException,
    IllegalArgumentException, 
    InvocationTargetException,
    GUIException {
    
    Component comp = loadComponent(data, loader);
    
    for(RegisteredObject obj : data) {
      if(obj instanceof DataNode)
        comp.addChild((DataNode) obj);
    }
    
    return comp;
  }
  
  private static RegisteredObject createMeta(DataNode data, ClassLoader loader, Map<String,String> alias) {
    String name = data.name;
    
    String classname = data.getValueByName(FIELD_NAME_CLASS);
    if(classname == null) {
      log.log(Level.SEVERE, NO_COMPONENT_CLASS, name);
      return null;
    }
    if(alias.containsKey(classname)) classname = alias.get(classname);
    
    //get the class for the component
    Class<? extends Component> compclass;
    try {
      compclass = (Class<? extends Component>) loader.loadClass(classname);
    } catch (ClassNotFoundException | ClassCastException ex) {
      log.log(Level.SEVERE, COMPONENT_CLASS_NOT_FOUND, ex);
      return null;
    }
    
    RegisteredObject result;
    Constructor<? extends RegisteredObject> cons;
    try {
      cons = compclass.getConstructor(String.class, DataNode.class);
    } catch (NoSuchMethodException | SecurityException e) {
      //try for a constructor with a string as an argument.
      try {
        cons = compclass.getConstructor(String.class);
      } catch (NoSuchMethodException | SecurityException ex) {
        log.log(Level.SEVERE, INVALID_CONSTRUCTOR, compclass);
        return null;
      }
    }
    try {
      result = cons.newInstance(name, data);
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
      log.log(Level.SEVERE, UNABLE_TO_CONSTRUCT, ex);
      return null;
    }
    
    return result;
  }
  
  private static Component loadComponent(DataNode data, ClassLoader loader) 
    throws NoSuchMethodException, 
    ClassNotFoundException, 
    InstantiationException, 
    IllegalAccessException, 
    IllegalArgumentException, 
    InvocationTargetException,
    GUIException {
    
    DataNode attrs = (DataNode) data.data;
    
    if(attrs == null || attrs.getValueByName(FIELD_NAME_CLASS) == null) {
      throw new GUIException(NO_COMPONENT_CLASS);
    }
    
    Class<? extends Component> clazz = (Class<? extends Component>) loader.loadClass((String) attrs.getValueByName(FIELD_NAME_CLASS));
    
    try {
      Constructor cons;
      cons = clazz.getConstructor(DataNode.class);
      
      return (Component) cons.newInstance(data);
    } catch(NoSuchMethodException ex) {
      return clazz.newInstance();
    }
  }

  private static final String locprefix = GUICreator.class.getName().toLowerCase();
  
  private static final String GUI_LOADING_ERROR = locprefix + ".gui_loading_error";
  private static final String NO_COMPONENT_CLASS = locprefix + ".no_component_class";
  private static final String COMPONENT_CLASS_NOT_FOUND = locprefix + ".component_class_not_found";
  private static final String INVALID_CONSTRUCTOR = locprefix + ".invalid_constructor";
  private static final String UNABLE_TO_CONSTRUCT = locprefix + ".unable_to_construct";

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
