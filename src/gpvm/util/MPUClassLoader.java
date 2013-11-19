/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gpvm.util;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;

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
  
  private ArrayList<ClassLoader> parents;
}
