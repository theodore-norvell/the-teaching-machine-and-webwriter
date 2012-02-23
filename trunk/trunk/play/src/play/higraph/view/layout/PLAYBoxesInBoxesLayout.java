/**
 * PLAYBoxesInBoxesLayout.java - play.higraph.view.layout - PLAY
 *
 * Created on 2012-2-15 by Kai Zhu
 */
package play.higraph.view.layout;

import play.higraph.model.PLAYEdge;
import play.higraph.model.PLAYEdgeLabel;
import play.higraph.model.PLAYHigraph;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYWholeGraph;
import higraph.view.layout.NestedTreeLayoutManager;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYBoxesInBoxesLayout
	extends
	NestedTreeLayoutManager<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> {

    public PLAYBoxesInBoxesLayout() {
	super(NestedTreeLayoutManager.Axis.Y);
    }

    public PLAYBoxesInBoxesLayout getThis() {
	return this;
    }

}
