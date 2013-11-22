/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * A multiple parent {@link URLClassLoader}.  This class loader can have multiple
 * parents that it will load from.  First it will check to see if the class is 
 * currently loaded before checking each parent class loader in order.  Finally
 * it will load the class itself if the class is not found.
 * @author russell
 */
public class MPUClassLoader extends URLClassLoader {

  public MPUClassLoader(URL[] urls) {
    super(urls);
    parents = new ArrayList<>();
  }

  public MPUClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
    parents = new ArrayList<>();
  }

  public MPUClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
    super(urls, parent, factory);
    parents = new ArrayList<>();
  }
  
  public MPUClassLoader(URL[] urls, ClassLoader parent, ClassLoader ... otherparents) {
    super(urls, parent);
    parents = new ArrayList<>();
  }
  
  public MPUClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory, ClassLoader ... otherparents) {
    super(urls, parent, factory);
    parents = new ArrayList<>();
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    Class<?> result = super.findClass(name);
    
    if(result != null) return result;
    if(loading) return null;
    
    //use the loadng boolean to prevent this method from being called multiple
    //times while loading the same class in the case of cyclic dependencies
    loading = true;
    for(ClassLoader par : parents) {
      result = par.loadClass(name);
      if(result != null) return result;
    }
    loading = false;
    
    return null;
  }

  @Override
  public URL findResource(String name) {
    URL result = super.findResource(name);
    
    if(result != null) return result;
    if(loading) return null;
    
    //use the loadng boolean to prevent this method from being called multiple
    //times while loading the same class in the case of cyclic dependencies
    loading = true;
    for(ClassLoader par : parents) {
      result = par.getResource(name);
      if(result != null)
        return result;
    }
    loading = false;
    
    return null;
  }

  @Override
  public Enumeration<URL> findResources(String name) throws IOException {
    Enumeration<URL> result = super.findResources(name);
    
    if(result != null) return result;
    if(loading) return null;
    
    //use the loadng boolean to prevent this method from being called multiple
    //times while loading the same class in the case of cyclic dependencies
    loading = true;
    for(ClassLoader par : parents) {
      result = par.getResources(name);
      if(result != null)
        return result;
    }
    loading = false;
    
    return null;
  }
  
  private ArrayList<ClassLoader> parents;
  private boolean loading;
}
