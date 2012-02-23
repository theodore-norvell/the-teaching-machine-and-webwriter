/**
 * PLAYEdge.java - play.higraph.model - PLAY
 * 
 * Created on Feb 13, 2012 by Kai Zhu
 */
package play.higraph.model;

import higraph.model.abstractClasses.AbstractEdge;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYEdge
	extends
	AbstractEdge<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> {

    protected PLAYEdge(PLAYNode source, PLAYNode target, PLAYEdgeLabel label,
	    PLAYWholeGraph wholeGraph) {
	super(source, target, label, wholeGraph);
    }

    @Override
    protected PLAYEdge getThis() {
	return this;
    }

}
