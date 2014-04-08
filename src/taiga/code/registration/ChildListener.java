/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.registration;

/**
 * A listener for children of {@link RegisteredObject}s.  This can be added to
 * a {@link RegisteredObject} to be notified when a child is added or removed,
 * or alternatively to be notified when it is added or removed from a parent.
 * @author russell
 */
public interface ChildListener {
  /**
   * Called when a child is added to the {@link RegisteredObject}.
   * 
   * @param parent The parent that the child is added to.
   * @param child The child that was added.
   */
  public void childAdded(RegisteredObject parent, RegisteredObject child);
  
  /**
   * Called when a child is removed from a {@link RegisteredObject}.
   * 
   * @param parent The {@link RegisteredObject} that had a child removed.
   * @param child The {@link RegisteredObject} that was removed.
   */
  public void childRemoved(RegisteredObject parent, RegisteredObject child);
}
