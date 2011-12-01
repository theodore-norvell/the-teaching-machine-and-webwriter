/**
 * IGraphContainer.java
 * 
 * @date: Aug 8, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view;

import visreed.model.VisreedHigraph;

/**
 * @author Xiaoyu Guo
 */
public interface IGraphContainer {
    /**
     * Re paint the graph, without any layout and modification of the model.
     * This operation will not expect every view (e.g. text form) to be the same
     * as the model.
     */
    void repaint();
    
    /**
     * Layout the graph, and re-paint it. This operation will expect every view 
     * shows the latest state of the model.
     */
    void refreshGraph();
    
    /**
     * Sets the subgraph for main display area(s).
     * @param subgraph
     */
    void setSubgraph(VisreedHigraph subgraph);
    
    /**
     * Gets the current displaying subgraph
     * @return
     */
    VisreedHigraph getCurrentSubgraph();
}
