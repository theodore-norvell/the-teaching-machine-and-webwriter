/**
 * VisreedSubgraph.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo (MUN# 200982155)
 * This file was prepared by Xiaoyu Guo. It was completed by me alone.
 */
package visreed.model;

import higraph.model.abstractClasses.AbstractSubgraph;

import java.util.ArrayList;

import visreed.pattern.IObserver;

/**
 * 
 * @author Xiaoyu Guo
 */
public class VisreedSubgraph
extends AbstractSubgraph<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>
implements VisreedHigraph
{
    protected VisreedSubgraph(VisreedWholeGraph wholeGraph) {
        super(wholeGraph);
    }
    
    @Override
    protected VisreedSubgraph getThis() { return this ; }

    private ArrayList<IObserver<VisreedHigraph>> observers = new ArrayList<IObserver<VisreedHigraph>>();
 
    /* observer pattern */
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
    
    /* operation overrides for observer notification */
    @Override
    public void addTop(VisreedNode v){
        super.addTop(v);
        notifyObservers();
    }
    
    /** Clear the graph */
    public void clear() {
        for(VisreedNode node : this.getTops()){
            node.deSelect();
            this.removeTop(node);
        }
        this.notifyObservers();
    }
}
