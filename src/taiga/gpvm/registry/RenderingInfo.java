package taiga.gpvm.registry;

import taiga.code.io.DataNode;

/**
 * Indicates that a class can be used to store rendering data for a specific
 * {@link Renderer} or {@link EntityRenderer}.  The implementing class must 
 * have a constructor that takes in a {@link DataNode} as an argument.  This 
 * {@link DataNode} will contain the value of the rendering-info field for a 
 * {@link RenderingEntry}, or null if no data is provided for the a given {@link RenderingEntry}
 * 
 * @author russell
 */
public interface RenderingInfo {
}
