/**
 * VisreedSubgraphMouseAdapter.java
 * 
 * @date: Jul 15, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.awt;

import higraph.swing.SubgraphMouseAdapter;

import javax.swing.JComponent;

import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.VisreedPayload;
import visreed.swing.VisreedJComponent;
import visreed.swing.VisreedSubgraphEventObserver;
import visreed.swing.VisreedSubgraphTransferHandler;
import visreed.view.VisreedHigraphView;

/**
 * @author Xiaoyu Guo
 *
 */
public class VisreedSubgraphMouseAdapter extends
    SubgraphMouseAdapter<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> {

    /**
     * @param view
     * @param observer
     */
    public VisreedSubgraphMouseAdapter(
            VisreedHigraphView view,
            VisreedSubgraphEventObserver observer) {
        super(view, observer);
        
        myTransferHandler = new VisreedSubgraphTransferHandler(this) ;
    }
    
    /* (non-Javadoc)
     * @see higraph.swing.SubgraphMouseAdapter#installIn(javax.swing.JComponent)
     */
    @Override
    public void installIn( JComponent jComponent ) {
        super.installIn(jComponent);
        if(jComponent instanceof VisreedJComponent && this.getObserver() != null){
            ((VisreedSubgraphEventObserver)this.getObserver()).setSubgraphView(((VisreedJComponent)jComponent).getSubgraphView());
            jComponent.setTransferHandler(new VisreedSubgraphTransferHandler(this));
        }
    }    

}
