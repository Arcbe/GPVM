/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.io;

import java.text.MessageFormat;
import taiga.code.registration.RegisteredObject;

/**
 * A simple way to store data as a tree of {@link gpvm.registry.RegisteredObject}s.
 * 
 * @author russell
 */
public class DataNode extends RegisteredObject {
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
   * 
   * @return 
   */
  @Override
  public String toString() {
    return MessageFormat.format("{0}:{1}", name, data);
  }
}
