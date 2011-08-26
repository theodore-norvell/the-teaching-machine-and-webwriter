/*
 * Created on 2010-03-09 by Theodore S. Norvell. 
 */
package higraph.swing;

import higraph.model.interfaces.*;
import higraph.view.ComponentView;

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.util.Stack;

import javax.swing.*;

class SubgraphTransferHandler 
    < NP extends Payload<NP>,
      EP extends Payload<EP>,
      HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
      WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
      SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
      N extends Node<NP,EP,HG,WG,SG,N,E>, 
      E extends Edge<NP,EP,HG,WG,SG,N,E>
    >
extends TransferHandler {
    
    SubgraphMouseAdapter<NP,EP,HG,WG,SG,N,E> sma ;
    
    SubgraphTransferHandler(SubgraphMouseAdapter<NP,EP,HG,WG,SG,N,E> sma) {
        this.sma = sma ;
    }

    // This isn't being called.
    public Icon getVisualRepresentation(Transferable t) {
        // TODO Revisit when swing actually calls this.
        return null ;
    }
    
    //This is for dragging from this component. Either copy or move is allowed.
    public int getSourceActions(JComponent c) {
        return sma.observer.getSourceActions() ;
    }

    //This is for dragging from this component.
    protected Transferable createTransferable(JComponent c) {
        return sma.observer.createTransferable( ) ;
    }

    // This is for dragging from this component.
    // It is called after the import actions.
    protected void exportDone(JComponent source, Transferable data,
            int action) {
        // Nothing to do as of yet
    }

    // This is for dropping on this component.
    public boolean canImport(TransferHandler.TransferSupport support) {
        if( ! support.isDrop() ) return false ;
        Point p = support.getDropLocation().getDropPoint() ;
        Stack<ComponentView<NP,EP,HG,WG,SG,N,E>> stack = sma.findComponentsUnder(p);
        return sma.observer.canDropHere(stack, support) ;
        
    }

    // This is for dropping on this component.
    public boolean importData(TransferHandler.TransferSupport support) {
        Point p = support.getDropLocation().getDropPoint() ;
        Stack<ComponentView<NP,EP,HG,WG,SG,N,E>> stack = sma.findComponentsUnder(p);
        return sma.observer.importData(stack, support) ;
    }
}
