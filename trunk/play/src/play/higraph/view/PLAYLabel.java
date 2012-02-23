/**
 * PLAYLabel.java - play.higraph.view - PLAY
 * 
 * Created on Feb 16, 2012 by Kai Zhu
 */
package play.higraph.view;

import higraph.view.ComponentView;
import higraph.view.Label;
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
public class PLAYLabel
	extends
	Label<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> {

    /**
     * @param id
     * @param position
     * @param cv
     * @param timeMan
     */
    public PLAYLabel(
	    String id,
	    int position,
	    ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> cv,
	    BTTimeManager timeMan) {
	super(id, position, cv, timeMan);
    }

}
