/*
 * Copyright (C) 2014 Russell Smith
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package taiga.code.registration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import taiga.code.text.TextLocalizer;

/**
 * {@link NamedObject}s forms a naming tree of objects.  This tree can 
 * then be traversed and find an object given the full name or relative name
 * from any object within the tree.
 * 
 * Serialization of a {@link NamedObject} will only serialize children and not
 * the parent, if any.  Any serializable {@link ChildListener}s will also be
 * serialized.
 * 
 * @author russell
 */
public class NamedObject implements Iterable<NamedObject>, Serializable {

  /**
   * A simple combination of a {@link NamedObject} with a {@link Method}
   * that allows for easy invocation of the {@link Method} through the reflection
   * API.
   */
  public final class NamedObjectMethod {
    public final Method method;
    public final NamedObject target;

    /**
     * Creates a new {@link NamedObjectMethod} with the given method and
     * target.
     * 
     * @param method The {@link Method} that will be executed.
     * @param target The target {@link NamedObject} for the {@link Method}.
     */
    public NamedObjectMethod(Method method, NamedObject target) {
      this.method = method;
      this.target = target;
      
      log.log(Level.FINER, GET_METHOD, new Object[] {target, method.toGenericString()});
    }
    
    /**
     * Invokes the {@link Method} on the target {@link NamedObject} using
     * the supplied parameters.
     * 
     * @param params The parameters for the {@link Method} invocation.
     * @return The returned value from the {@link Method} or null.
     * @throws IllegalAccessException Thrown if {@link Method#invoke(java.lang.Object, java.lang.Object...) }
     * throws an {@link IllegalAccessException}.
     * @throws IllegalArgumentException Thrown if {@link Method#invoke(java.lang.Object, java.lang.Object...) }
     * throws an {@link IllegalArgumentException}.
     * @throws InvocationTargetException Thrown if {@link Method#invoke(java.lang.Object, java.lang.Object...) }
     * throws an {@link InvocationTargetException}.
     */
    public Object invoke(Object ... params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      log.log(Level.FINEST, EXECUTE_METHOD, new Object[] {target, method.toGenericString()});
      return method.invoke(target, params);
    }
  }
  
  /**
   * The default name separator for names in a registration path.
   */
  public static final String SEPARATOR = ".";
  
  /**
   * The name for this {@link NamedObject}.  This will be used to identify
   * this {@link NamedObject} within the registration tree.
   */
  public final String name;
  
  public NamedObject() {
    name = null;
    childlist = new HashSet<>();
    children = new HashMap<>();
  }

  /**
   * Creates a new {@link RegisteredObject} with the given name.  Each {@link
   * RegisteredObject} requires a name, so the argument must not be null or
   * an empty {@link String}.
   * 
   * @param name The name for the new {@link RegisteredObject}
   */
  public NamedObject(String name) {
    if(!checkName(name))
      throw new IllegalArgumentException(TextLocalizer.localize(ILLEGAL_NAME, name));
    
    this.name = name;
    childlist = new HashSet<>();
    
    log.log(Level.FINEST, CREATION, name);
  }
  
  /**
   * Returns the full name of this object including all parents.  Each name will
   * be appended to previous separated by a '.' in order from root to this
   * {@link NamedObject}.  This name can be used to access this object from
   * any other object within the same tree of {@link NamedObject}s.
   * 
   * @return The full name of this object.
   */
  public String getFullName() {
    if(parent == null) return name;
    else return parent.getFullName() + SEPARATOR + name;
  }
  
  /**
   * Returns the parent of this {@link NamedObject} if any.
   * 
   * @return The parent of this object.
   */
  public NamedObject getParent() {
    return parent;
  }
  
  /**
   * Retrieves the child of this {@link NamedObject} with the given
   * name if there is one, otherwise this returns null.
   * 
   * @param <T> The type for the returned child
   * @param name The name of the desired child.
   * @return The desired child or null.
   */
  @SuppressWarnings("unchecked")
  public <T extends NamedObject> T getChild(String name) {
    if(children == null) return null;
    
    try {
      T result = (T) children.get(name);
      return result;
    } catch(ClassCastException ex) {
      log.log(Level.WARNING, WRONG_CHILD_CLASS, new Object[]{name});
      
      return null;
    }
  }
  
  /**
   * Returns an immutable list of the registered children.  Some of the entries
   * may be null if children have been removed.  The index of the children corresponds
   * to their IDs.
   * 
   * @return 
   */
  public Collection<NamedObject> getChildren() {
    if(children == null)
      children = new HashMap<>();
    
    return Collections.unmodifiableCollection(children.values());
  }
  
  /**
   * Adds a child to this {@link NamedObject}.  The child will be added to
   * the first available slot in this object and can be index by the id returned
   * by this method.
   * 
   * @param <T> The return type for this method.
   * @param child The child to add.
   * @return A reference to the input {@link NamedObject} or null if the child
   * could not be added.
   * @throws NullPointerException Thrown if the child is null.
   */
  @SuppressWarnings("unchecked")
  public <T extends NamedObject> T addChild(NamedObject child) {
    if(child == null) return null;
    
    //check to make sure that this child does not already have a parent. kidnapping is
    //not allowed
    if(child.getParent() != null) {
      log.log(Level.WARNING, EXISTING_PARENT, new Object[]{getFullName(), child.name});
      
      return null;
    }
    
    //make sure that the child is not already added.  No cloning either.
    if(children.get(child.name) != null) {
      log.log(Level.WARNING, ALREADY_ADDED, new Object[]{getFullName(), child.name});

      return null;
    }

    children.put(child.name, child);
    
    log.log(Level.FINE, ADDED_CHILD, new Object[]{getFullName(), child.name});
    child.parent = this;
    
    //now notify the listeners
    fireChildAdded(child);
    
    try {
      return (T) child;
    } catch(ClassCastException ex) {
      log.log(Level.WARNING, WRONG_CHILD_CLASS, child);
      
      return null;
    }
  }
  
  /**
   * Removes a child from this {@link NamedObject}.
   * 
   * @param child The child to remove.
   * @return Whether the removal was successful.
   */
  public boolean removeChild(NamedObject child) {
    
    boolean result = children.containsKey(child.name);
    
    if(result) {
      if(child == children.put(child.name, null))
        child.parent = null;

      fireChildRemoved(child);
      log.log(Level.FINE, REMOVED_CHILD, new Object[]{getFullName(), child.name});
    }
    
    return result;
  }
  
  /**
   * Removes all children from this {@link NamedObject}.  A child removed
   * event will be generated for each child removed this way.
   */
  public void removeAllChildren() {
    if(children == null) return;
    
    for(NamedObject child : this)
      removeChild(child);
  }
  
  /**
   * Removes this object from is parent and sets it as having no parent.
   */
  public void removeParent() {
    if(parent != null) parent.removeChild(this);
    
    parent = null;
  }
  
  /**
   * Adds a {@link ChildListener} to this {@link NamedObject} that will
   * be notified if a child is added or removed.
   * 
   * @param clist The {@link ChildListener} to add.
   */
  public void addChildListener(ChildListener clist) {
    childlist.add(clist);
  }
  
  /**
   * Removes a {@link ChildListener} from this {@link NamedObject}.
   * 
   * @param clist The {@link ChildListener} to remove.
   */
  public void removeChildListener(ChildListener clist) {
    childlist.remove(clist);
  }
  
  /**
   * Retrieves the {@link NamedObject} with the given name. This name can
   * be the full name of the object, or it can be relative.  For relative names
   * it checks to see if it is relative to this object then passes it to its
   * parent and so on until it reaches the root. An empty {@link String} will
   * return this {@link NamedObject}.
   * 
   * @param <T> The type of {@link NamedObject} to return.
   * @param name The name of the {@link NamedObject} to retrieve.
   * @return The object with the given name or null if no object can be found.
   */
  public <T extends NamedObject> T getObject(String name) {
    if(!name.contains(SEPARATOR)) return getObject(new String[]{name});
    return getObject(name.split(SEPARATOR, -1));
  }
  
  /**
   * Retrieves the {@link NamedObject} with the given name.  This name can
   * be the full name of the object, or it can be a relative path.  Each {@link String}
   * in the array will be treated as the name of an individual {@link NamedObject}
   * on the path.  For relative names it checks this {@link NamedObject}
   * for a child with the given name, the moves on to the parent of {@link NamedObject}
   * and so on in this fashion.  An empty array will simply return this
   * {@link NamedObject}.
   * 
   * @param <T> The type of the {@link NamedObject} to return.
   * @param path A array of names for the path to the desired object.
   * @return The desired {@link NamedObject} or null if it could not be found.
   */
  @SuppressWarnings("unchecked")
  public <T extends NamedObject> T getObject(String ... path) {
    if(path.length == 0) try {
      return (T) this;
    } catch(ClassCastException ex) {
      return null;
    }
    
    T obj =  (T) getObject(path, 0);
    
    if(obj != null) return obj;
    else if(parent != null) return parent.getObject(path);
    else if(path[0].equals(name)) return (T) getObject(path, 1);
    else return null;
  }
  
  /**
   * Executes a method from a {@link NamedObject}.  The method is found by
   * first getting the {@link NamedObject} using everything before the last
   * separator in the name.  The remaining string beyond the last separator is
   * the name of the desired method.
   * 
   * @param name The full name for the desired method in this {@link NamedObject}'s
   *  naming tree.
   * @param params The parameters to use in executing the desired method.
   * @return The return value if any from the executed method.
   * @throws NoSuchMethodException Thrown if the method cannot be found.
   * @throws IllegalAccessException Thrown if the permission checks fail.
   * @throws IllegalArgumentException Thrown if one of the arguments given is invalid.
   * @throws InvocationTargetException Thrown if there is an {@link Exception}
   *  encountered while executing the desired method.
   */
  public Object executeMethod(String name, Object ... params) throws
    NoSuchMethodException,
    IllegalAccessException,
    IllegalArgumentException,
    InvocationTargetException {
    
    StringTokenizer token = new StringTokenizer(name, 
      SEPARATOR,
      false);
    
    String[] path = new String[token.countTokens()];
    
    int i = 0;
    while(token.hasMoreTokens()) {
      path[i] = token.nextToken();
      i++;
    }
    
    return executeMethod(path, params);
  }
  
  /**
   * Executes a method from a {@link NamedObject}.  The method is found by
   * first getting the {@link NamedObject} using all but the last element of
   * the path to traverse the naming tree.  The last element is used as the name
   * of the desired method.
   * 
   * @param path The names used to traverse the naming tree followed by the
   * name of the method.
   * @param params The parameters to use in executing the desired method.
   * @return The return value if any from the executed method.
   * @throws NoSuchMethodException Thrown if the method cannot be found.
   * @throws IllegalAccessException Thrown if the permission checks fail.
   * @throws IllegalArgumentException Thrown if one of the arguments given is invalid.
   * @throws InvocationTargetException Thrown if there is an {@link Exception}
   *  encountered while executing the desired method.
   */
  public Object executeMethod(String[] path, Object ... params) throws 
    NoSuchMethodException, 
    IllegalAccessException, 
    IllegalArgumentException, 
    InvocationTargetException {
    
    Class<?>[] paramtypes = new Class<?>[params.length];
    for(int i = 0; i < paramtypes.length; i++) {
      paramtypes[i] = params[i].getClass();
    }
    
    NamedObjectMethod meth = getMethod(path, paramtypes);
    if(meth == null)
      throw new NoSuchMethodException(); //TODO: add detail message.
    
    return meth.invoke(params);
  }
  
  /**
   * Retrieves a {@link NamedObjectMethod} from the registration path with the given
   * signature.  The last name in the path is the name of the method
   * while the rest of the path is the name of the {@link NamedObject}
   * that will be used in {@link RegisteredObject#getObject(java.lang.String) }
   * to retrieve the {@link NamedObject} containing the desired {@link Method}.
   * 
   * @param name the name of the desired {@link Method} with the name of the
   * containing {@link NamedObject} and the {@link RegisteredObject#SEPARATOR}
   * prepended.
   * @param paramtypes The {@link Class}es for the parameters of the desired
   * {@link Method}.
   * @return The method with the given signature or null if no such method
   * can be found.
   */
  public NamedObjectMethod getMethod(String name, Class<?> ... paramtypes) {
    if(!name.contains(SEPARATOR)) return getMethod(new String[]{name}, paramtypes);
    return getMethod(name.split(SEPARATOR), paramtypes);
  }
  
  /**
   * Retrieves a {@link NamedObjectMethod} from the registration path with the given
   * signature.  The last {@link String} in the array is the name of the method
   * while the rest of the array is the name of the {@link Registered} that will be used
   * in {@link RegisteredObject#getObject(java.lang.String[]) } to retrieve the
   * {@link NamedObject} containing the desired {@link Method}.
   * 
   * @param path An array of names for the path to the array, the last name being
   * the simple name of the desired {@link Method}.
   * @param paramtypes The {@link Class}es for the parameters of the desired
   * {@link Method}.
   * @return The method with the given signature or null if no such method
   * can be found.
   */
  public NamedObjectMethod getMethod(String[] path, Class<?> ... paramtypes) {    
    String[] objpath = new String[path.length - 1];
    System.arraycopy(path, 0, objpath, 0, objpath.length);
    
    NamedObject obj = getObject(objpath);
    if(obj == null) return null;
    
    try {
      //look for a method with the exact signature.
      Method meth = obj.getClass().getMethod(path[path.length - 1], paramtypes);
      return new NamedObjectMethod(meth, obj);
      
    } catch(NoSuchMethodException ex) {
      //look for methods that use super types of the given parameters.
      for(Method meth : obj.getClass().getMethods()) {
        Class<?>[] pts = meth.getParameterTypes();
        if(pts.length != paramtypes.length) continue;
        
        boolean works = true;
        for(int i = 0; i < pts.length; i++) {
          if(!pts[i].isAssignableFrom(paramtypes[i])) {
            works = false;
            break;
          }
        }
        
        if(works) {
          return new NamedObjectMethod(meth, obj);
        }
      }
      
      //nothing was found.
      return null;
    }
  }
  
  @Override
  public String toString() {
    return getFullName();
  }

  /**
   * This returns an unmodifiable iterator over the children of this {@link NamedObject}.
   * The removal and registering methods should be used instead of this method
   * for adding or removing children.
   * 
   * @return 
   */
  @Override
  public Iterator<NamedObject> iterator() {
    return getChildren().iterator();
  }
  
  /**
   * Checks to see if the names of this {@link NamedObject} and its children
   * are the same as the given {@link NamedObject}.
   * 
   * @param obj The object to check.
   * @return Whether both {@link NamedObject}s are the root of identical naming
   * trees.
   */
  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof NamedObject)) return false;
    
    NamedObject other = (NamedObject)obj;
    if(!other.name.equals(name)) return false;
    for(NamedObject o : other)
      if(!children.containsKey(o.name) ||
        !children.get(o.name).equals(o))
        return false;
    
    return true;
  }
  
  /**
   * Called when this {@link NamedObject} is attached to registration tree.
   * 
   * @param parent The parent of this {@link NamedObject} in the tree.
   */
  protected void attached(NamedObject parent) {}
  
  /**
   * Called when this {@link NamedObject} is removed from its registration tree.
   * 
   * @param parent The {@link NamedObject} that was the parent of this one.
   */
  protected void dettached(NamedObject parent) {}
  
  private transient NamedObject parent;
  private Map<String, NamedObject> children;
  private transient Collection<ChildListener> childlist;
  
  private static final long serialVersionUID = 100L;
  
  private void readObject(ObjectInputStream input) throws
    ClassNotFoundException,
    IOException {
    input.defaultReadObject();
    
    //get any listeners.
    Iterable<ChildListener> lists = (Iterable<ChildListener>) input.readObject();
    for(ChildListener list : lists)
      addChildListener(list);
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
    
    //collect all of the 
    Collection<ChildListener> seriallist = new ArrayList<>();
    for(ChildListener list : childlist)
      if(list instanceof Serializable)
        seriallist.add(list);
    
    out.writeObject(seriallist);
  }
  
  private NamedObject getObject(String[] path, int index) {
    if(path.length <= index || children == null) return null;
    NamedObject obj;
    
    obj = children.get(path[index]);
    if(obj == null) return null;
    else if(index == path.length - 1) return obj;
    
    obj = obj.getObject(path, index + 1);
    if(obj != null) return obj;
    else return null;
  }
  
  private boolean checkName(String name) {
    if(name == null) return false;
    if(name.isEmpty()) return false;
    
    return NAME_REGEX.matcher(name).matches();
  }
  
  private void fireChildAdded(NamedObject child) {
    for(ChildListener list : childlist)
      list.childAdded(this, child);
    
    child.attached(this);
  }
  
  private void fireChildRemoved(NamedObject child) {
    for(ChildListener list : childlist)
      list.childRemoved(this, child);
    
    child.dettached(this);
  }
  
  private static final Pattern NAME_REGEX = Pattern.compile("[^\n./\\\\]+?$",
    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
  
  private static final String locprefix = NamedObject.class.getName().toLowerCase();
  
  private static final String CREATION = locprefix + ".created";
  private static final String EXISTING_PARENT = locprefix + ".existing_parent";
  private static final String ALREADY_ADDED = locprefix + ".already_added";
  private static final String ADDED_CHILD = locprefix + ".added_child";
  private static final String REMOVED_CHILD = locprefix + ".removed_child";
  private static final String WRONG_CHILD_CLASS = locprefix + ".wrong_child_class";
  private static final String ILLEGAL_NAME = locprefix + ".illegal_name";
  private static final String GET_METHOD = locprefix + ".get_method";
  private static final String EXECUTE_METHOD = locprefix + ".execute_method";
  
  private static final Logger log = Logger.getLogger(NamedObject.class.getName(),
    System.getProperty("taiga.code.logging.text"));
}
