/**
 * RegexSubgraph.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo (MUN# 200982155)
 * This file was prepared by Xiaoyu Guo. It was completed by me alone.
 */
package regex.model;

import java.util.ArrayList;

import higraph.model.abstractClasses.AbstractSubgraph;

/**
 * 
 * @author Xiaoyu Guo
 */
public class RegexSubgraph
extends AbstractSubgraph<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge>
implements RegexHigraph
{
    protected RegexSubgraph(RegexWholeGraph wholeGraph) {
        super(wholeGraph);
    }
    
    @Override
    protected RegexSubgraph getThis() { return this ; }

    private ArrayList<IRegexHigraphObserver> observers = new ArrayList<IRegexHigraphObserver>();
 
    /* observer pattern */
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
    
    /* operation overrides for observer notification */
    @Override
    public void addTop(RegexNode v){
        super.addTop(v);
        notifyObservers();
    }
    
    /** Clear the graph */
    public void clear() {
        for(RegexNode node : this.getTops()){
            node.deSelect();
            this.removeTop(node);
        }
        this.notifyObservers();
    }
}
