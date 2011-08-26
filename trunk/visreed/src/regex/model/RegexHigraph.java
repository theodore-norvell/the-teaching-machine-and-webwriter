/**
 * RegexHigraph.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo (MUN# 200982155)
 * This file was prepared by Xiaoyu Guo. It was completed by me alone.
 */
package regex.model;

import higraph.model.abstractClasses.AbstractHigraph;

/**
 * @author Xiaoyu Guo
 */
public interface RegexHigraph
        extends
        AbstractHigraph
        <RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> {
    /**
     * Register an observer to this higraph
     * @param o the observer
     */
    void registerObserver(IRegexHigraphObserver o);
    
    /**
     * De-Register an observer to this higraph
     * @param o
     */
    void deRegisterObserver(IRegexHigraphObserver o);
    
    /**
     * Notify the observers that the content of the higraph is changed.
     */
    void notifyObservers();
}
