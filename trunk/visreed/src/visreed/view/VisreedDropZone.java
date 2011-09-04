/**
 * VisreedDropZone.java
 * 
 * @date: Jul 25, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view;

import higraph.view.DropZone;
import higraph.view.NodeView;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import tm.backtrack.BTTimeManager;
import visreed.model.IHoverable;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.swing.IInteractable;

/**
 * VisreedDropZone is the unified drop zones for this project.
 * @author Xiaoyu Guo
 */
public class VisreedDropZone 
extends DropZone<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>
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
    public VisreedDropZone(
        NodeView <VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> nv,
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
        if(VisreedNodeView.IN_DEBUG_MODE || this.isHoverOn){
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
     * @see visreed.swing.IInteractable#isDropAccepted()
     */
    @Override
    public boolean isDropAccepted(){
        return false;
    }
    
    /* (non-Javadoc)
     * @see visreed.swing.IInteractable#handleDrop(java.util.List)
     */
    @Override
    public void handleDrop(MouseEvent e, List<VisreedNode> nodes){}

    /* (non-Javadoc)
     * @see visreed.swing.IInteractable#handleClick(java.awt.event.MouseEvent)
     */
    @Override
    public void handleClick(MouseEvent e) {
    }

    /* (non-Javadoc)
     * @see visreed.swing.IInteractable#handleDoubleClick(java.awt.event.MouseEvent)
     */
    @Override
    public void handleDoubleClick(MouseEvent e) {
    }
    
    /* (non-Javadoc)
     * @see visreed.swing.IInteractable#getPopupMenuItems(java.awt.event.MouseEvent)
     */
    @Override
    public JPopupMenu getPopupMenu(MouseEvent e){
        return null;
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
     * @see visreed.model.IHoverable#setHoverOn()
     */
    @Override
    public void setHoverOn() {
        this.isHoverOn = true;
        // sets its parent hovered on
        ((VisreedNodeView)this.getAssociatedComponent()).setHoverOn();
    }

    /* (non-Javadoc)
     * @see visreed.model.IHoverable#setHoverOff()
     */
    @Override
    public void setHoverOff() {
        this.isHoverOn = false;
        if(((VisreedNodeView)this.getAssociatedComponent()).isHoverOn()){
            ((VisreedNodeView)this.getAssociatedComponent()).setHoverOff();
        }
    }

    /* (non-Javadoc)
     * @see visreed.model.IHoverable#isHoverOn()
     */
    @Override
    public boolean isHoverOn() {
        return this.isHoverOn;
    }
}
