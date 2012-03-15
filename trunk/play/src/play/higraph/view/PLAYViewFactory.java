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
	if (PLAYTag.PLACEHOLDER.defaultPayload().toString()
		.equals(node.getPayload().toString())) {
	    nodeView = new PlaceHolderNodeView(higraphView, node, super.timeMan);
	} else if (PLAYTag.ASSIGN.defaultPayload().toString()
		.equals(node.getPayload().toString())) {
	    nodeView = this.makeASSIGNNodeView(higraphView, node);
	} else if (PLAYTag.SEQ.defaultPayload().toString()
		.equals(node.getPayload().toString())) {
	    nodeView = this.makeSEQNodeView(higraphView, node);
	} else if (PLAYTag.IF.defaultPayload().toString()
		.equals(node.getPayload().toString())) {
	    nodeView = this.makeIFNodeView(higraphView, node);
	} else if (PLAYTag.WHILE.defaultPayload().toString()
		.equals(node.getPayload().toString())) {
	    nodeView = this.makeWHILENodeView(higraphView, node);
	} else {
	    nodeView = new PLAYNodeView(higraphView, node, super.timeMan);
	}
	return nodeView;
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
	PLAYNode ifNode = ifNodeView.getNode();
	// add a place holder child node
	PLAYNode expPlaceHolderNode = ifNode.getWholeGraph().makeRootNode(
		new PLAYPayload(PLAYTag.PLACEHOLDER.toString(),
			PLAYTag.PLACEHOLDER));
	ifNode.insertChild(0, expPlaceHolderNode);
	// add a seq child node for then branch
	PLAYNode thenSeqNode = ifNode.getWholeGraph().makeRootNode(
		new PLAYPayload(PLAYTag.SEQ.toString(), PLAYTag.SEQ));
	ifNode.insertChild(1, thenSeqNode);
	// add a seq child node for else branch
	PLAYNode elseSeqNode = ifNode.getWholeGraph().makeRootNode(
		new PLAYPayload(PLAYTag.SEQ.toString(), PLAYTag.SEQ));
	ifNode.insertChild(2, elseSeqNode);

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
	PLAYNode whileNode = whileNodeView.getNode();
	// add a place holder child node
	PLAYNode expPlaceHolderNode = whileNode.getWholeGraph().makeRootNode(
		new PLAYPayload(PLAYTag.PLACEHOLDER.toString(),
			PLAYTag.PLACEHOLDER));
	whileNode.insertChild(0, expPlaceHolderNode);
	// add a seq child node for do branch
	PLAYNode doSeqNode = whileNode.getWholeGraph().makeRootNode(
		new PLAYPayload(PLAYTag.SEQ.toString(), PLAYTag.SEQ));
	whileNode.insertChild(1, doSeqNode);
	// add a seq child node for else branch
	PLAYNode elseSeqNode = whileNode.getWholeGraph().makeRootNode(
		new PLAYPayload(PLAYTag.SEQ.toString(), PLAYTag.SEQ));
	whileNode.insertChild(2, elseSeqNode);

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
	PLAYNode assignNode = assignNodeView.getNode();
	// add left exp node
	PLAYNode leftExpNode = assignNode.getWholeGraph().makeRootNode(
		new PLAYPayload(PLAYTag.EXP.toString(), PLAYTag.EXP));
	assignNode.insertChild(0, leftExpNode);
	// add right exp node
	PLAYNode rightExpNode = assignNode.getWholeGraph().makeRootNode(
		new PLAYPayload(PLAYTag.EXP.toString(), PLAYTag.EXP));
	assignNode.insertChild(1, rightExpNode);

	assignNodeView.setLayoutManager(new ASSIGNNodeViewLayout());

	return assignNodeView;
    }

}
