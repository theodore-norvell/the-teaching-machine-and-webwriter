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
 * The root node is the {@link VisreedHigraph} itself.
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
		TreeModelEvent e = new TreeModelEvent(this, this.higraph.getTops().toArray());
		for(TreeModelListener tml : this.listeners){
			tml.treeNodesChanged(e);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	@Override
	public Object getChild(Object arg0, int arg1) {
		if(arg0 == null){
			return null;
		}
		if(arg0 instanceof VisreedHigraph){
			VisreedHigraph higraph = (VisreedHigraph)arg0;
			List<VisreedNode> tops = higraph.getTops();
			if(arg1 >= 0 && arg1 < tops.size()){
				return tops.get(arg1);
			}
		} else if(arg0 instanceof VisreedNode){
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
		if(arg0 == null){
			return 0;
		}
		if(arg0 instanceof VisreedHigraph){
			return ((VisreedHigraph)arg0).getTops().size();
		} else if(arg0 instanceof VisreedNode){
			return ((VisreedNode)arg0).getNumberOfChildren();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int getIndexOfChild(Object arg0, Object arg1) {
		if(arg0 == null || arg1 == null){
			return -1;
		}
		if(arg0 instanceof VisreedHigraph && arg1 instanceof VisreedNode){
			List<VisreedNode> tops = ((VisreedHigraph)arg0).getTops();
			int result = -1;
			for(int i = 0; i < tops.size(); i++){
				if(tops.get(i).equals(arg1)){
					result = i;
					break;
				}
			}
			return result;
		} else if(arg0 instanceof VisreedNode && arg1 instanceof VisreedNode){
			if(((VisreedNode)arg1).getParent() == arg0){
				return ((VisreedNode)arg1).getOrder();
			}
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	@Override
	public Object getRoot() {
		return this.higraph;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	@Override
	public boolean isLeaf(Object arg0) {
		if(arg0 == null){
			return true;
		}
		if(arg0 instanceof VisreedHigraph){
			return ((VisreedHigraph)arg0).getTops().size() == 0;
		} else if(arg0 instanceof VisreedNode){
			return ((VisreedNode)arg0).getNumberOfChildren() == 0;
		}
		return false;
	}

}
