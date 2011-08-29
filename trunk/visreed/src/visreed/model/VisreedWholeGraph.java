/**
 * VisreedWholeGraph.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo (MUN# 200982155)
 * This file was prepared by Xiaoyu Guo. It was completed by me alone.
 */
package visreed.model;

import java.util.ArrayList;
import java.util.List;

import higraph.model.abstractClasses.AbstractWholeGraph;
import tm.backtrack.BTTimeManager;
import visreed.extension.regex.model.RegexNode;

/**
 * @author Xiaoyu Guo
 *
 */
public class VisreedWholeGraph
extends AbstractWholeGraph<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>
implements VisreedHigraph{

    public VisreedWholeGraph(BTTimeManager timeMan) {
        super(timeMan);
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
        return new VisreedSubgraph(this) ;
    }

    @Override
    public VisreedWholeGraph getWholeGraph() {
        return this;
    }
    
    /* Handles selection */
    private VisreedSubgraph selectedNodesGraph;
    private List<VisreedNode> selectedNodesList;
    
    public void deSelectAll(){
        for(VisreedNode selected : selectedNodesGraph.getNodes()){
            selected.deSelect();
        }
        this.selectedNodesGraph.clear();
        this.selectedNodesList.clear();
    }
    
    public void select(VisreedNode node){
        this.deSelectAll();
        addToSelection(node);
    }
    
    public void select(List<VisreedNode> nodes){
        this.deSelectAll();
        addToSelection(nodes);
    }
    
    public void addToSelection(VisreedNode node){
        if(node != null){
            node.select();
            selectedNodesGraph.addTop(node);
            selectedNodesList.add(node);
        }
    }
    
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
    }
    
    public void addToSelection(List<VisreedNode> nodes){
        if(nodes == null){
            return;
        }
        for(VisreedNode selected : nodes){
            this.addToSelection(selected);
        }
    }
    
    public List<VisreedNode> getSelectionNodes(){
        return this.selectedNodesList;
    }
    
    public VisreedSubgraph getSelectionSubgraph(){
        return this.selectedNodesGraph;
    }

    
    /* observer pattern */
    private ArrayList<IVisreedHigraphObserver> observers = new ArrayList<IVisreedHigraphObserver>();
    
    /**
     * Notify the observers that the content of the subgraph is changed.
     */
    public void notifyObservers(){
        if(this.observers == null || this.observers.size() == 0){
            return;
        }
        for(IVisreedHigraphObserver o : this.observers){
            o.changed(this);
        }
    }
    
    /* (non-Javadoc)
     * @see visreed.model.VisreedHigraph#registerObserver(visreed.model.IVisreedHigraphObserver)
     */
    public void registerObserver(IVisreedHigraphObserver o){
        if(o != null && !this.observers.contains(o)){
            this.observers.add(o);
        }
    }
    /* (non-Javadoc)
     * @see visreed.model.VisreedHigraph#deRegisterObserver(visreed.model.IVisreedHigraphObserver)
     */
    public void deRegisterObserver(IVisreedHigraphObserver o){
        if(o != null && this.observers.contains(o)){
            this.observers.remove(o);
        }
    }
}
