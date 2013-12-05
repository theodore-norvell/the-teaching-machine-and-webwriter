/**
 * PLAYViewSelectionModel.java - play.higraph.view.model - PLAY
 * 
 * Created on 2012-06-20 by Kai Zhu
 */
package play.higraph.view.model;

import java.util.ArrayList;
import java.util.List;

import play.higraph.model.PLAYNode;
import play.higraph.view.PLAYDropZone;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYNodeView;
import play.higraph.view.PLAYZoneView;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYViewSelectionModel {

	private int anchor=0;

	private int focus=0;

	private PLAYNodeView nv;

	private boolean flag;


	public PLAYViewSelectionModel() {
		//this.selectedViewList = new ArrayList<PLAYNodeView>();
		this.anchor = 0;
		this.focus = 0;
		this.flag = false;
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

	public void clearSelectedNodeViewList() {
		this.nv = null;
		this.anchor = 0;
		this.focus = 0;
	}

	public List<PLAYNodeView> getSelectedViewList(PLAYHigraphView higraphView) {

		List<PLAYNodeView> selectedViewList = new ArrayList<PLAYNodeView>();
		PLAYNode currentChild = null;
		PLAYNode parentNode = null;
		selectedViewList.clear();

		//System.out.println("anchor = "+anchor+ " focus = "+focus);

		if(getParentNodeView() != null){
			//System.out.println("parentnotnull");
			selectedViewList.clear();
			parentNode = getParentNodeView().getNode();

			int childrenNumber = parentNode.getNumberOfChildren();
			//System.out.println("no of children = " + childrenNumber);

			if(childrenNumber == 0){

				selectedViewList.add(getParentNodeView());
			}
			else
			{
				if(anchor==focus && anchor < childrenNumber){
					currentChild = parentNode.getChild(anchor);
					selectedViewList.add((PLAYNodeView)higraphView.getNodeView(currentChild));

				}
				else if((anchor < focus) &&(focus < childrenNumber)){
					System.out.println("anchor = "+anchor+" focus = "+focus);
					for(int i = anchor; i<=focus;i++){
						currentChild = parentNode.getChild(i);
						selectedViewList.add((PLAYNodeView)higraphView.getNodeView(currentChild));
					}
				}
			}
		}
		return selectedViewList;
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

	public void setFlag(boolean flag){
		this.flag = flag;
	}
	
	public boolean getFlag(){
		return flag;
	}
}
