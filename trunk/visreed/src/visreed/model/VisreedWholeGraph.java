/**
 * VisreedWholeGraph.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo (MUN# 200982155)
 * This file was prepared by Xiaoyu Guo. It was completed by me alone.
 */
package visreed.model;

import higraph.model.abstractTaggedClasses.AbstractTaggedWholeGraph;

import java.util.ArrayList;
import java.util.List;

import tm.backtrack.BTTimeManager;
import visreed.pattern.IObserver;

/**
 * @author Xiaoyu Guo
 *
 */
public class VisreedWholeGraph
extends AbstractTaggedWholeGraph<VisreedTag, VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>
implements VisreedHigraph{
	
	protected List<VisreedSubgraph> subgraphs;

    public VisreedWholeGraph(BTTimeManager timeMan) {
        super(timeMan);
        this.subgraphs = new ArrayList<VisreedSubgraph>();
        this.selectedNodesGraph = constructSubGraph();
        this.selectedNodesList = new ArrayList<VisreedNode>();
    }

    @Override
    protected VisreedEdge constructEdge(
        VisreedNode source, 
        VisreedNode target,
        VisreedEdgeLabel label
    ) {
        return new VisreedEdge( source, target, label, this ) ;
    }

    @Override
    protected VisreedNode constructNode(
        VisreedWholeGraph higraph, 
        VisreedPayload payload
    ) {
        return new VisreedNode( higraph, payload ) ;
    }

    @Override
    protected VisreedNode constructNode(VisreedNode original, VisreedNode parent) {
        return new VisreedNode( original, parent ) ;
    }

    @Override
    protected VisreedSubgraph constructSubGraph() {
        VisreedSubgraph sg = new VisreedSubgraph(this) ;
        this.subgraphs.add(sg);
        return sg;
    }

    @Override
    public VisreedWholeGraph getWholeGraph() {
        return this;
    }
    
    public List<VisreedSubgraph> getAllSubgraph(){
    	return subgraphs;
    }
    
    /* Handles selection */
    private VisreedSubgraph selectedNodesGraph;
    private List<VisreedNode> selectedNodesList;
    
    /**
     * Cancel all the selected nodes
     */
    public void deSelectAll(){
        for(VisreedNode selected : selectedNodesGraph.getNodes()){
            selected.deSelect();
        }
        this.selectedNodesGraph.clear();
        this.selectedNodesList.clear();
    }
    
    /**
     * Select the specified one node
     * @param node
     */
    public void select(VisreedNode node){
        this.deSelectAll();
        addToSelection(node);
        this.notifyObservers();
    }
    
    /**
     * select the specified nodes
     * @param nodes
     */
    public void select(List<VisreedNode> nodes){
        this.deSelectAll();
        addToSelection(nodes);
    }
    
    /**
     * Add the specified node to the current selection
     * @param node
     */
    public void addToSelection(VisreedNode node){
        if(node != null){
            node.select();
            selectedNodesGraph.addTop(node);
            selectedNodesList.add(node);
        }
    }
    
    /**
     * Add the specified nodes to the current selection
     * @param nodes
     */
    public void addToSelection(List<VisreedNode> nodes){
        if(nodes == null){
            return;
        }
        for(VisreedNode selected : nodes){
            this.addToSelection(selected);
        }
        this.notifyObservers();
    }
    
    /**
     * Toggle the selection of the specified node
     * @param node
     */
    public void toggleSelection(VisreedNode node){
        if(node != null){
            if(node.isSelected()){
                node.deSelect();
                selectedNodesGraph.removeTop(node);
                selectedNodesList.remove(node);
            } else {
                this.addToSelection(node);
            }
        }
        this.notifyObservers();
    }
    
    /**
     * Reduce the current selection so that no selected nodes are nested.	
     */
    public void reduceSelection(){
    	for(int i = 0; i < this.selectedNodesList.size(); i++){
    		VisreedNode node = this.selectedNodesList.get(i);
    		VisreedNode currentParent = node.getParent();
    		while(currentParent != null){
    			if(currentParent.isSelected() == true){
    				// reduce current node
    				this.toggleSelection(node);
    				i--;
    				break;
    			}
    			currentParent = currentParent.getParent();
    		}
    	}
    }
    
    public List<VisreedNode> getSelectionNodes(){
        return this.selectedNodesList;
    }
    
    public VisreedSubgraph getSelectionSubgraph(){
        return this.selectedNodesGraph;
    }

    
    /* observer pattern */
    private ArrayList<IObserver<VisreedHigraph>> observers = new ArrayList<IObserver<VisreedHigraph>>();
    
    /**
     * Notify the observers that the content of the subgraph is changed.
     */
    public void notifyObservers(){
        if(this.observers == null || this.observers.size() == 0){
            return;
        }
        for(IObserver<VisreedHigraph> o : this.observers){
            o.changed(this);
        }
    }
    
    /* (non-Javadoc)
     * @see visreed.model.VisreedHigraph#registerObserver(visreed.model.IVisreedHigraphObserver)
     */
    public void registerObserver(IObserver<VisreedHigraph> o){
        if(o != null && !this.observers.contains(o)){
            this.observers.add(o);
        }
    }
    /* (non-Javadoc)
     * @see visreed.model.VisreedHigraph#deRegisterObserver(visreed.model.IVisreedHigraphObserver)
     */
    public void deRegisterObserver(IObserver<VisreedHigraph> o){
        if(o != null && this.observers.contains(o)){
            this.observers.remove(o);
        }
    }

	/**
	 * Clear all pre-created subgraphs and nodes
	 */
	public void clearAll() {
		this.subgraphs.clear();
	}
}
