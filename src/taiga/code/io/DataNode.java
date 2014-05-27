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
  public Object data;

  public DataNode(String name) {
    super(name);
  }
  
  public String toString() {
    return MessageFormat.format("{0}:{1}", name, data);
  }
}
