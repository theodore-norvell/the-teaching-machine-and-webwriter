/**
 * PLAYNode.java - play.higraph.model - PLAY
 * 
 * Created on Feb 13, 2012 by Kai Zhu
 */
package play.higraph.model;

import higraph.model.abstractTaggedClasses.AbstractTaggedNode;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYNode
	extends
	AbstractTaggedNode<PLAYTag,PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> {

    public PLAYNode(PLAYWholeGraph wholeGraph, PLAYPayload payload) {
	super(wholeGraph, payload);
    }

    protected PLAYNode(PLAYNode original, PLAYNode parent) {
	super(original, parent);
    }

    @Override
    protected PLAYNode getThis() {
	return this;
    }

}
