/**
 * NodeViewSelectionModel.java - play.higraph.view.model - PLAY
 * 
 * Created on 2012-06-20 by Kai Zhu
 */
package play.higraph.view.model;

import java.util.ArrayList;
import java.util.List;

import play.higraph.model.PLAYNode;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYNodeView;

/**
 * @author Kai Zhu
 * 
 */
public class NodeViewSelectionModel {

    private PLAYNodeView lastHoverNodeView;

    private PLAYNodeView selectedNodeView;

    private List<PLAYNodeView> selectedNodwViewList;

    private int anchor;

    private int focus;

    public NodeViewSelectionModel() {
	this.lastHoverNodeView = null;
	this.selectedNodeView = null;
	this.selectedNodwViewList = new ArrayList<PLAYNodeView>();
	this.anchor = 0;
	this.focus = 0;
    }

    /**
     * @return the lastHoverNodeView
     */
    public PLAYNodeView getLastHoverNodeView() {
	return lastHoverNodeView;
    }

    /**
     * @param lastHoverNodeView
     *            the lastHoverNodeView to set
     */
    public void setLastHoverNodeView(PLAYNodeView lastHoverNodeView) {
	this.lastHoverNodeView = lastHoverNodeView;
    }

    /**
     * @return the selectedNodeView
     */
    public PLAYNodeView getSelectedNodeView() {
	return this.selectedNodeView;
    }

    /**
     * @param selectedNodeView
     *            the selectedNodeView to set
     */
    public void setSelectedNodeView(PLAYNodeView selectedNodeView) {
	this.selectedNodeView = selectedNodeView;
    }

    /**
     * @return the anchor
     */
    public int getAnchor() {
	return this.anchor;
    }

    /**
     * @param anchor
     *            the anchor to set
     */
    public void setAnchor(int anchor) {
	this.anchor = anchor;
    }

    /**
     * @return the focus
     */
    public int getFocus() {
	return this.focus;
    }

    /**
     * @param focus
     *            the focus to set
     */
    public void setFocus(int focus) {
	this.focus = focus;
    }

    public List<PLAYNodeView> getNextNodeViewList(PLAYHigraphView higraphView) {
	this.selectedNodwViewList.add(this.getNextNodeView(higraphView));
	return this.selectedNodwViewList;
    }

    public PLAYNodeView getNextNodeView(PLAYHigraphView higraphView) {
	PLAYNode currentNode = this.selectedNodeView.getNode();
	PLAYNode parentNode = currentNode.getParent();
	if (parentNode != null) {
	    int childrenNumber = parentNode.getNumberOfChildren();
	    if ((this.anchor + 1) < childrenNumber) {
		this.anchor += 1;
	    }
	    currentNode = parentNode.getChild(this.anchor);
	} else {
	    int position = 0;
	    List<PLAYNode> topsList = currentNode.getTops();
	    for (PLAYNode node : topsList) {
		if (node == currentNode) {
		    break;
		} else {
		    position++;
		}
	    }
	    int topsNumber = currentNode.getNumberOfTops();
	    if ((position + 1) < topsNumber) {
		position += 1;
	    }
	    currentNode = currentNode.getTop(position);
	}
	this.selectedNodeView = (PLAYNodeView) higraphView
		.getNodeView(currentNode);
	return this.selectedNodeView;
    }

    public PLAYNodeView getPreviousNodeView(PLAYHigraphView higraphView) {
	PLAYNode currentNode = this.selectedNodeView.getNode();
	PLAYNode parentNode = currentNode.getParent();
	if (parentNode != null) {
	    int childrenNumber = parentNode.getNumberOfChildren();
	    if (0 <= (this.anchor - 1)) {
		this.anchor -= 1;
	    }
	    currentNode = parentNode.getChild(this.anchor);
	} else {
	    int position = 0;
	    List<PLAYNode> topsList = currentNode.getTops();
	    for (PLAYNode node : topsList) {
		if (node == currentNode) {
		    break;
		} else {
		    position++;
		}
	    }
	    int topsNumber = currentNode.getNumberOfTops();
	    if (0 <= (position - 1)) {
		position -= 1;
	    }
	    currentNode = currentNode.getTop(position);
	}
	this.selectedNodeView = (PLAYNodeView) higraphView
		.getNodeView(currentNode);
	return this.selectedNodeView;
    }

}
