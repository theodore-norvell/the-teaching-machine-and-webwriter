/**
 * RepeatRangeNodeView.java
 * 
 * @date: Oct 4, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.view.layout.VisreedNodeLayoutManager;

/**
 * @author Xiaoyu Guo
 *
 */
public class RepeatRangeNodeView extends VisreedNodeView {

	/**
	 * @param v
	 * @param node
	 * @param timeMan
	 */
	public RepeatRangeNodeView(
			HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> v,
			VisreedNode node, BTTimeManager timeMan) {
		super(v, node, timeMan);
	}

	/* (non-Javadoc)
	 * @see visreed.view.VisreedNodeView#getLayoutHelper()
	 */
	@Override
	protected VisreedNodeLayoutManager getLayoutHelper() {
		// TODO Auto-generated method stub
		return null;
	}

}
