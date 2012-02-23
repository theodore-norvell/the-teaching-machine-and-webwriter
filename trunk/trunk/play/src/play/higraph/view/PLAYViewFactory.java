/**
 * PLAYViewFactory.java - play.higraph.view - PLAY
 * 
 * Created on Feb 14, 2012 by Kai Zhu
 */
package play.higraph.view;

import higraph.view.HigraphView;
import higraph.view.ViewFactory;

import java.awt.Component;

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
public class PLAYViewFactory
	extends
	ViewFactory<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> {

    public PLAYViewFactory(BTTimeManager tm) {
	super(tm);
    }

    @Override
    public PLAYHigraphView makeHigraphView(PLAYHigraph hg, Component display) {
	return new PLAYHigraphView(this, hg, display, super.timeMan);
    }

    @Override
    public PLAYNodeView makeNodeView(
	    HigraphView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> hgv,
	    PLAYNode node) {
	PLAYHigraphView higraphView = (PLAYHigraphView) hgv;
	return new PLAYNodeView(higraphView, node, super.timeMan);
    }

}
