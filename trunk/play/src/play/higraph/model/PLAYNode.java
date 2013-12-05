/**
 * PLAYNode.java - play.higraph.model - PLAY
 * 
 * Created on Feb 13, 2012 by Kai Zhu
 */
package play.higraph.model;

import play.higraph.view.PLAYNodeView;
import higraph.model.abstractTaggedClasses.AbstractTaggedNode;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYNode
	extends
	AbstractTaggedNode<PLAYTag,PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> {

	private PLAYNodeView view;
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

    public void setView(PLAYNodeView view){
    this.view = view;	
    }
    
    public PLAYNodeView getView(){
    	return this.view;
    }
}
