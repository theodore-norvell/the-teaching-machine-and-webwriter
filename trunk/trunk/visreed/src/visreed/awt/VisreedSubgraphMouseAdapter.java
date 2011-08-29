/**
 * VisreedSubgraphMouseAdapter.java
 * 
 * @date: Jul 15, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.awt;

import higraph.swing.SubgraphMouseAdapter;
import higraph.view.ComponentView;
import higraph.view.NodeView;

import java.awt.Point;
import java.util.Stack;

import javax.swing.JComponent;

import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.swing.VisreedSubgraphEventObserver;
import visreed.swing.VisreedJComponent;
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
    
    @Override
    protected void findComponentsUnder(
        Stack<ComponentView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>> stack, 
        NodeView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> nodeView,
        Point p
    ){
//      System.out.println("Checking node " + nodeView + " for component under (" + p.x + ", " + p.y + ")");
        if (nodeView.getNextShapeExtent().contains(p)) {
            nodeView.getComponentsUnder(stack, p);  // containing nodeView is deepest backup
            for (int i = 0; i < nodeView.getNumChildren(); i++) // children will be considered first
                findComponentsUnder(stack, nodeView.getChild(i), p);            
        }
    }
}
