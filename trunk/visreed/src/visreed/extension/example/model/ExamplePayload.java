/**
 * ExamplePayload.java
 * 
 * @date: Nov 30, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.example.model;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import visreed.extension.example.view.ExampleNodeView;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedTag;
import visreed.model.VisreedWholeGraph;
import visreed.view.VisreedNodeView;

/**
 * @author Xiaoyu Guo
 *
 */
public class ExamplePayload extends VisreedPayload {

	/**
	 * @param tag
	 */
	public ExamplePayload(VisreedTag tag) {
		super(tag);
	}

	/* (non-Javadoc)
	 * @see visreed.model.VisreedPayload#format(visreed.model.VisreedNode)
	 */
	@Override
	public String format(VisreedNode currentNode) {
		return this.tag.getDescription();
	}

	/* (non-Javadoc)
	 * @see visreed.model.VisreedPayload#constructView(higraph.view.HigraphView, visreed.model.VisreedNode, tm.backtrack.BTTimeManager)
	 */
	@Override
	public VisreedNodeView constructView(
			HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> sgv,
			VisreedNode node, BTTimeManager timeman) {
		return new ExampleNodeView(sgv, node, timeman);
	}

}
