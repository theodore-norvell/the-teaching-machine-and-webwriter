/**
 * RegexNode.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.model;

import java.io.StringReader;

import regex.parser.ParseException;
import regex.parser.RegexParser;
import tm.backtrack.BTTimeManager;
import higraph.model.abstractClasses.AbstractNode;

/**
 * @author Xiaoyu Guo
 */
public class RegexNode 
extends AbstractNode<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge>
implements ISelectable, IHoverable {

    protected RegexNode(RegexWholeGraph higraph, RegexPayload payload) {
        super(higraph, payload);
    }

    protected RegexNode(RegexNode original, RegexNode parent) {
        super(original, parent);
    }

    @Override
    protected RegexNode getThis() { return this ; }

    /**
     * Construct a tree structure from a given regular expression
     * @param wg the WholeGraph
     * @param timeMan the TimeManager
     * @param regexp  the regular expression
     * @return the root node of the node tree
     * @throws ParseException 
     */
    public static RegexNode construct(
        RegexWholeGraph wg,
        BTTimeManager timeMan,
        String regexp
    ) throws ParseException{
        StringReader reader = new StringReader(regexp);
        if(timeMan == null){
            timeMan = new BTTimeManager();
        }
        if(wg == null){
            wg = new RegexWholeGraph(timeMan);
        }
        
        RegexNode result = null;
        result = RegexParser.parse(wg, reader);
        return result;
    }
    
    /**
     * Appends the specified child node to the node
     * @param child
     */
    public void appendChild(RegexNode child){
        this.insertChild(getNumberOfChildren(), child);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        String desc = "";
        if(getPayload() == null || getPayload().getDescription() == null){
            desc = "null";
        } else {
            desc = getPayload().getDescription();
        }
        return "RegexNode_" + desc;
    }

    /* Handles selection */
    private boolean isSelected = false;

    /* (non-Javadoc)
     * @see regex.model.ISelectable#isSelected()
     */
    @Override
    public boolean isSelected() {
        return isSelected;
    }

    /* (non-Javadoc)
     * @see regex.model.ISelectable#select()
     */
    @Override
    public void select() {
        setSelected(true);
    }

    /* (non-Javadoc)
     * @see regex.model.ISelectable#deSelect()
     */
    @Override
    public void deSelect() {
        setSelected(false);
    }

    /* (non-Javadoc)
     * @see regex.model.ISelectable#setSelected(boolean)
     */
    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    
    private boolean hover;
    protected final void setHover(boolean hover) {
        this.hover = hover;
    }
    
    /* (non-Javadoc)
     * @see regex.view.IHoverable#setHoverOn()
     */
    @Override
    public void setHoverOn() {
        this.setHover(true);
    }

    /* (non-Javadoc)
     * @see regex.view.IHoverable#setHoverOff()
     */
    @Override
    public void setHoverOff() {
        this.setHover(false);
    }
    

    /* (non-Javadoc)
     * @see regex.model.IHoverable#isHoverOn()
     */
    @Override
    public boolean isHoverOn() {
        return this.hover;
    }
    
}
