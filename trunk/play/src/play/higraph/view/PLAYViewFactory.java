/**
 * PLAYViewFactory.java - play.higraph.view - PLAY
 * 
 * Created on Feb 14, 2012 by Kai Zhu
 */
package play.higraph.view;

import higraph.view.ComponentView;
import higraph.view.HigraphView;
import higraph.view.NodeView;
import higraph.view.ViewFactory;

import java.awt.Component;

import play.higraph.model.PLAYEdge;
import play.higraph.model.PLAYEdgeLabel;
import play.higraph.model.PLAYHigraph;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYTag;
import play.higraph.model.PLAYWholeGraph;
import play.higraph.view.layout.ASSIGNNodeViewLayout;
import play.higraph.view.layout.IFNodeViewLayout;
import play.higraph.view.layout.SEQNodeViewLayout;
import play.higraph.view.layout.SIGNNodeViewLayout;
import play.higraph.view.layout.WHILENodeViewLayout;
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
	PLAYNodeView nodeView = null;
	if (PLAYTag.PLACEHOLDER.defaultPayload().getTag()
		.equals(node.getPayload().getTag())) {
	    nodeView = this.makePlaceHolderNodeView(higraphView, node);
	} else if (PLAYTag.ASSIGN.defaultPayload().getTag()
		.equals(node.getPayload().getTag())) {
	    nodeView = this.makeASSIGNNodeView(higraphView, node);
	} else if (PLAYTag.SEQ.defaultPayload().getTag()
		.equals(node.getPayload().getTag())) {
	    nodeView = this.makeSEQNodeView(higraphView, node);
	} else if (PLAYTag.IF.defaultPayload().getTag()
		.equals(node.getPayload().getTag())) {
	    nodeView = this.makeIFNodeView(higraphView, node);
	} else if (PLAYTag.WHILE.defaultPayload().getTag()
		.equals(node.getPayload().getTag())) {
	    nodeView = this.makeWHILENodeView(higraphView, node);
	} else if (PLAYTag.NUMBER.defaultPayload().getTag()
		.equals(node.getPayload().getTag())) {
	    nodeView = this.makeNUMNodeView(higraphView, node);
	} else if (PLAYTag.BOOLEAN.defaultPayload().getTag()
		.equals(node.getPayload().getTag())) {
	    nodeView = this.makeBOONodeView(higraphView, node);
	} else if (PLAYTag.STRING.defaultPayload().getTag()
		.equals(node.getPayload().getTag())) {
	    nodeView = this.makeSTRINGNodeView(higraphView, node);
	} else if (PLAYTag.NULL.defaultPayload().getTag()
		.equals(node.getPayload().getTag())) {
	    nodeView = this.makeNULLNodeView(higraphView, node);
	} else if (PLAYTag.VAR.defaultPayload().getTag()
		.equals(node.getPayload().getTag())) {
	    nodeView = this.makeVARNodeView(higraphView, node);
	} else if (PLAYTag.PLUS.defaultPayload().getTag()
		.equals(node.getPayload().getTag())) {
	    nodeView = this.makePLUSNodeView(higraphView, node);
	} else if (PLAYTag.MINUS.defaultPayload().getTag()
		.equals(node.getPayload().getTag())) {
	    nodeView = this.makeMINUSNodeView(higraphView, node);
	} else if (PLAYTag.MULTIPLICATION.defaultPayload().getTag()
		.equals(node.getPayload().getTag())) {
	    nodeView = this.makeMULTIPLICATIONNodeView(higraphView, node);
	} else if (PLAYTag.DIVISION.defaultPayload().getTag()
		.equals(node.getPayload().getTag())) {
	    nodeView = this.makeDIVISIONNodeView(higraphView, node);
	} else {
	    nodeView = new PLAYNodeView(higraphView, node, super.timeMan);
	}
	return nodeView;
    }

    /**
     * @param higraphView
     * @param node
     * @return
     */
    private PLAYNodeView makePlaceHolderNodeView(PLAYHigraphView higraphView,
	    PLAYNode node) {
	PLAYNodeView placeHolderNodeView = new PlaceHolderNodeView(higraphView,
		node, super.timeMan);
	return placeHolderNodeView;
    }

    /**
     * @param higraphView
     * @param node
     * @return
     */
    private PLAYNodeView makeBOONodeView(PLAYHigraphView higraphView,
	    PLAYNode node) {
	PLAYNodeView boolNodeView = new BOOLNodeView(higraphView, node,
		super.timeMan);
	return boolNodeView;
    }

    /**
     * @param higraphView
     * @param node
     * @return
     */
    private PLAYNodeView makeVARNodeView(PLAYHigraphView higraphView,
	    PLAYNode node) {
	PLAYNodeView varNodeView = new VARNodeView(higraphView, node,
		super.timeMan);
	return varNodeView;
    }

    /**
     * @param higraphView
     * @param node
     * @return
     */
    private PLAYNodeView makeSTRINGNodeView(PLAYHigraphView higraphView,
	    PLAYNode node) {
	PLAYNodeView stringNodeView = new STRINGNodeView(higraphView, node,
		super.timeMan);
	return stringNodeView;
    }

    /**
     * @param higraphView
     * @param node
     * @return
     */
    private PLAYNodeView makeNULLNodeView(PLAYHigraphView higraphView,
	    PLAYNode node) {
	PLAYNodeView nullNodeView = new NULLNodeView(higraphView, node,
		super.timeMan);
	return nullNodeView;
    }

    /**
     * @param higraphView
     * @param node
     * @return
     */
    private PLAYNodeView makeNUMNodeView(PLAYHigraphView higraphView,
	    PLAYNode node) {
	PLAYNodeView numNodeView = new NUMNodeView(higraphView, node,
		super.timeMan);
	return numNodeView;
    }

    /**
     * @see higraph.view.ViewFactory#makeDropZone(higraph.view.NodeView)
     */
    @Override
    public PLAYDropZone makeDropZone(
	    NodeView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> nv) {
	PLAYDropZone dropZone = new PLAYDropZone(nv, super.timeMan);
	nv.addZone(dropZone);
	return dropZone;
    }

    /**
     * @see higraph.view.ViewFactory#makeLabel(higraph.view.ComponentView,
     *      java.lang.String, int)
     */
    @Override
    public PLAYLabel makeLabel(
	    ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> cv,
	    String id, int position) {
	return new PLAYLabel(id, position, cv, super.timeMan);
    }

    /**
     * Create a if node view
     * 
     * @param higraphView
     * @param node
     * @return
     */
    private PLAYNodeView makeIFNodeView(PLAYHigraphView higraphView,
	    PLAYNode node) {
	PLAYNodeView ifNodeView = new IFNodeView(higraphView, node,
		super.timeMan);
	ifNodeView.setLayoutManager(new IFNodeViewLayout());
	return ifNodeView;
    }

    /**
     * Create a while node view
     * 
     * @param higraphView
     * @param node
     * @return
     */
    private PLAYNodeView makeWHILENodeView(PLAYHigraphView higraphView,
	    PLAYNode node) {
	PLAYNodeView whileNodeView = new WHILENodeView(higraphView, node,
		super.timeMan);
	whileNodeView.setLayoutManager(new WHILENodeViewLayout());
	return whileNodeView;
    }

    /**
     * Create a seq node view
     * 
     * @param higraphView
     * @param node
     * @return
     */
    private PLAYNodeView makeSEQNodeView(PLAYHigraphView higraphView,
	    PLAYNode node) {
	PLAYNodeView seqNodeView = new SEQNodeView(higraphView, node,
		super.timeMan);
	seqNodeView.setLayoutManager(new SEQNodeViewLayout());
	return seqNodeView;
    }

    /**
     * Create a assign node view
     * 
     * @param higraphView
     * @param node
     * @return
     */
    private PLAYNodeView makeASSIGNNodeView(PLAYHigraphView higraphView,
	    PLAYNode node) {
	PLAYNodeView assignNodeView = new ASSIGNNodeView(higraphView, node,
		super.timeMan);
	assignNodeView.setLayoutManager(new ASSIGNNodeViewLayout());
	return assignNodeView;
    }

    /**
     * Create a plus node view
     * 
     * @param higraphView
     * @param node
     * @return
     */
    private PLAYNodeView makePLUSNodeView(PLAYHigraphView higraphView,
	    PLAYNode node) {
	PLAYNodeView plusNodeView = new PLUSNodeView(higraphView, node,
		super.timeMan);
	plusNodeView.setLayoutManager(new SIGNNodeViewLayout());
	return plusNodeView;
    }

    /**
     * Create a minus node view
     * 
     * @param higraphView
     * @param node
     * @return
     */
    private PLAYNodeView makeMINUSNodeView(PLAYHigraphView higraphView,
	    PLAYNode node) {
	MINUSNodeView minusNodeView = new MINUSNodeView(higraphView, node,
		super.timeMan);
	minusNodeView.setLayoutManager(new SIGNNodeViewLayout());
	return minusNodeView;
    }

    /**
     * Create a multiplication node view
     * 
     * @param higraphView
     * @param node
     * @return
     */
    private PLAYNodeView makeMULTIPLICATIONNodeView(
	    PLAYHigraphView higraphView, PLAYNode node) {
	MULTIPLICATIONNodeView multiplicationNodeView = new MULTIPLICATIONNodeView(
		higraphView, node, super.timeMan);
	multiplicationNodeView.setLayoutManager(new SIGNNodeViewLayout());
	return multiplicationNodeView;
    }

    /**
     * Create a division node view
     * 
     * @param higraphView
     * @param node
     * @return
     */
    private PLAYNodeView makeDIVISIONNodeView(PLAYHigraphView higraphView,
	    PLAYNode node) {
	DIVISIONNodeView divisionNodeView = new DIVISIONNodeView(higraphView,
		node, super.timeMan);
	divisionNodeView.setLayoutManager(new SIGNNodeViewLayout());
	return divisionNodeView;
    }

}
