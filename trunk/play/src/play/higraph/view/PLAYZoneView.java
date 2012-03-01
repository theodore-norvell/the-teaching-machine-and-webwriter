/**
 * PLAYZoneView.java - play.higraph.view - PLAY
 * 
 * Created on 2012-02-29 by Kai Zhu
 */
package play.higraph.view;

import higraph.view.ComponentView;
import higraph.view.ZoneView;
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
public class PLAYZoneView
	extends
	ZoneView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> {

    /**
     * @param view
     * @param timeMan
     */
    protected PLAYZoneView(
	    ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> view,
	    BTTimeManager timeMan) {
	super(view, timeMan);
    }

    /**
     * @see higraph.view.ComponentView#getViewType()
     */
    @Override
    public String getViewType() {
	return null;
    }

}
