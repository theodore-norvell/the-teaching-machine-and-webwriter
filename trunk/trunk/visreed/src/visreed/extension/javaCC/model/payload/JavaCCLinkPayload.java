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
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.VisreedPayload;
import visreed.view.VisreedNodeView;

/**
 * @author Xiaoyu Guo
 *
 */
public class JavaCCLinkPayload extends VisreedPayload {

	protected ProductionPayload source;
	protected String productionName;
	
	public JavaCCLinkPayload(String productionName) {
		super(JavaCCTag.LINK);
		this.productionName = productionName;
	}
	
	public JavaCCLinkPayload(ProductionPayload pl){
		super(JavaCCTag.LINK);
		this.setSource(pl);
	}
	
	public void setSource(ProductionPayload pl){
		if(pl != null){
			this.source = pl;
			this.productionName = pl.getName();
		} else {
			this.source = null;
			this.productionName = "";
		}
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
			String name = this.source.getName();
			if(!name.equals(this.productionName)){
				this.productionName = name;
			}
			return name;
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
	
	@Override
    public StringBuffer dump(StringBuffer sb, int indentLevel) {
    	sb = super.dump(sb, indentLevel);
    	sb.append("< ");
    	
    	String text = "";
    	if(this.source != null){
    		text = this.source.getName();
    	} else {
    		text = this.productionName;
    	}
    	sb.append(text);
    	
		sb.append(">");
    	return sb;
    }
}
