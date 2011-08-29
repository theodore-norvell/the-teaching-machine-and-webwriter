/*
 * Created on 2010-03-10 by Theodore S. Norvell. 
 */
package visreed.swing;
import higraph.swing.ViewTransferObject;
import higraph.view.ComponentView;
import higraph.view.interfaces.SubgraphEventObserver;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;

import visreed.model.IHoverable;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.view.*;

public class VisreedSubgraphEventObserver
extends SubgraphEventObserver<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> {

    /** Defines the minimum distance of dragging */
    private static final double MINIMUM_DRAG_DISTANCE_PIXEL = 10;

    /**
     * Default constructor
     * @param frame
     * @param wg
     */
    public VisreedSubgraphEventObserver(IGraphContainer frame, VisreedWholeGraph wg){
        this.graphContainer = frame;
        this.myWholeGraph = wg;
    }
    
    public void setSubgraphView(VisreedHigraphView view){
        this.mySubgraphView = view;
    }

    private Point2D marqueeStartingPoint;
    private Point2D marqueeEndingPoint;
    private int lastButton = -1;
    private Point2D dragSourcePoint;
    private Point2D dragTargetPoint;
    
    protected VisreedHigraphView mySubgraphView ;
    protected VisreedWholeGraph myWholeGraph;
    protected IGraphContainer graphContainer;
    protected IHoverable lastHoveringOn;
    
    private StringBuilder sb = new StringBuilder();
    
    /**
     * Gets the top node view from the stack
     * @param stack
     * @return
     */
    protected VisreedNodeView getTopNodeView(Stack<ComponentView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>> stack){
        if(stack == null || stack.size() == 0){
            return null;
        }
        if(stack.peek() instanceof VisreedNodeView){
            return (VisreedNodeView)stack.peek();
        } else if (stack.peek() instanceof VisreedDropZone){
            return (VisreedNodeView)(((VisreedDropZone)stack.peek()).getAssociatedComponent());
        } else {
            return null;
        }
    }
    
    /**
     * Gets the top hoverable from the stack
     * @param stack
     * @return
     */
    protected IHoverable getTopHoverable(Stack<ComponentView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>> stack){
        if(stack == null || stack.size() == 0){
            return null;
        }
        if(stack.peek() instanceof IHoverable){
            return (IHoverable)stack.peek();
        } else {
            return null;
        }
    }
    
    protected String getTopNodeDescFromStack(Stack<ComponentView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>> stack){
        sb.setLength(0);
        if(stack == null || stack.size() == 0){
            return "";
        }
        
        sb.append("<");
        ComponentView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> v = stack.firstElement();
        if(v instanceof VisreedNodeView){
            VisreedNodeView rnv = (VisreedNodeView)v;
            VisreedPayload pl = rnv.getNode().getPayload();
            sb.append(pl.getDescription());
        } else if (v instanceof VisreedDropZone){
            VisreedDropZone rdz = (VisreedDropZone)v;
            sb.append("dropzone(");
            sb.append(rdz.getId());
            sb.append(") for ");
            
            VisreedNodeView rnv = (VisreedNodeView)(rdz.getAssociatedComponent());
            VisreedPayload pl = rnv.getNode().getPayload();
            sb.append(pl.getDescription());
        }
        sb.append(">");
        return sb.toString();
    }
    
    /**
     * Check whether the source of drag is the selected view(s) <br />
     * The check is performed by checking every view's extent
     * @return
     */
    protected boolean isSelectedSource(){
        if(this.dragSourcePoint == null){
            return false;
//        } else if (this.dragTargetPoint == null){
//            return false;
//        } else if (dragSourcePoint.distance(dragTargetPoint) < 5){
//            return false;
        }
        
        List<VisreedNodeView> selection = this.getSelectedViews();
        for(VisreedNodeView nv : selection){
            if(nv.getNextShapeExtent().contains(this.dragSourcePoint)){
                return true;
            }
        }
        return false;
    }
    
    private void maybeShowPopupMenu(
        Stack<ComponentView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>> stack,
        MouseEvent e
    ){
        if(stack == null || stack.size() == 0 || e == null || !e.isPopupTrigger()){
            return;
        }
        
        JPopupMenu menu = null; 
        
        ComponentView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> v = null;
        for(int i = stack.size() - 1; i >= 0; i--){
            v = stack.elementAt(i);
            if(v instanceof IInteractable){
                menu = ((IInteractable)v).getPopupMenu(e);
                if(menu != null){
                    break;
                }
            }
        }
        if(menu != null){
            // show menu
            menu.show(
                e.getComponent(), 
                e.getX(), 
                e.getY()
            );
        }
    }
    
    /** Called when the mouse is being moved with no buttons down. */
    @Override
    public void movedOver(Stack<ComponentView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>> stack, MouseEvent e) {
//        System.out.println( getTopNodeDescFromStack(stack) + " : Moved over") ;
        // handling mouse hovering
        IHoverable top = this.getTopHoverable(stack);
        if(top == null){
            // cancel last hover
            if(this.lastHoveringOn != null){
                this.lastHoveringOn.setHoverOff();
                this.lastHoveringOn = null;
                this.graphContainer.repaint();
            }
        } else if (top != null && top != this.lastHoveringOn){
            // set new hover
            if(this.lastHoveringOn != null){
                this.lastHoveringOn.setHoverOff();
            }
            top.setHoverOn();
            this.lastHoveringOn = top;
            this.graphContainer.repaint();
        }
    }
    
    /** Called when the mouse is being moved with one or more buttons down. */ 
    @Override
    public void dragged(Stack<ComponentView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>> stack, MouseEvent e) {
        // System.out.println( getTopNodeDescFromStack(stack) + " : Dragged") ;
        if(this.lastButton == MouseEvent.BUTTON1){
            if(this.isSelectedSource()){
                // selected == source, handling move/copy/etc.
                int a = 0;
                a++;
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

    /** Called when the mouse button has been pressed and released. */
    @Override
    public void clickedOn(Stack<ComponentView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>> stack, MouseEvent e) {
//        System.out.println( getTopNodeDescFromStack(stack) + " : Clicked on " + e.getClickCount() + " times") ;
        
        if(e.getClickCount() == 1){
            // handle click selection
            VisreedNodeView view = getTopNodeView(stack);
            if(view == null){
                // clear selection
                this.myWholeGraph.deSelectAll();
            } else {
                if(e.isControlDown()){
                    // add to selection
                    this.myWholeGraph.toggleSelection(view.getNode());
                } else {
                    // select the view
                    this.myWholeGraph.select(view.getNode());
                }
            }
            
            // clear marquee selection
            this.lastButton = -1;
            this.mySubgraphView.clearMarquee();
            this.graphContainer.repaint();
        } else if (e.getClickCount() >= 2){
            // handle double click
            if(stack == null || stack.size() == 0){
                return;
            }
            if(stack.peek() instanceof IInteractable){
                ((IInteractable)(stack.peek())).handleDoubleClick(e);
            }
        }
    }
    
    
    /** Called when a mouse button goes down. */
    @Override
    public void pressedOn(Stack<ComponentView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>> stack, MouseEvent e) {
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
//        System.out.println( "Release on, position = (" + e.getPoint() + ")" ) ;
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
    
    /**
     * Gets the only one selected view
     * @return
     */
    private VisreedNodeView getSelectedView(){
        VisreedNodeView result = null;
        if( this.myWholeGraph.getSelectionNodes().size() == 0 ){
            // do nothing
        } else {
            VisreedNode node = this.myWholeGraph.getSelectionNodes().get(0);
            result = (VisreedNodeView) this.mySubgraphView.getNodeView(node);
        }
        return result;
    }
    
    /**
     * Gets all the selected views
     * @return
     */
    private List<VisreedNodeView> getSelectedViews(){
        List<VisreedNodeView> result = new ArrayList<VisreedNodeView>();
        for(VisreedNode node : this.myWholeGraph.getSelectionNodes()){
            result.add((VisreedNodeView)this.mySubgraphView.getNodeView(node));
        }
        return result;
    }
    
    protected int getNumSelection(){
        return this.myWholeGraph.getSelectionNodes().size();
    }
    
    /* (non-Javadoc)
     * @see higraph.view.interfaces.SubgraphEventObserver#getSourceActions()
     */
    @Override
    public int getSourceActions() {
//        System.out.println( "getSourceActions()" ) ;
        int result = java.awt.dnd.DnDConstants.ACTION_NONE;
        if( this.getNumSelection() == 0){
            result =  java.awt.dnd.DnDConstants.ACTION_NONE ;
        } else if (this.isSelectedSource()){
            result = java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE;
        } else if (false) {
            // TODO: decide whether we drags from the toolbar
            result = java.awt.dnd.DnDConstants.ACTION_COPY;
        } else {
            result =  java.awt.dnd.DnDConstants.ACTION_NONE;
        }
//        System.out.println( "...returns "+ result ) ;
        return result ;
    }

    /* (non-Javadoc)
     * @see higraph.view.interfaces.SubgraphEventObserver#createTransferable()
     */
    @Override
    public Transferable createTransferable() {
        Transferable result = null;
        System.out.println( "createTransferable" ) ;
        if( this.myWholeGraph.getSelectionNodes().size() == 0 ){
            result =  null ;
        } else {
            result = new ViewTransferObject( getSelectedView() ) ;
        }
        System.out.println( "...returns " + result ) ;
        return result ;
    }
    
    /* (non-Javadoc)
     * @see higraph.view.interfaces.SubgraphEventObserver#canDropHere(java.util.Stack, javax.swing.TransferHandler.TransferSupport)
     */
    @Override
    public boolean canDropHere(
        Stack<ComponentView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>> stack,
        TransferHandler.TransferSupport supportObj ) {
        System.out.println( "canDropHere()" ) ;  
        boolean result = false;
        if ( supportObj.getDropAction() == java.awt.dnd.DnDConstants.ACTION_NONE ){
            System.out.println("ACTION_NONE");
            result = false ;
//        } else if (dragSourcePoint != null && dragTargetPoint != null && dragSourcePoint.distance(dragTargetPoint) < 5){
//            // small move distance, considers no move at all
//            System.out.println("No move");
//            result = false;
        } else {
            for( DataFlavor f : supportObj.getDataFlavors() ) {
                System.out.println( "...data flavor is " + f) ; 
            }
            result = supportObj.isDataFlavorSupported( ViewTransferObject.theViewDataFlavor ) ;
            result |= supportObj.isDataFlavorSupported( VisreedNodeTransferObject.theNodeDataFlavor ) ;
        
            // pass the handler to the top view
            if(stack.size() > 0){
                if(stack.peek() instanceof IInteractable){
                    result &= ((IInteractable)(stack.peek())).isDropAccepted();
                }
            }
            
            if( result == false && this.getNumSelection() > 0 && !stack.contains( getSelectedView() ) ){
//                result = false ;
            }
        }
        
        // handling hovering
        IHoverable top = this.getTopHoverable(stack);
        if(top == null){
            // cancel last hover
            if(this.lastHoveringOn != null){
                this.lastHoveringOn.setHoverOff();
                this.lastHoveringOn = null;
                this.graphContainer.repaint();
            }
        } else if (top != null && top != this.lastHoveringOn){
            // set new hover
            if(this.lastHoveringOn != null){
                this.lastHoveringOn.setHoverOff();
            }
            top.setHoverOn();
            this.lastHoveringOn = top;
            this.graphContainer.repaint();
        }
        System.out.println( "...returns " + result) ;  
        return result ;
    }
    
    /* (non-Javadoc)
     * @see higraph.view.interfaces.SubgraphEventObserver#importData(java.util.Stack, javax.swing.TransferHandler.TransferSupport)
     */
    @Override
    public boolean importData(
        Stack<ComponentView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>> stack,
        TransferHandler.TransferSupport support) {
        System.out.println( "Import data") ;
        if( !canDropHere( stack, support ) ) {
            System.out.println( "... returns false") ;
            return false ;
        }
        // Do the drop.
        
        List<VisreedNode> data = new ArrayList<VisreedNode>();
        try {
            Transferable t = support.getTransferable();
            if(support.getTransferable().isDataFlavorSupported(VisreedHigraphTransferObject.theNodeListDataFlavor)){
                data = (List<VisreedNode>) t.getTransferData(VisreedHigraphTransferObject.theNodeListDataFlavor);
            } else if (t.isDataFlavorSupported(VisreedNodeTransferObject.theNodeDataFlavor)){
                data.add( (VisreedNode) t.getTransferData(VisreedNodeTransferObject.theNodeDataFlavor) );
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        
        // notify the graph container to re-layout
        if(stack.peek() instanceof IInteractable){
            ((IInteractable)stack.peek()).handleDrop(data);
            this.graphContainer.refreshGraph();
        }
        
//        this.dragSourcePoint = null;
//        this.dragTargetPoint = null;
        System.out.println( "... returns true") ;
        return true ;
    }
}


