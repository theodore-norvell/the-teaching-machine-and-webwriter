/**
 * PLAYViewSelectionModel.java - play.higraph.view.model - PLAY
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
public class PLAYViewSelectionModel {

	private List<PLAYNodeView> selectedViewList;

	private int anchor;

	private int focus;
	
	private PLAYNodeView nv;
	

	public PLAYViewSelectionModel() {
		this.selectedViewList = new ArrayList<PLAYNodeView>();
		this.anchor = 0;
		this.focus = 0;
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
		if (anchor < 0) {
			anchor = 0;
		}
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
		if (focus < 0) {
			focus = 0;
		}
		this.focus = focus;
	}

	public void addSelectedView(PLAYNodeView view) {
		this.selectedViewList.add(view);
	}

	public void clearSelectedNodeViewList() {
		this.selectedViewList.clear();
		this.anchor = 0;
		this.focus = 0;
	}

	public List<PLAYNodeView> getSelectedViewList() {
		return this.selectedViewList;
	}

	public void setSelectedViewList(PLAYHigraphView higraphView) {
		PLAYNode currentNode = ((PLAYNodeView) this.selectedViewList
				.get(this.selectedViewList.size() - 1)).getNode();
		PLAYNode parentNode = currentNode.getParent();
		if (parentNode != null) {
			int childrenNumber = parentNode.getNumberOfChildren();
			if (this.anchor < childrenNumber) {
				currentNode = parentNode.getChild(this.anchor);
			} else {
				if (currentNode.getChildren().isEmpty()) {
					currentNode = parentNode;
					this.anchor = 0;
					this.focus = 0;
				} else {
					currentNode = currentNode.getChild(0);
					this.anchor = 0;
					this.focus = 0;
				}
			}
		} else {
			currentNode = currentNode.getChild(0);
			this.anchor = 0;
			this.focus = 0;
		}
		PLAYNodeView nodeview = (PLAYNodeView) higraphView
				.getNodeView(currentNode);
		this.selectedViewList.clear();
		this.selectedViewList.add(nodeview);
	}

	public void setMultipleSelectedVideList(PLAYHigraphView higraphView) {
		PLAYNode currentNode = ((PLAYNodeView) this.selectedViewList
				.get(this.selectedViewList.size() - 1)).getNode();
		PLAYNode parentNode = currentNode.getParent();
		if (parentNode != null) {
			int childrenNumber = parentNode.getNumberOfChildren();
			if (this.focus < childrenNumber) {
				currentNode = parentNode.getChild(this.focus);
			} else {
				if (currentNode.getChildren().isEmpty()) {
					currentNode = parentNode;
					this.anchor = 0;
					this.focus = 0;
				} else {
					currentNode = currentNode.getChild(0);
					this.anchor = 0;
					this.focus = 0;
				}
			}
		} else {
			currentNode = currentNode.getChild(0);
			this.anchor = 0;
			this.focus = 0;
		}
		PLAYNodeView nodeview = (PLAYNodeView) higraphView
				.getNodeView(currentNode);
		this.selectedViewList.add(nodeview);
	}
	
	public void setParentNodeView(PLAYNodeView nv){
		this.nv = nv;
	}
	
	public PLAYNodeView getParentNodeView(){
		return nv;
	}
	
	public boolean isUnderSelection(PLAYNodeView nv){
		return true;
	}
	
	public List<PLAYNodeView> getSelection(){
		return selectedViewList;
	}

}
