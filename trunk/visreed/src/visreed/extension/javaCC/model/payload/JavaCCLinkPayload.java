/**
 * JavaCCLinkPayload.java
 * 
 * @date: Sep 20, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.payload;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.TerminalPayload;
import visreed.view.TerminalNodeView;
import visreed.view.VisreedNodeView;

/**
 * @author Xiaoyu Guo
 *
 */
public class JavaCCLinkPayload extends TerminalPayload {

	private ProductionPayload source;
	
	public JavaCCLinkPayload(String productionName) {
		super(productionName);
	}
	
	public JavaCCLinkPayload(ProductionPayload pl){
		super(pl.getName());
		this.source = pl;
	}

	/* (non-Javadoc)
	 * @see visreed.model.VisreedPayload#format(visreed.model.VisreedNode)
	 */
	@Override
	public String format(VisreedNode currentNode) {
		return this.source.format(currentNode);
	}

	/* (non-Javadoc)
	 * @see visreed.model.VisreedPayload#constructView(higraph.view.HigraphView, visreed.model.VisreedNode, tm.backtrack.BTTimeManager)
	 */
	@Override
	public VisreedNodeView constructView(
			HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> sgv,
			VisreedNode node, BTTimeManager timeman) {
		return new TerminalNodeView(sgv, node, timeman);
	}

}
