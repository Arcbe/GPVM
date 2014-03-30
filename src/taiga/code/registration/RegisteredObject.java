/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.registration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@link RegisteredObject}s form a tree of objects within a program.  This tree
 * can then be traversed and find an object given the full name or relative name
 * from any object within the tree.
 * 
 * @author russell
 */
public class RegisteredObject implements Iterable<RegisteredObject>{
  public static final String SEPERATOR = ".";
  
  public final String name;
  private RegisteredObject parent;
  private int id;
  
  private List<RegisteredObject> children;

  /**
   * Creates a new {@link RegisteredObject} with the given name.  Each {@link
   * RegisteredObject} requires a name, so the argument must not be null or
   * an empty {@link String}.
   * 
   * @param name The name for the new {@link RegisteredObject}
   */
  public RegisteredObject(String name) {
    assert name != null;
    assert !name.equals("");
    
    this.name = name;
    
    log.log(Level.FINER, CREATION, name);
  }
  
  /**
   * The id for the {@link RegisteredObject} used by its parent. If this {@link RegisteredObject}
   * does not have a parent then the returned id is undefined.
   * 
   * @return The current id.
   */
  public int getID() {
    if(parent == null)
      log.log(Level.WARNING, NO_PARENT, name);
    
    return id;
  }
  
  /**
   * Returns the full name of this object including all parents.  Each name will
   * be appended to previous separated by a '.' in order from root to this
   * {@link RegisteredObject}.  This name can be used to access this object from
   * any other object within the same tree of {@link RegisteredObject}s.
   * 
   * @return The full name of this object.
   */
  public String getFullName() {
    if(parent == null) return name;
    else return parent.getFullName().concat(SEPERATOR).concat(name);
  }
  
  /**
   * Returns the parent of this {@link RegisteredObject} if any.
   * 
   * @return The parent of this object.
   */
  public RegisteredObject getParent() {
    return parent;
  }
  
  /**
   * Returns an immutable list of the registered children.  Some of the entries
   * may be null if children have been removed.  The index of the children corresponds
   * to their IDs.
   * 
   * @return 
   */
  public List<RegisteredObject> getChildren() {
    if(children == null)
      children = new ArrayList<>();
    
    return Collections.unmodifiableList(children);
  }
  
  /**
   * Adds a child to this {@link RegisteredObject}.  The child will be added to
   * the first available slot in this object and can be index by the id returned
   * by this method.
   * 
   * @param child The child to add.
   * @return The id of the child within this {@link RegisteredObject}.
   * @throws NullPointerException Thrown if the child is null.
   */
  public int addChild(RegisteredObject child) {
    if(child == null) throw new NullPointerException();
    
    //check to make sure that this child does not already have a parent. kidnapping is
    //not allowed
    if(child.getParent() != null) {
      log.log(Level.WARNING, EXISTING_PARENT, new Object[]{getFullName(), child.name});
      
      return -1;
    }
    
    if(children == null) {
      children = new ArrayList<>();
      children.add(child);
      
      child.id = 0;
    } else {
      //make sure that the child is not already added.  No cloning either.
      if(children.indexOf(child) != -1) {
        log.log(Level.WARNING, ALREADY_ADDED, new Object[]{getFullName(), child.name});
        
        return -1;
      }
      
      //children are added in the first available slot.
      child.id = children.indexOf(null);
      
      if(child.id == -1) {
        child.id = children.size();
        children.add(child);
      } else {
        children.set(child.id, child);
      }
    }
    
    log.log(Level.FINE, ADDED_CHILD, new Object[]{getFullName(), child.name});
    return child.id;
  }
  
  /**
   * Removes a child from this {@link RegisteredObject}.
   * 
   * @param child The child to remove.
   * @return Whether the removal was successful.
   */
  public boolean removeChild(RegisteredObject child) {
    if(children == null) return false;
    
    log.log(Level.FINE, REMOVED_CHILD, new Object[]{getFullName(), child.name});
    
    return children.remove(child);
  }
  
  public void removeALlChildren() {
    if(children == null) return;
    
    children.clear();
  }
  
  /**
   * Removes this object from is parent and sets it as having no parent.
   */
  public void removeParent() {
    if(parent != null) parent.removeChild(this);
    
    parent = null;
  }

  /**
   * This returns an unmodifiable iterator over the children of this {@link RegisteredObject}.
   * The removal and registering methods should be used instead of this method
   * for adding or removing children.
   * 
   * @return 
   */
  @Override
  public Iterator<RegisteredObject> iterator() {
    return getChildren().listIterator();
  }
  
  private static final String CREATION = RegisteredObject.class.getName().toLowerCase() + ".created";
  private static final String NO_PARENT = RegisteredObject.class.getName().toLowerCase() + ".no_parent";
  private static final String EXISTING_PARENT = RegisteredObject.class.getName().toLowerCase() + ".existing_parent";
  private static final String ALREADY_ADDED = RegisteredObject.class.getName().toLowerCase() + ".already_added";
  private static final String ADDED_CHILD = RegisteredObject.class.getName().toLowerCase() + ".added_child";
  private static final String REMOVED_CHILD = RegisteredObject.class.getName().toLowerCase() + ".removed_child";
  
  private static final Logger log = Logger.getLogger(RegisteredObject.class.getName(),
    System.getProperty("taiga.code.logging.text"));
}
