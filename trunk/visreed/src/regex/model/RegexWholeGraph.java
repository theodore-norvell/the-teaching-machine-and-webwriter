/**
 * RegexWholeGraph.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo (MUN# 200982155)
 * This file was prepared by Xiaoyu Guo. It was completed by me alone.
 */
package regex.model;

import java.util.ArrayList;
import java.util.List;

import higraph.model.abstractClasses.AbstractWholeGraph;
import tm.backtrack.BTTimeManager;

/**
 * @author Xiaoyu Guo
 *
 */
public class RegexWholeGraph
extends AbstractWholeGraph<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge>
implements RegexHigraph{

    public RegexWholeGraph(BTTimeManager timeMan) {
        super(timeMan);
        this.selectedNodesGraph = constructSubGraph();
        this.selectedNodesList = new ArrayList<RegexNode>();
    }

    @Override
    protected RegexEdge constructEdge(
        RegexNode source, 
        RegexNode target,
        RegexEdgeLabel label
    ) {
        return new RegexEdge( source, target, label, this ) ;
    }

    @Override
    protected RegexNode constructNode(
        RegexWholeGraph higraph, 
        RegexPayload payload
    ) {
        return new RegexNode( higraph, payload ) ;
    }

    @Override
    protected RegexNode constructNode(RegexNode original, RegexNode parent) {
        return new RegexNode( original, parent ) ;
    }

    @Override
    protected RegexSubgraph constructSubGraph() {
        return new RegexSubgraph(this) ;
    }

    @Override
    public RegexWholeGraph getWholeGraph() {
        return this;
    }
    
    /* Handles selection */
    private RegexSubgraph selectedNodesGraph;
    private List<RegexNode> selectedNodesList;
    
    public void deSelectAll(){
        for(RegexNode selected : selectedNodesGraph.getNodes()){
            selected.deSelect();
        }
        this.selectedNodesGraph.clear();
        this.selectedNodesList.clear();
    }
    
    public void select(RegexNode node){
        this.deSelectAll();
        addToSelection(node);
    }
    
    public void select(List<RegexNode> nodes){
        this.deSelectAll();
        addToSelection(nodes);
    }
    
    public void addToSelection(RegexNode node){
        if(node != null){
            node.select();
            selectedNodesGraph.addTop(node);
            selectedNodesList.add(node);
        }
    }
    
    public void toggleSelection(RegexNode node){
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
    
    public void addToSelection(List<RegexNode> nodes){
        if(nodes == null){
            return;
        }
        for(RegexNode selected : nodes){
            this.addToSelection(selected);
        }
    }
    
    public List<RegexNode> getSelectionNodes(){
        return this.selectedNodesList;
    }
    
    public RegexSubgraph getSelectionSubgraph(){
        return this.selectedNodesGraph;
    }

    
    /* observer pattern */
    private ArrayList<IRegexHigraphObserver> observers = new ArrayList<IRegexHigraphObserver>();
    
    /**
     * Notify the observers that the content of the subgraph is changed.
     */
    public void notifyObservers(){
        if(this.observers == null || this.observers.size() == 0){
            return;
        }
        for(IRegexHigraphObserver o : this.observers){
            o.changed(this);
        }
    }
    
    /* (non-Javadoc)
     * @see regex.model.RegexHigraph#registerObserver(regex.model.IRegexHigraphObserver)
     */
    public void registerObserver(IRegexHigraphObserver o){
        if(o != null && !this.observers.contains(o)){
            this.observers.add(o);
        }
    }
    /* (non-Javadoc)
     * @see regex.model.RegexHigraph#deRegisterObserver(regex.model.IRegexHigraphObserver)
     */
    public void deRegisterObserver(IRegexHigraphObserver o){
        if(o != null && this.observers.contains(o)){
            this.observers.remove(o);
        }
    }
}
