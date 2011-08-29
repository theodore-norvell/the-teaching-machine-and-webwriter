/**
 * VisreedHigraph.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo (MUN# 200982155)
 * This file was prepared by Xiaoyu Guo. It was completed by me alone.
 */
package visreed.model;

import higraph.model.abstractClasses.AbstractHigraph;

/**
 * @author Xiaoyu Guo
 */
public interface VisreedHigraph
extends
AbstractHigraph
        <VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> {
    /**
     * Register an observer to this higraph
     * @param o the observer
     */
    void registerObserver(IVisreedHigraphObserver o);
    
    /**
     * De-Register an observer to this higraph
     * @param o
     */
    void deRegisterObserver(IVisreedHigraphObserver o);
    
    /**
     * Notify the observers that the content of the higraph is changed.
     */
    void notifyObservers();
}
