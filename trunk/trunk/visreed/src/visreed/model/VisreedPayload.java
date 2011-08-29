/**
 * VisreedPayload.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo (MUN# 200982155)
 * This file was prepared by Xiaoyu Guo. It was completed by me alone.
 */
package visreed.model;

import higraph.model.taggedInterfaces.TaggedPayload;
import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import visreed.model.tag.VisreedTag;
import visreed.view.VisreedNodeView;

/**
 * @author biggates
 *
 */
public abstract class VisreedPayload implements TaggedPayload<VisreedTag, VisreedPayload> {

    protected final VisreedTag tag;
    
    public VisreedPayload(VisreedTag tag){
        this.tag = tag;
    }
    
    /* (non-Javadoc)
     * @see higraph.model.interfaces.Payload#copy()
     */
    @Override
    public VisreedPayload copy() {
        return this;
    }

    /* (non-Javadoc)
     * @see higraph.model.taggedInterfaces.TaggedPayload#getTag()
     */
    @Override
    public VisreedTag getTag() {
        return this.tag;
    }
    
    /**
     * Returns the string format of regular expression
     * @param currentNode the {@link visreed.extension.regex.model.RegexNode} of the payload
     * @return the string representation of the regular expression
     */
    public abstract String format(VisreedNode currentNode);
    
    /**
     * Gets the brief description of the payload.
     * <p>normally, this will returns the tag name.
     * @return the brief description
     */
    public String getDescription(){
        return this.getTag().toString();
    }
    
    /**
     * Constructs the corresponding {@link visreed.view.VisreedNodeView} class
     * @param sgv
     * @param node
     * @param timeman
     * @return
     */
    public abstract VisreedNodeView constructView(
        HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> sgv,
        VisreedNode node,
        BTTimeManager timeman
    );
}
