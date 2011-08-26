/**
 * RegexNodeLayoutManager.java
 * 
 * @date: 2011-6-14
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.view.layout;

import higraph.view.HigraphView;
import higraph.view.NodeView;
import higraph.view.layout.AbstractLayoutManager;

import java.util.Iterator;

import regex.model.RegexEdge;
import regex.model.RegexEdgeLabel;
import regex.model.RegexHigraph;
import regex.model.RegexNode;
import regex.model.RegexPayload;
import regex.model.RegexSubgraph;
import regex.model.RegexWholeGraph;
import regex.view.RegexDropZone;
import regex.view.RegexHigraphView;
import regex.view.RegexNodeView;

/**
 * RegexNodeLayoutManager concentrates on the layout algorithm of each NodeView.
 * This is the base class for the project visreed.
 * @author Xiaoyu Guo
 */
public abstract class RegexNodeLayoutManager 
    extends AbstractLayoutManager<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge>
{
    /**
     * Recursively layout the children of the nodeview, bottom-up.
     * @param nv the desired nodeview
     * @param px
     * @param py
     * @return the extent of the node
     */
    public abstract void layoutNode(RegexNodeView nv, double px, double py);
    
    /**
     * Gets a {@link regex.view.layout.RegexNodeLayoutManager} from a given {@link regex.view.RegexNodeView}.
     * @param view
     * @return
     */
    public static RegexNodeLayoutManager get(RegexNodeView view){
        if(view == null){
            return SequenceLayoutManager.getInstance();
        } else {
            return (RegexNodeLayoutManager) view.getLayoutManager();
        }
    }
    
    public void layoutLocal(HigraphView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> hgv){
        Iterator<NodeView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge>> iterator = hgv.getTops();
        if(iterator.hasNext()){
            NodeView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> top = iterator.next();
            
            layoutLocal(top);
        }
        
        if(hgv instanceof RegexHigraphView){
            ((RegexHigraphView)hgv).updateNodePosition();
        }
    }
    
    /* (non-Javadoc)
     * @see higraph.view.layout.NodeLayoutManager#layoutLocal(higraph.view.NodeView)
     */
    @Override
    public void layoutLocal(NodeView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> nv){
        if(nv instanceof RegexNodeView){
            this.layoutNodes((RegexNodeView)nv, 0., 0.);
        }
    }
    
    public final void layoutNodes(RegexNodeView nv, double px, double py){
        // refresh drop zones
        nv.refreshDropZone();
        nv.getLayoutManager().layoutNode(nv, px, py);
        nv.moveZones();
        //v.doLayout(0, 0);
    }
    
    /**
     * Handles the stretch for a {@link RegexNodeView}
     * @param view
     */
    public void handleStretch(RegexNodeView view) {
        // currently only Sequence Node supports the stretch
    }

    /**
     * @param view
     * @param zone
     */
    public void layoutZones(RegexNodeView view, RegexDropZone zone) {
        // TODO Auto-generated method stub
        
    }
}
