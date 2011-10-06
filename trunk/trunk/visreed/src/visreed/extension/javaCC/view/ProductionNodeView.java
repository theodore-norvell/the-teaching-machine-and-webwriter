/**
 * ProductionNodeView.java
 * 
 * @date: Aug 29, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.view;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.view.TerminalNodeView;
import visreed.view.layout.AlternationLayoutManager;
import visreed.view.layout.VisreedNodeLayoutManager;

/**
 * @author Xiaoyu Guo
 *
 */
public class ProductionNodeView extends TerminalNodeView {

    /**
     * @param v
     * @param node
     * @param timeMan
     */
    public ProductionNodeView(
            HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> v,
            VisreedNode node,
            BTTimeManager timeMan) {
        super(v, node, timeMan);
    }
    
    private boolean folded;

    /* (non-Javadoc)
     * @see visreed.view.VisreedNodeView#getLayoutHelper()
     */
    @Override
    protected VisreedNodeLayoutManager getLayoutHelper() {
        return AlternationLayoutManager.getInstance();
    }

	public boolean isFolded() {
		return folded;
	}

	public void setFolded(boolean folded) {
		if(folded != this.folded){
			this.folded = folded;
		}
	}
}
