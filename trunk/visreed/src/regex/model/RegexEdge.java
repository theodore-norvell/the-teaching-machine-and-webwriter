/**
 * RegexEdge.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo (MUN# 200982155)
 * This file was prepared by Xiaoyu Guo. It was completed by me alone.
 */
package regex.model;

import higraph.model.abstractClasses.AbstractEdge;

/**
 * RegexEdges are not used in this project.
 * @author Xiaoyu Guo
 */
public class RegexEdge
extends
AbstractEdge<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> {

    protected RegexEdge(
        RegexNode source, 
        RegexNode target,
        RegexEdgeLabel label,
        RegexWholeGraph higraph
    ){
        super(
            source,
            target,
            label,
            higraph);
    }

    @Override
    protected RegexEdge getThis() {
        return this;
    }

}
