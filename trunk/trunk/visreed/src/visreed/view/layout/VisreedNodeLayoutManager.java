/**
 * VisreedNodeLayoutManager.java
 * 
 * @date: 2011-6-14
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view.layout;

import higraph.view.HigraphView;
import higraph.view.NodeView;
import higraph.view.layout.AbstractLayoutManager;

import java.util.Iterator;

import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.VisreedPayload;
import visreed.view.VisreedDropZone;
import visreed.view.VisreedHigraphView;
import visreed.view.VisreedNodeView;

/**
 * VisreedNodeLayoutManager concentrates on the layout algorithm of each NodeView.
 * This is the base class for the project visreed.
 * @author Xiaoyu Guo
 */
public abstract class VisreedNodeLayoutManager 
    extends AbstractLayoutManager<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>
{
    /**
     * Recursively layout the children of the nodeview, bottom-up.
     * @param nv the desired nodeview
     * @param px
     * @param py
     * @return the extent of the node
     */
    public abstract void layoutNode(VisreedNodeView nv, double px, double py);
    
    /**
     * Gets a {@link visreed.view.layout.VisreedNodeLayoutManager} from a given {@link visreed.view.VisreedNodeView}.
     * @param view
     * @return
     */
    public static VisreedNodeLayoutManager get(VisreedNodeView view){
        if(view == null){
            return SequenceLayoutManager.getInstance();
        } else {
            return (VisreedNodeLayoutManager) view.getLayoutManager();
        }
    }
    
    /* (non-Javadoc)
     * @see higraph.view.layout.AbstractLayoutManager#layoutLocal(higraph.view.HigraphView)
     */
    public void layoutLocal(HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> hgv){
        Iterator<NodeView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>> iterator = hgv.getTops();
        if(iterator.hasNext()){
            NodeView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> top = iterator.next();
            
            layoutLocal(top);
        }
        
        if(hgv instanceof VisreedHigraphView){
            ((VisreedHigraphView)hgv).updateNodePosition();
        }
    }
    
    /* (non-Javadoc)
     * @see higraph.view.layout.NodeLayoutManager#layoutLocal(higraph.view.NodeView)
     */
    @Override
    public void layoutLocal(NodeView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> nv){
        if(nv instanceof VisreedNodeView){
            this.layoutNodes((VisreedNodeView)nv, 0., 0.);
        }
    }
    
    /**
     * @param nv
     * @param px
     * @param py
     */
    public final void layoutNodes(VisreedNodeView nv, double px, double py){
        // refresh drop zones
        nv.refreshDropZone();
        nv.getLayoutManager().layoutNode(nv, px, py);
        nv.moveZones();
        //v.doLayout(0, 0);
    }
    
    /**
     * Handles the stretch for a {@link VisreedNodeView}
     * @param view
     */
    public void handleStretch(VisreedNodeView view) {
        // currently only Sequence Node supports the stretch
    }

    /**
     * @param view
     * @param zone
     */
    public void layoutZones(VisreedNodeView view, VisreedDropZone zone) {
        // TODO Auto-generated method stub
        
    }
}
