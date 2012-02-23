/**
 * PLAYWholeGraph.java - play.higraph.model - PLAY
 * 
 * Created on Feb 13, 2012 by Kai Zhu
 */
package play.higraph.model;

import tm.backtrack.BTTimeManager;
import higraph.model.abstractClasses.AbstractWholeGraph;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYWholeGraph
	extends
	AbstractWholeGraph<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>
	implements PLAYHigraph {

    public PLAYWholeGraph(BTTimeManager timeMan) {
	super(timeMan);
    }

    @Override
    public PLAYWholeGraph getWholeGraph() {
	return this;
    }

    @Override
    protected PLAYNode constructNode(PLAYWholeGraph higraph, PLAYPayload payload) {
	return new PLAYNode(higraph, payload);
    }

    @Override
    protected PLAYNode constructNode(PLAYNode original, PLAYNode parent) {
	return new PLAYNode(original, parent);
    }

    @Override
    protected PLAYEdge constructEdge(PLAYNode source, PLAYNode target,
	    PLAYEdgeLabel label) {
	return new PLAYEdge(source, target, label, this);
    }

    @Override
    protected PLAYSubgraph constructSubGraph() {
	return new PLAYSubgraph(this);
    }

}
