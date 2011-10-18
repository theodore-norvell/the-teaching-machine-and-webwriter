/**
 * MarqueSelectionSGEventObserver.java
 * 
 * @date: Sep 19, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing;

import higraph.view.ComponentView;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Stack;

import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.VisreedPayload;
import visreed.view.IGraphContainer;
import visreed.view.VisreedNodeView;

/**
 * SGEventObserver with Marquee Selection support
 * @author Xiaoyu Guo
 */
public class MarqueeSelectionSGEventObserver extends VisreedSubgraphEventObserver {

    private Point2D marqueeStartingPoint;
    private Point2D marqueeEndingPoint;

	/**
	 * @param frame
	 * @param wg
	 */
	public MarqueeSelectionSGEventObserver(
			IGraphContainer frame,
			VisreedWholeGraph wg) {
		super(frame, wg);
	}
	
    /** Called when the mouse is being moved with one or more buttons down. */ 
    @Override
    public void dragged(Stack<ComponentView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>> stack, MouseEvent e) {
        // System.out.println( getTopNodeDescFromStack(stack) + " : Dragged") ;
        if(this.lastButton == MouseEvent.BUTTON1){
            if(this.isSelectedSource(stack)){
                // selected == source, handling move/copy/etc.
            } else {
                // not dragging selection, handling marquee selection
                if(this.marqueeStartingPoint == null){
                    this.marqueeStartingPoint = e.getPoint();
    //                System.out.println( "Dragged: setting starting point = (" + this.marqueeStartingPoint + ")") ;
                }
                this.marqueeEndingPoint = e.getPoint();
//                System.out.println( "Dragged: start = (" + this.marqueeStartingPoint + "), end = (" + this.marqueeEndingPoint + ")") ;
                
                this.mySubgraphView.setMarquee(this.marqueeStartingPoint, this.marqueeEndingPoint);
                this.mySubgraphView.selectMarquee();
                this.graphContainer.repaint();
            }
        }
    }
    
    /** Called when a mouse button goes down. */
    @Override
    public void pressedOn(Stack<ComponentView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>> stack, MouseEvent e) {
        System.out.println( "Pressed on, position = (" + e.getPoint() + ")" ) ;
        this.dragSourcePoint = e.getPoint();
        this.dragTargetPoint = null;
        this.lastButton = e.getButton();
        
        // handle marquee selection
        if(e.getButton() == MouseEvent.BUTTON1){
            this.marqueeStartingPoint = e.getPoint();
//            System.out.println("Pressed: starting = (" + this.marqueeStartingPoint + ")");
        } else if (e.isPopupTrigger()){
            maybeShowPopupMenu(stack, e);
        }
        this.graphContainer.repaint();
    }
    
    /** Called when a mouse button goes up. */ 
    /* (non-Javadoc)
     * @see higraph.view.interfaces.SubgraphEventObserver#releasedOn(java.util.Stack, java.awt.event.MouseEvent)
     */
    @Override
    public void releasedOn(Stack<ComponentView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>> stack, MouseEvent e) {
        System.out.println( "Release on, position = (" + e.getPoint() + ")" ) ;
        this.dragTargetPoint = e.getPoint();
        if(this.lastButton == MouseEvent.BUTTON1){
            if(marqueeStartingPoint == null){
                return;
            }
            if(marqueeStartingPoint.distance(e.getPoint()) < MINIMUM_DRAG_DISTANCE_PIXEL){
                // treat as click
                // note: the click is already handled, so we do not need to call
                // this.clickedOn(stack, e);
                // again.
                return;
            }
            
            // handle marquee selection
            this.marqueeEndingPoint = e.getPoint();
            // handle marquee selection
            this.mySubgraphView.setMarquee(marqueeStartingPoint, marqueeEndingPoint);
            this.mySubgraphView.selectMarquee();
            
            // finish selection
            this.marqueeStartingPoint = null;
            this.marqueeEndingPoint = null;
            this.lastButton = -1;
            this.mySubgraphView.clearMarquee();
            this.graphContainer.repaint();
        
            // clear a drag operation at last
            this.dragSourcePoint = null;
            this.dragTargetPoint = null;
            this.lastButton = -1;
        } else if (e.isPopupTrigger()){
            // handle single right-click selection
            VisreedNodeView view = getTopNodeView(stack);
            if(view == null){
                // clear selection
                this.myWholeGraph.deSelectAll();
            } else {
                // select the view
                this.myWholeGraph.select(view.getNode());
            }
            // handling popup menu
            maybeShowPopupMenu(stack, e);
        } else {
            if(this.isSelectedSource()){
            }
                // handling drag with options
            else {
                // handling with pan
            }
        }
    }
}
