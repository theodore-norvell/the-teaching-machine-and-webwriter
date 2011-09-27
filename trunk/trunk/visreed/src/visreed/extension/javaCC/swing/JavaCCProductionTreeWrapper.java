/**
 * JavaCCProductionWrapper.java
 * 
 * @date: Sep 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import visreed.extension.javaCC.model.JavaCCWholeGraph;
import visreed.extension.javaCC.model.payload.ProductionPayload;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedWholeGraph;
import visreed.pattern.IObserver;

/**
 * JavaCCProductionTreeWrapper displays all the productions in a tree view.
 * The root should be the JavaCCWholeGraph, and all the nodes are VisreedNode with
 * Payload of ProductionPayload.
 * @author Xiaoyu Guo
 */
public class JavaCCProductionTreeWrapper
implements TreeModel, IObserver<VisreedHigraph> {

	private JavaCCWholeGraph higraph;

	public VisreedWholeGraph getWholeGraph() {
		return higraph;
	}
	
	private List<VisreedNode> cache = new ArrayList<VisreedNode>();
	
	protected List<VisreedNode> getAllNodes(){
		return this.cache;
	}
	
	private List<TreeModelListener> listeners;
	
	public JavaCCProductionTreeWrapper(JavaCCWholeGraph higraph) {
		this.higraph = higraph;
		higraph.registerObserver(this);
		this.listeners = new ArrayList<TreeModelListener>();
	}

	/* (non-Javadoc)
	 * @see visreed.pattern.IObserver#changed(visreed.pattern.IObservable)
	 */
	@Override
	public void changed(VisreedHigraph object) {
		this.cache.clear();
		this.cache.addAll(this.higraph.getProductionManager().getAllProductions());
		Collections.sort(this.cache, new Comparator<VisreedNode>(){
			@Override
			public int compare(VisreedNode o1, VisreedNode o2) {
				try{
					ProductionPayload p1 = ((ProductionPayload)o1.getPayload());
					ProductionPayload p2 = ((ProductionPayload)o2.getPayload());
					return p1.getName().compareTo(p2.getName());
				} catch (Exception ex){
					return 0;
				}
			}
		});
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
		if(arg0 != null && arg0 instanceof JavaCCWholeGraph && arg0 == this.higraph){
			return this.cache.get(arg1);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	@Override
	public int getChildCount(Object arg0) {
		if(arg0 != null && arg0 instanceof JavaCCWholeGraph){
			return ((JavaCCWholeGraph)arg0).getProductionManager().getAllProductions().size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if(parent != null && parent instanceof VisreedNode && child != null && child instanceof JavaCCWholeGraph){
			if(((VisreedNode)child).getWholeGraph() == parent){
				return ((VisreedNode)child).getOrder();
			}
		}
		return 0;
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
		if(arg0 != null && arg0 instanceof VisreedNode){
			return true;
		}
		return false;
	}

}
