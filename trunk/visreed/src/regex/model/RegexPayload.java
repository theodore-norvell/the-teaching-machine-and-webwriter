/**
 * RegexPayload.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo (MUN# 200982155)
 * This file was prepared by Xiaoyu Guo. It was completed by me alone.
 */
package regex.model;

import higraph.model.taggedInterfaces.TaggedPayload;
import higraph.view.HigraphView;
import regex.view.RegexNodeView;
import tm.backtrack.BTTimeManager;

/**
 * @author biggates
 *
 */
public abstract class RegexPayload implements TaggedPayload<RegexTag, RegexPayload> {

    protected final RegexTag tag;
    
    public RegexPayload(RegexTag tag){
        this.tag = tag;
    }
    
    /* (non-Javadoc)
     * @see higraph.model.interfaces.Payload#copy()
     */
    @Override
    public RegexPayload copy() {
        return this;
    }

    /* (non-Javadoc)
     * @see higraph.model.taggedInterfaces.TaggedPayload#getTag()
     */
    @Override
    public RegexTag getTag() {
        return this.tag;
    }
    
    /**
     * Returns the string format of regular expression
     * @param currentNode the {@link regex.model.RegexNode} of the payload
     * @return the string representation of the regular expression
     */
    public abstract String format(RegexNode currentNode);
    
    /**
     * Gets the brief description of the payload.
     * <p>normally, this will returns the tag name.
     * @return the brief description
     */
    public String getDescription(){
        return this.getTag().toString();
    }
    
    /**
     * Constructs the corresponding {@link regex.view.RegexNodeView} class
     * @param sgv
     * @param node
     * @param timeman
     * @return
     */
    public abstract RegexNodeView constructView(
        HigraphView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> sgv,
        RegexNode node,
        BTTimeManager timeman
    );
}
