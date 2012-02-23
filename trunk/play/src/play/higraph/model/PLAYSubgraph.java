/**
 * PLAYSubgraph.java - play.higraph.model - PLAY
 * 
 * Created on Feb 13, 2012 by Kai Zhu
 */
package play.higraph.model;

import higraph.model.abstractClasses.AbstractSubgraph;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYSubgraph
	extends
	AbstractSubgraph<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>
	implements PLAYHigraph {

    public PLAYSubgraph(PLAYWholeGraph wholeGraph) {
	super(wholeGraph);
    }

    @Override
    protected PLAYSubgraph getThis() {
	return this;
    }

}
