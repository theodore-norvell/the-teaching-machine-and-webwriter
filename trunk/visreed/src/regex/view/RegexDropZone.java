/**
 * RegexDropZone.java
 * 
 * @date: Jul 25, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import java.awt.event.MouseEvent;
import java.util.List;

import higraph.view.DropZone;
import higraph.view.NodeView;
import regex.model.IHoverable;
import regex.model.RegexEdge;
import regex.model.RegexEdgeLabel;
import regex.model.RegexHigraph;
import regex.model.RegexNode;
import regex.model.RegexPayload;
import regex.model.RegexSubgraph;
import regex.model.RegexWholeGraph;
import regex.swing.IInteractable;
import tm.backtrack.BTTimeManager;

/**
 * RegexDropZone is the unified drop zones for this project.
 * @author Xiaoyu Guo
 */
public class RegexDropZone 
extends DropZone<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge>
implements IHoverable, IInteractable
{

    private int nodeNumber;
    
    /**
     * @return the nodeNumber
     */
    public int getNodeNumber() {
        return nodeNumber;
    }

    /**
     * @param nodeNumber the nodeNumber to set
     */
    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    /**
     * @param nv
     * @param timeMan
     */
    protected RegexDropZone(
        NodeView <RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> nv,
        BTTimeManager timeMan) {
        super(nv, timeMan);
        this.nodeNumber = 0; // Default value
    }
    
    private static final Color ZONE_BORDER_COLOR = SystemColor.textHighlight;
    private static final Color ZONE_FILL_COLOR = new Color(
        SystemColor.textHighlight.getRed(),
        SystemColor.textHighlight.getGreen(),
        SystemColor.textHighlight.getBlue(),
        50
    );
    
    @Override
    protected void drawSelf(Graphics2D screen){
        if(RegexNodeView.IN_DEBUG_MODE || this.isHoverOn){
            Color previousColor = screen.getColor();
            
            // fill
            screen.setColor(ZONE_FILL_COLOR);
            screen.fill(this.getShape());
            
            // border
            screen.setColor(ZONE_BORDER_COLOR);
            screen.draw(this.getShape());
            
            screen.setColor(previousColor);
        }
    }
    
    /* Handling drops */

    /* (non-Javadoc)
     * @see regex.swing.IInteractable#isDropAccepted()
     */
    @Override
    public boolean isDropAccepted(){
        return false;
    }
    
    /* (non-Javadoc)
     * @see regex.swing.IInteractable#handleDrop(java.util.List)
     */
    @Override
    public void handleDrop(List<RegexNode> nodes){}

    /* (non-Javadoc)
     * @see regex.swing.IInteractable#handleClick(java.awt.event.MouseEvent)
     */
    @Override
    public void handleClick(MouseEvent e) {
    }

    /* (non-Javadoc)
     * @see regex.swing.IInteractable#handleDoubleClick(java.awt.event.MouseEvent)
     */
    @Override
    public void handleDoubleClick(MouseEvent e) {
    }
    
    /* (non-Javadoc)
     * @see higraph.view.DropZone#toString()
     */
    @Override
    public String toString(){
        return TYPE_STRING + "(" + this.getId() + ")" + this.getAssociatedComponent();
    }

    /* Handling hovering */
    private boolean isHoverOn;
    
    /* (non-Javadoc)
     * @see regex.model.IHoverable#setHoverOn()
     */
    @Override
    public void setHoverOn() {
        this.isHoverOn = true;
        // sets its parent hovered on
        ((RegexNodeView)this.getAssociatedComponent()).setHoverOn();
    }

    /* (non-Javadoc)
     * @see regex.model.IHoverable#setHoverOff()
     */
    @Override
    public void setHoverOff() {
        this.isHoverOn = false;
        if(((RegexNodeView)this.getAssociatedComponent()).isHoverOn()){
            ((RegexNodeView)this.getAssociatedComponent()).setHoverOff();
        }
    }

    /* (non-Javadoc)
     * @see regex.model.IHoverable#isHoverOn()
     */
    @Override
    public boolean isHoverOn() {
        return this.isHoverOn;
    }
}
