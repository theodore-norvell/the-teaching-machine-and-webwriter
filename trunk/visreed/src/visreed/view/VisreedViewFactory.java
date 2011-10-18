
package visreed.view;

import higraph.view.HigraphView;
import higraph.view.NodeView;
import higraph.view.ViewFactory;

import java.awt.Component;

import tm.backtrack.BTTimeManager;
import tm.utilities.Assert;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.VisreedPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class VisreedViewFactory
extends ViewFactory<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>
{
    
    public VisreedViewFactory( BTTimeManager tm ) {
        super(tm) ;
    }

    @Override
    public VisreedHigraphView makeHigraphView(VisreedHigraph sg, Component display) {
        return new VisreedHigraphView( this, sg, display, this.timeMan ) ;
    }
    
    @Override 
    public VisreedNodeView makeNodeView(
        HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> sgv,
        VisreedNode node
    ) {
        // create actual view according to the payload
        Assert.check(node != null);
        VisreedPayload pl = node.getPayload();
        VisreedNodeView result = pl.constructView(sgv, node, this.timeMan);
        result.setNodeViewFactory(this);
        node.registerObserver(result);
        return result;
    }
    
    @Override
    public VisreedDropZone makeDropZone(NodeView <VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> nv) {
        return new VisreedDropZone(nv, timeMan) ;
    }

    public VisreedDropZone makeInsertChildDropZone(VisreedNodeView nv) {
        return new InsertChildDropZone(nv, timeMan);
    }
    
    public VisreedDropZone makeTerminalDropZone(VisreedNodeView nv){
    	return new TerminalDropZone(nv, timeMan);
    }
}
