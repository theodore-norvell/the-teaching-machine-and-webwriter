/**
 * PLAYDropZone.java - play.higraph.view - PLAY
 * 
 * Created on 2012-02-29 by Kai Zhu
 */
package play.higraph.view;

import higraph.view.DropZone;
import higraph.view.NodeView;
import play.higraph.model.PLAYEdge;
import play.higraph.model.PLAYEdgeLabel;
import play.higraph.model.PLAYHigraph;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYWholeGraph;
import tm.backtrack.BTTimeManager;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYDropZone
	extends
	DropZone<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> {

    /**
     * @param nv
     * @param timeMan
     */
    protected PLAYDropZone(
	    NodeView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> nv,
	    BTTimeManager timeMan) {
	super(nv, timeMan);
    }

}
