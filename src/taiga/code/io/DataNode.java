/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package taiga.code.io;

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
}
