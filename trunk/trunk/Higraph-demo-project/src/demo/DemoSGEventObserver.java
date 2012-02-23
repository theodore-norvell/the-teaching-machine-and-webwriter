/*
 * Created on 2010-03-10 by Theodore S. Norvell. 
 */
package demo;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.TransferHandler;

import demo.model.* ;
import demo.view.DemoHigraphView;
import higraph.view.ComponentView;
import higraph.view.interfaces.SubgraphEventObserver;
import higraph.swing.ViewTransferObject;

public class DemoSGEventObserver
extends SubgraphEventObserver<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge> {
    
    // TODO this class uses ComponentView. Could it use an application specific class instead?
    
    ComponentView<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>
          selectedView = null ;
    DemoHigraphView mySubgraphView ;
    
    
    /** Called when the mouse is being moved with no buttons down. */
    public void movedOver(Stack<ComponentView<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>> stack, MouseEvent e) {
        System.out.println( "Moved over") ;
    }
    
    /** Called when the mouse is being moved with one or more buttons down. */ 
    public void dragged(Stack<ComponentView<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>> stack, MouseEvent e) {
        System.out.println( "Dragged") ;}

    /** Called when the mouse button has been pressed and released. */
    public void clickedOn(Stack<ComponentView<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>> stack, MouseEvent e) {
        System.out.println( "Clicked on") ;
    }
    
    
    /** Called when a mouse button goes down. */
    public void pressedOn(Stack<ComponentView<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>> stack, MouseEvent e) {
        System.out.println( "Pressed on") ;
        if( stack != null && stack.size() > 0) {
            selectedView = stack.firstElement() ; }
        else {
            selectedView = null ; }
        System.out.println( "selectedView is " + selectedView) ;
    }
    
    /** Called when a mouse button goes up. */ 
    public void releasedOn(Stack<ComponentView<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>> stack, MouseEvent e) {
        System.out.println( "Release on") ;}
    
    public int getSourceActions() {
        System.out.println( "getSourceActions()" ) ;
        int result ;
        if( selectedView == null ) result =  java.awt.dnd.DnDConstants.ACTION_NONE ;
        else result =  java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE ;
        System.out.println( "...returns "+ result ) ;
        return result ;
    }

    public Transferable createTransferable() {
        Transferable result ;
        System.out.println( "createTransferable" ) ;
        if( selectedView == null ) result =  null ;
        else result = new ViewTransferObject( selectedView ) ;
        System.out.println( "...returns " + result ) ;
        return result ;
    }
    
    public boolean canDropHere( Stack<ComponentView<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>> stack,
                        TransferHandler.TransferSupport supportObj ) {
        System.out.println( "canDropHere()" ) ;
        boolean result ;
        if( selectedView != null && stack.contains( selectedView ) )
            result = false ;
        else if( supportObj.getDropAction() == java.awt.dnd.DnDConstants.ACTION_NONE )
            result = false ;
        else {
            for( DataFlavor f : supportObj.getDataFlavors() ) {
                System.out.println( "...data flavor is " + f) ; 
            }
            result = supportObj.isDataFlavorSupported( ViewTransferObject.theViewDataFlavor ) ;
        }
        System.out.println( "...returns " + result) ;  
        return result ;
    }
    
    public boolean importData( Stack<ComponentView<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>> stack,
            TransferHandler.TransferSupport support) {
        System.out.println( "Import data") ;
        if( !canDropHere( stack, support ) ) {
            System.out.println( "... returns false") ;
            return false ;
        }
        // TODO Do the drop.
        System.out.println( "... returns true") ;
        return true ;
    }
}


