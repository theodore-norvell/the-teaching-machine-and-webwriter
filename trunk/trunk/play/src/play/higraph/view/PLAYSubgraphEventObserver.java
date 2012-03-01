/**
 * PLAYSubgraphEventObserver.java - play.higraph.view - PLAY
 *
 * Created on 2012-2-15 by Kai Zhu
 */
package play.higraph.view;

import higraph.view.ComponentView;
import higraph.view.interfaces.SubgraphEventObserver;

import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Stack;

import javax.swing.TransferHandler;

import demo.model.DemoNode;
import demo.model.DemoPayload;
import demo.model.DemoTag;

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

    private PLAYNodeView selectedView;;

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
	    if (this.selectedView == null) {
		if (stack.lastElement() instanceof PLAYNodeView) {
		    this.selectedView = (PLAYNodeView) stack.lastElement();
		    this.selectedView.setFillColor(Color.LIGHT_GRAY);
		    this.higraphView.refresh();
		    this.higraphView.getDisplay().repaint();
		}
	    } else {
		this.selectedView.setFillColor(null);
		this.higraphView.refresh();
		this.higraphView.getDisplay().repaint();
		this.selectedView = null;
	    }
	} else {
	    this.selectedView = null;
	}
	System.out.println("selectedView is " + this.selectedView);
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
	if (this.selectedView == null)
	    result = TransferHandler.NONE;
	else
	    result = TransferHandler.COPY_OR_MOVE;
	System.out.println("...returns " + result);
	return result;
    }

    public Transferable createTransferable() {
	Transferable result;
	System.out.println("createTransferable");
	if (this.selectedView == null)
	    result = null;
	else {
	    result = new PLAYViewTransferObject(this.selectedView);
	}
	System.out.println("...returns " + result);
	return result;
    }

    public boolean canDropHere(
	    Stack<ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>> stack,
	    TransferHandler.TransferSupport supportObj) {
	System.out.println("canDropHere()");
	PLAYTag tag = null;
	PLAYNodeView nodeView = null;
	boolean result = false;

	if (!supportObj
		.isDataFlavorSupported(PLAYViewTransferObject.TAG_DATAFLAVOR)
		&& !supportObj
			.isDataFlavorSupported(PLAYViewTransferObject.NODEVIEW_DATAFLAVOR)) {
	    System.out.println("No support Data Flavor");
	    return false;
	}

	try {
	    Object object = supportObj.getTransferable().getTransferData(
		    PLAYViewTransferObject.TAG_DATAFLAVOR);
	    if (object != null) {
		tag = (PLAYTag) object;
		if (stack.isEmpty()) {
		    return true;
		}
	    } else {
		object = supportObj.getTransferable().getTransferData(
			PLAYViewTransferObject.NODEVIEW_DATAFLAVOR);
		if (object != null) {
		    nodeView = (PLAYNodeView) object;
		    if (stack.isEmpty()) {
			return nodeView.getNode().canDetach();
		    }
		}
	    }
	} catch (UnsupportedFlavorException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	if ((tag == null) && (nodeView == null)) {
	    result = false;
	} else if ((nodeView != null) && (tag == null)) {
	    if (stack.lastElement() instanceof PLAYDropZone) {
		// TODO
	    } else if (stack.lastElement() instanceof PLAYNodeView) {
		PLAYNodeView parentNodeView = (PLAYNodeView) stack
			.lastElement();
		PLAYNode parentNode = parentNodeView.getNode();
		if (supportObj.getDropAction() == TransferHandler.COPY) {
		}
	    } else {
		result = false;
	    }
	} else {
	    if (stack.lastElement() instanceof PLAYDropZone) {
		// TODO
	    } else if (stack.lastElement() instanceof PLAYNodeView) {
		PLAYNodeView parentNodeView = (PLAYNodeView) stack
			.lastElement();
		PLAYNode parentNode = parentNodeView.getNode();
		result = parentNode.canReplacePayload(tag.defaultPayload());
	    } else {
		result = false;
	    }
	}

	if (this.selectedView != null && stack.contains(this.selectedView))
	    result = false;
	else if (supportObj.getDropAction() == java.awt.dnd.DnDConstants.ACTION_NONE)
	    result = false;
	else {
	    for (DataFlavor f : supportObj.getDataFlavors()) {
		System.out.println("...data flavor is " + f);
	    }
	    result = supportObj
		    .isDataFlavorSupported(PLAYViewTransferObject.TAG_DATAFLAVOR);
	}
	System.out.println("...returns " + result);
	return result;
    }

    public boolean importData(
	    Stack<ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge>> stack,
	    TransferHandler.TransferSupport support) {
	System.out.println("Import data");
	PLAYTag tag = null;
	PLAYNodeView nodeView = null;
	if (!canDropHere(stack, support)) {
	    System.out.println("... returns false");
	    return false;
	}

	try {
	    Object object = support.getTransferable().getTransferData(
		    PLAYViewTransferObject.TAG_DATAFLAVOR);
	    if (object != null) {
		tag = (PLAYTag) object;
		if (support.isDrop()) {
		    if (stack.isEmpty()) {
			this.wholeGraph.makeRootNode(new PLAYPayload(tag
				.toString(), tag));
			this.higraphView.refresh();
			this.higraphView.getDisplay().repaint();
		    } else if (stack.lastElement() instanceof PLAYDropZone) {

		    } else if (stack.lastElement() instanceof PLAYNodeView) {
			PLAYNodeView currentNodeView = (PLAYNodeView) stack
				.lastElement();
			PLAYNode currentNode = currentNodeView.getNode();
			PLAYNode newNode = this.wholeGraph
				.makeRootNode(new PLAYPayload(tag.toString(),
					tag));
			if (currentNode.canInsertChild(0, newNode)) {
			    currentNode.insertChild(0, newNode);
			} else if (currentNode.canReplace(newNode)) {
			    currentNode.replace(newNode);
			}
			this.higraphView.refresh();
			this.higraphView.getDisplay().repaint();
		    }
		} else {
		    return false;
		}
	    } else {
		object = support.getTransferable().getTransferData(
			PLAYViewTransferObject.NODEVIEW_DATAFLAVOR);
		if (object != null) {
		    nodeView = (PLAYNodeView) object;
		    PLAYNode node = nodeView.getNode();
		    // TODO
		}
	    }
	} catch (UnsupportedFlavorException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	System.out.println("... returns true");
	return true;
    }

}
