/*
 * Copyright (C) 2014 Russell Smith.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package taiga.code.util;

import java.text.MessageFormat;
import taiga.code.registration.NamedObject;

/**
 * A simple way to store data as a tree of {@link gpvm.registry.RegisteredObject}s.
 * 
 * @author russell
 */
public class DataNode extends NamedObject {
  /**
   * The data contained in this node.  Is there is no data directly associated
   * with this {@link DataNode} then this field will be null indicating that
   * any data will be stored in the children of this {@link DataNode}.
   */
  public Object data;

  /**
   * Creates a new {@link DataNode} with the given name.
   * 
   * @param name The name for the new {@link DataNode}.
   */
  public DataNode(String name) {
    super(name);
  }
  
  /**
   * Returns the value of the child with the given name, or null if there
   * is not a child with that name or the child does not contain data.
   * 
   * @param <T> The type of data that is expected.
   * @param name The name of the child to get the value from.
   * @return The data from the child with the given name.
   */
  public <T> T getValueByName(String ... name) {
    DataNode data = getDataNode(name);
    
    if(data == null || data.data == null) return null;
    else try {
      return (T) data.data;
    } catch(ClassCastException ex) {}
    
    return null;
  }
  
  /**
   * Similar to the {@link #getObject(java.lang.String) } method but only
   * looks through the children and does not search through the parent.
   * 
   * @param names The names of the desired value.
   * @return The desired value.
   */
  public DataNode getDataNode(String ... names) {
    if(names.length == 1) {
      String[] temp = names[0].split(SEPARATOR);
      if(temp.length != 0) names = temp;
    }
    
    return getDataNode(names, 0);
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public String toString() {
    return MessageFormat.format("{0}:{1}", name, data);
  }
  
  private DataNode getDataNode(String[] names, int index) {
    if(index == names.length) return this;
    
    DataNode node = getChild(names[index]);
    if(node == null) return null;
    return node.getDataNode(names, index + 1);
  }
}
