/**
 * VisreedEdge.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo (MUN# 200982155)
 * This file was prepared by Xiaoyu Guo. It was completed by me alone.
 */
package visreed.model;

import visreed.model.payload.VisreedPayload;
import higraph.model.abstractClasses.AbstractEdge;

/**
 * RegexEdges are not used in this project.
 * @author Xiaoyu Guo
 */
public class VisreedEdge
extends
AbstractEdge<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> {

    protected VisreedEdge(
        VisreedNode source, 
        VisreedNode target,
        VisreedEdgeLabel label,
        VisreedWholeGraph higraph
    ){
        super(source, target, label, higraph);
    }

    /* (non-Javadoc)
     * @see higraph.model.abstractClasses.AbstractEdge#getThis()
     */
    @Override
    protected VisreedEdge getThis() {
        return this;
    }

}
