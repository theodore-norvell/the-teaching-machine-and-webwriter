/**
 * PLAYHigraphView.java - play.higraph.view - PLAY
 * 
 * Created on Feb 14, 2012 by Kai Zhu
 */
package play.higraph.view;

import higraph.swing.HigraphJComponent;
import higraph.view.HigraphView;
import higraph.view.ViewFactory;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

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
public class PLAYHigraphView
	extends
	HigraphView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> {

    private List<PLAYNodeView> deletedNodeViewList;

    protected PLAYHigraphView(
	    ViewFactory<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> viewFactory,
	    PLAYHigraph theGraph, Component display, BTTimeManager timeMan) {
	super(viewFactory, theGraph, display, timeMan);
	this.deletedNodeViewList = new ArrayList<>();
    }

    /**
     * @return the deletedNodeViewList
     */
    public List<PLAYNodeView> getDeletedNodeViewList() {
	return deletedNodeViewList;
    }

    public void refresh() {
	HigraphJComponent playHigraphJComponent = (HigraphJComponent) super
		.getDisplay();
	playHigraphJComponent.refresh();
	// Dimension dimension = playHigraphJComponent.getPreferredSize();
	// playHigraphJComponent.scrollRectToVisible(new Rectangle(0,
	// dimension.height, 100, 100));
	this.getDisplay().revalidate();
    }

    /**
     * @return the deletedNodeView
     */
    public PLAYNodeView getDeletedNodeView() {
	PLAYNodeView nodeView = null;
	if (!this.deletedNodeViewList.isEmpty()) {
	    nodeView = this.deletedNodeViewList.get(0);
	    this.deletedNodeViewList.remove(0);
	}
	return nodeView;
    }

    /**
     * @param deletedNodeView
     *            the deletedNodeView to set
     */
    public void setDeletedNodeView(PLAYNodeView deletedNodeView) {
	this.deletedNodeViewList.add(0, deletedNodeView);
    }

}
