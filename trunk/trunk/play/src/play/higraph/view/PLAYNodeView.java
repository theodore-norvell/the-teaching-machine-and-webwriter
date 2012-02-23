/**
 * PLAYNodeView.java - play.higraph.view - PLAY
 * 
 * Created on Feb 14, 2012 by Kai Zhu
 */
package play.higraph.view;

import higraph.view.HigraphView;
import higraph.view.NodeView;

import java.awt.Color;

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
public class PLAYNodeView
	extends
	NodeView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> {

    public PLAYNodeView(
	    HigraphView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> v,
	    PLAYNode node, BTTimeManager timeMan) {
	super(v, node, timeMan);
	super.setColor(Color.BLUE);
	super.setFillColor(null);
	super.setNodeShapeType(ROUND_RECTANGLE);
	PLAYLabel label = new PLAYLabel(node.getPayload().toString(), 0, this,
		timeMan);
	setColor(Color.BLACK);
	label.setTheLabel(node.getPayload().toString());
	super.addLabel(label);
    }

}
