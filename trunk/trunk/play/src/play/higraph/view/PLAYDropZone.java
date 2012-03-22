/**
 * PLAYDropZone.java - play.higraph.view - PLAY
 * 
 * Created on 2012-02-29 by Kai Zhu
 */
package play.higraph.view;

import higraph.view.DropZone;
import higraph.view.NodeView;

import java.awt.BasicStroke;
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
	super.strokeVar.set(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
		BasicStroke.JOIN_MITER, 10.0f, new float[] { 1.0f }, 0.0f));
	super.setColor(Color.RED);
	super.setFillColor(null);
	this.setVisibility(true);
    }

}
