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
import visreed.extension.javaCC.model.tag.JavaCCTag;
import visreed.extension.javaCC.view.JavaCCLinkNodeView;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.view.VisreedNodeView;

/**
 * @author Xiaoyu Guo
 *
 */
public class JavaCCLinkPayload extends VisreedPayload {

	private ProductionPayload source;
	String productionName;
	
	public JavaCCLinkPayload(String productionName) {
		super(JavaCCTag.LINK);
		this.productionName = productionName;
	}
	
	public JavaCCLinkPayload(ProductionPayload pl){
		super(JavaCCTag.LINK);
		this.source = pl;
	}
	
	public void setSource(ProductionPayload pl){
		this.source = pl;
	}

	/* (non-Javadoc)
	 * @see visreed.model.VisreedPayload#format(visreed.model.VisreedNode)
	 */
	@Override
	public String format(VisreedNode currentNode) {
		// TODO
		if(this.source == null){
			return "Link_" + this.productionName;
		} else {
			return "Link_" + this.source.format(currentNode);
		}
	}
	
	/* (non-Javadoc)
	 * @see visreed.model.payload.TerminalPayload#getDescription()
	 */
	@Override
	public String getDescription(){
		if(this.source == null){
			return this.productionName;
		} else {
			return this.source.getName();
		}
	}

	/* (non-Javadoc)
	 * @see visreed.model.VisreedPayload#constructView(higraph.view.HigraphView, visreed.model.VisreedNode, tm.backtrack.BTTimeManager)
	 */
	@Override
	public VisreedNodeView constructView(
			HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> sgv,
			VisreedNode node, BTTimeManager timeman) {
		return new JavaCCLinkNodeView(sgv, node, timeman);
	}

}
