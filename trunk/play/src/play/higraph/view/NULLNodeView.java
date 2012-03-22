/**
 * NULLNodeView.java - play.higraph.view - PLAY
 * 
 * Created on 2012-03-21 by Kai Zhu
 */
package play.higraph.view;

import higraph.view.HigraphView;

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
public class NULLNodeView extends PLAYNodeView {

    /**
     * @param v
     * @param node
     * @param timeMan
     */
    public NULLNodeView(
	    HigraphView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> v,
	    PLAYNode node, BTTimeManager timeMan) {
	super(v, node, timeMan);
	super.setNodeSize(80, 20);
	super.setColor(Color.GREEN);
	super.setFillColor(null);
	super.label.setTheLabel("null");
	super.label.setShow(true);
    }

}
