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

package taiga.code.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import taiga.code.registration.ChildListener;
import taiga.code.registration.NamedObject;

public final class NamingTreeViewer extends JTree {

  public NamingTreeViewer() {
    model = new NTModel();
    
    setModel(model);
    setRootVisible(true);
  }

  public NamingTreeViewer(NamedObject r) {
    this.model = new NTModel();
    
    setModel(model);
    setRootVisible(true);
    
    setRoot(r);
  }
  
  public void setRoot(NamedObject r) {
    if(root != null && root != r) {
      removeListeners(root);
      
      if(r != null)
        addListeners(r);
    }
    
    root = r;
    
    if(root != null) {
      model.rootChanged();
    }
  }
  
  private void addListeners(NamedObject obj) {
    obj.addChildListener(model);
    for(NamedObject o : obj)
      addListeners(o);
  }
  
  private void removeListeners(NamedObject obj) {
    obj.removeChildListener(model);
    for(NamedObject o : obj)
      removeListeners(o);
  }
  
  private final NTModel model;
  private NamedObject root;
  
  private class NTModel implements TreeModel, ChildListener {

    public NTModel() {
      this.lists = new HashSet<>();
    }
    
    public void rootChanged() {
      TreeModelEvent event = new TreeModelEvent(this, new Object[]{root});
      
      for(TreeModelListener list : lists)
        list.treeStructureChanged(event);
    }

    @Override
    public Object getRoot() {
      return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
      if(!(parent instanceof NamedObject)) return null;
      
      NamedObject obj = (NamedObject)parent;
      
      List<NamedObject> children = new ArrayList<>(obj.getChildren());
      Collections.sort(children);
      
      return children.get(index);
    }

    @Override
    public int getChildCount(Object parent) {
      if(!(parent instanceof NamedObject)) return -1;
      
      NamedObject obj = (NamedObject) parent;
      return obj.getChildren().size();
    }

    @Override
    public boolean isLeaf(Object node) {
      if(!(node instanceof NamedObject)) return false;
      
      NamedObject obj = (NamedObject) node;
      return obj.getChildren().isEmpty();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
      assert false;
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
      if(!(parent instanceof NamedObject)) return -1;
      
      NamedObject obj = (NamedObject)parent;
      
      List<NamedObject> children = new ArrayList<>(obj.getChildren());
      Collections.sort(children);
      
      return children.indexOf(child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
      lists.add(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
      lists.remove(l);
    }
    
    public void fireNodeAdded(NamedObject obj) {
      TreeModelEvent event;
      
      List<NamedObject> path = new ArrayList<>();
      while(obj != null) {
        path.add(obj);
        obj = obj.getParent();
      }

      Collections.reverse(path);

      event = new TreeModelEvent(this, path.toArray());
      
      for(TreeModelListener list : lists)
        list.treeNodesInserted(event);
    }
    
    public void fireNodeRemoved(NamedObject parent, NamedObject obj) {
      List<NamedObject> path = new ArrayList<>();
      path.add(obj);
      
      obj = parent;
      while(obj != null) {
        path.add(obj);
        obj = obj.getParent();
      }
      
      Collections.reverse(path);
      
      TreeModelEvent event = new TreeModelEvent(obj, path.toArray());
      
      for(TreeModelListener list : lists)
        list.treeNodesRemoved(event);
    }
    
    private final Collection<TreeModelListener> lists;

    @Override
    public void childAdded(NamedObject parent, NamedObject child) {
      fireNodeAdded(child);
      child.addChildListener(this);
    }

    @Override
    public void childRemoved(NamedObject parent, NamedObject child) {
      fireNodeRemoved(parent, child);
      child.removeChildListener(this);
    }
  }

  private static final String locprefix = NamingTreeViewer.class.getName().toLowerCase();

  private static final Logger log = Logger.getLogger(locprefix,
    System.getProperty("taiga.code.logging.text"));
}
