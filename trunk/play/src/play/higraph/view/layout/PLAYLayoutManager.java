/**
 * PLAYLayoutManager.java - play.higraph.view.layout - PLAY
 * 
 * Created on 2012-03-07 by Kai Zhu
 */
package play.higraph.view.layout;

import higraph.view.HigraphView;
import higraph.view.NodeView;
import higraph.view.layout.AbstractLayoutManager;
import play.higraph.model.PLAYEdge;
import play.higraph.model.PLAYEdgeLabel;
import play.higraph.model.PLAYHigraph;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYWholeGraph;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYNodeView;

/**
 * @author Kai Zhu
 * 
 */
public abstract class PLAYLayoutManager
	extends
	AbstractLayoutManager<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> {

    public abstract void layoutLocal(PLAYHigraphView higraphView);

    public abstract void layoutLocal(PLAYNodeView nodeView);

    /**
     * @see higraph.view.layout.AbstractLayoutManager#layoutLocal(higraph.view.HigraphView)
     */
    @Override
    public final void layoutLocal(
	    HigraphView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> view) {
	layoutLocal((PLAYHigraphView) view);
    }

    /**
     * @see higraph.view.layout.AbstractLayoutManager#layoutLocal(higraph.view.NodeView)
     */
    @Override
    public final void layoutLocal(
    		
	    NodeView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> view) {
	layoutLocal((PLAYNodeView) view);
	//System.out.println("locallayout");
    }

}
