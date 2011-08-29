/**
 * VisreedNode.java
 * 
 * @date: 2011-8-15
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model;

import higraph.model.abstractClasses.AbstractNode;

/**
 * @author Xiaoyu Guo
 */
public class VisreedNode 
extends AbstractNode<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>
implements ISelectable, IHoverable {

    protected VisreedNode(VisreedWholeGraph higraph, VisreedPayload payload) {
        super(higraph, payload);
    }

    protected VisreedNode(VisreedNode original, VisreedNode parent) {
        super(original, parent);
    }

    @Override
    protected VisreedNode getThis() { return this ; }
    
    /**
     * Appends the specified child node to the node
     * @param child
     */
    public void appendChild(VisreedNode child){
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
        return "VisreedNode_" + desc;
    }

    /* Handles selection */
    private boolean isSelected = false;

    /* (non-Javadoc)
     * @see visreed.model.ISelectable#isSelected()
     */
    @Override
    public boolean isSelected() {
        return isSelected;
    }

    /* (non-Javadoc)
     * @see visreed.model.ISelectable#select()
     */
    @Override
    public void select() {
        setSelected(true);
    }

    /* (non-Javadoc)
     * @see visreed.model.ISelectable#deSelect()
     */
    @Override
    public void deSelect() {
        setSelected(false);
    }

    /* (non-Javadoc)
     * @see visreed.model.ISelectable#setSelected(boolean)
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
     * @see visreed.view.IHoverable#setHoverOn()
     */
    @Override
    public void setHoverOn() {
        this.setHover(true);
    }

    /* (non-Javadoc)
     * @see visreed.view.IHoverable#setHoverOff()
     */
    @Override
    public void setHoverOff() {
        this.setHover(false);
    }
    

    /* (non-Javadoc)
     * @see visreed.model.IHoverable#isHoverOn()
     */
    @Override
    public boolean isHoverOn() {
        return this.hover;
    }
    
    @Override
    public VisreedNode duplicate(){
    	VisreedNode result = super.duplicate();
    	result.isSelected = this.isSelected;
    	result.hover = this.hover;
    	return result;
    }
}
