/**
 * VisreedTreeWrapper.java
 * 
 * @date: Sep 20, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing.TreeView;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedWholeGraph;
import visreed.pattern.IObserver;

/**
 * VisreedOutlineTreeWrapper converts a {@link VisreedHigraph} to the tree view
 * @author Xiaoyu Guo
 */
public class VisreedOutlineTreeWrapper
implements TreeModel, IObserver<VisreedHigraph> {

	private VisreedHigraph higraph;

	public VisreedHigraph getHigraph() {
		return higraph;
	}
	
	private List<TreeModelListener> listeners;
	
	public VisreedOutlineTreeWrapper(VisreedWholeGraph higraph) {
		this.higraph = higraph;
		higraph.registerObserver(this);
		this.listeners = new ArrayList<TreeModelListener>();
	}

	/* (non-Javadoc)
	 * @see visreed.pattern.IObserver#changed(visreed.pattern.IObservable)
	 */
	@Override
	public void changed(VisreedHigraph object) {
		TreeModelEvent e = new TreeModelEvent(this, this.higraph.getTops().toArray());
		for(TreeModelListener tml : this.listeners){
			tml.treeStructureChanged(e);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	@Override
	public void addTreeModelListener(TreeModelListener arg0) {
		if(arg0 != null && !this.listeners.contains(arg0)){
			this.listeners.add(arg0);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	@Override
	public void removeTreeModelListener(TreeModelListener arg0) {
		if(arg0 != null && this.listeners.contains(arg0)){
			this.listeners.remove(arg0);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
	 */
	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	@Override
	public Object getChild(Object arg0, int arg1) {
		if(arg0 != null && arg0 instanceof VisreedNode){
			VisreedNode node = (VisreedNode)arg0;
			if(node.getNumberOfChildren() >= arg1){
				return node.getChild(arg1);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	@Override
	public int getChildCount(Object arg0) {
		if(arg0 != null && arg0 instanceof VisreedNode){
			return ((VisreedNode)arg0).getNumberOfChildren();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int getIndexOfChild(Object arg0, Object arg1) {
		if(arg0 != null && arg0 instanceof VisreedNode && arg1 != null && arg1 instanceof VisreedNode){
			if(((VisreedNode)arg1).getParent() == arg0){
				return ((VisreedNode)arg1).getOrder();
			}
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	@Override
	public Object getRoot() {
		List<VisreedNode> tops = this.higraph.getTops();
		if(tops != null && tops.size() > 0){
			return tops.get(0);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	@Override
	public boolean isLeaf(Object arg0) {
		if(arg0 != null && arg0 instanceof VisreedNode){
			return ((VisreedNode)arg0).getNumberOfChildren() == 0;
		}
		return false;
	}

}
