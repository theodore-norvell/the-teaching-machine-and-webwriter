/**
 * PLAYSubgraphEventObserver.java - play.higraph.view - PLAY
 *
 * Created on 2012-2-15 by Kai Zhu
 */
package play.higraph.view;

import higraph.view.ComponentView;
import higraph.view.interfaces.SubgraphEventObserver;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Stack;

import javax.swing.TransferHandler;

import play.higraph.model.PLAYEdge;
import play.higraph.model.PLAYEdgeLabel;
import play.higraph.model.PLAYHigraph;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYTag;
import play.higraph.model.PLAYWholeGraph;
import play.higraph.swing.PLAYViewTransferObject;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYSubgraphEventObserver
	extends
	SubgraphEventObserver<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> {

    // TODO

    private ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> selectedView = null;

    private PLAYHigraphView higraphView;

    private PLAYWholeGraph wholeGraph;

    private PLAYViewFactory viewFactory;

    private PLAYSubgraph subgraph;

    /**
     * @param higraphView
     * @param wholeGraph
     * @param viewFactory
     * @param subgraph
     */
    public PLAYSubgraphEventObserver(PLAYHigraphView higraphView,
	    PLAYWholeGraph wholeGraph, PLAYViewFactory viewFactory,
	    PLAYSubgraph subgraph) {
	super();
	this.higraphView = higraphView;
	this.wholeGraph = wholeGraph;
	this.viewFactory = viewFactory;
	this.subgraph = subgraph;
    }

    /** Called when the mouse is being moved with no buttons down. */
    public void movedOver(
	    Stack<ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>> stack,
	    MouseEvent e) {
	System.out.println("Moved over");
    }

    /** Called when the mouse is being moved with one or more buttons down. */
    public void dragged(
	    Stack<ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>> stack,
	    MouseEvent e) {
	System.out.println("Dragged");
    }

    /** Called when the mouse button has been pressed and released. */
    public void clickedOn(
	    Stack<ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>> stack,
	    MouseEvent e) {
	System.out.println("Clicked on");
    }

    /** Called when a mouse button goes down. */
    public void pressedOn(
	    Stack<ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>> stack,
	    MouseEvent e) {
	System.out.println("Pressed on");
	if (stack != null && stack.size() > 0) {
	    selectedView = stack.firstElement();
	} else {
	    selectedView = null;
	}
	System.out.println("selectedView is " + selectedView);
    }

    /** Called when a mouse button goes up. */
    public void releasedOn(
	    Stack<ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>> stack,
	    MouseEvent e) {
	System.out.println("Release on");
    }

    public int getSourceActions() {
	System.out.println("getSourceActions()");
	int result;
	if (selectedView == null)
	    result = java.awt.dnd.DnDConstants.ACTION_NONE;
	else
	    result = java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE;
	System.out.println("...returns " + result);
	return result;
    }

    public Transferable createTransferable() {
	Transferable result;
	System.out.println("createTransferable");
	if (selectedView == null)
	    result = null;
	else
	    result = null;// new PLAYViewTransferObject(selectedView);
	System.out.println("...returns " + result);
	return result;
    }

    public boolean canDropHere(
	    Stack<ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>> stack,
	    TransferHandler.TransferSupport supportObj) {
	System.out.println("canDropHere()");
	PLAYNodeView nodeView = null;
	PLAYTag tag = null;
	boolean result = false;
	if (selectedView != null && stack.contains(selectedView))
	    result = false;
	else if (supportObj.getDropAction() == java.awt.dnd.DnDConstants.ACTION_NONE)
	    result = false;
	else {
	    for (DataFlavor f : supportObj.getDataFlavors()) {
		System.out.println("...data flavor is " + f);
	    }
	    result = supportObj
		    .isDataFlavorSupported(PLAYViewTransferObject.THE_TAG_DATAFLAVOR);
	}
	System.out.println("...returns " + result);
	return result;
    }

    public boolean importData(
	    Stack<ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>> stack,
	    TransferHandler.TransferSupport support) {
	System.out.println("Import data");
	if (!canDropHere(stack, support)) {
	    System.out.println("... returns false");
	    return false;
	}
	try {
	    PLAYTag tag = (PLAYTag) support.getTransferable().getTransferData(
		    PLAYViewTransferObject.THE_TAG_DATAFLAVOR);
	    wholeGraph.makeRootNode(new PLAYPayload(tag.toString(), tag));
	    higraphView.refresh();
	    this.higraphView.getDisplay().repaint();
	} catch (UnsupportedFlavorException | IOException e) {
	    e.printStackTrace();
	}
	System.out.println("... returns true");
	return true;
    }
}
