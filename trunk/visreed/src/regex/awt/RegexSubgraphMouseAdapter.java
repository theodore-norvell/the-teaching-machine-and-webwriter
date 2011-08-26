/**
 * RegexSubgraphMouseAdapter.java
 * 
 * @date: Jul 15, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.awt;

import higraph.swing.SubgraphMouseAdapter;
import higraph.view.ComponentView;
import higraph.view.NodeView;

import java.awt.Point;
import java.util.Stack;

import javax.swing.JComponent;

import regex.RegexSGEventObserver;
import regex.model.RegexEdge;
import regex.model.RegexEdgeLabel;
import regex.model.RegexHigraph;
import regex.model.RegexNode;
import regex.model.RegexPayload;
import regex.model.RegexSubgraph;
import regex.model.RegexWholeGraph;
import regex.swing.RegexJComponent;
import regex.swing.RegexSubgraphTransferHandler;
import regex.view.RegexHigraphView;

/**
 * @author Xiaoyu Guo
 *
 */
public class RegexSubgraphMouseAdapter extends
    SubgraphMouseAdapter<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> {

    /**
     * @param view
     * @param observer
     */
    public RegexSubgraphMouseAdapter(
            RegexHigraphView view,
            RegexSGEventObserver observer) {
        super(view, observer);
        
        myTransferHandler = new RegexSubgraphTransferHandler(this) ;
    }
    
    /* (non-Javadoc)
     * @see higraph.swing.SubgraphMouseAdapter#installIn(javax.swing.JComponent)
     */
    @Override
    public void installIn( JComponent jComponent ) {
        super.installIn(jComponent);
        if(jComponent instanceof RegexJComponent && this.getObserver() != null){
            ((RegexSGEventObserver)this.getObserver()).setSubgraphView(((RegexJComponent)jComponent).getSubgraphView());
        }
    }    
    
    @Override
    protected void findComponentsUnder(
        Stack<ComponentView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge>> stack, 
        NodeView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> nodeView,
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
