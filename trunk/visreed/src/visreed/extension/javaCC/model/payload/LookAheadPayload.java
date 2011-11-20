/**
 * LookAheadPayload.java
 * 
 * @date: Nov 2, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.payload;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import visreed.extension.javaCC.model.tag.JavaCCTag;
import visreed.extension.javaCC.parser.JavaCCBuilder;
import visreed.extension.javaCC.view.LookAheadNodeView;
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
public class LookAheadPayload extends VisreedPayload {

	private int integer;
	private boolean hasInteger = false;
	private boolean hasChild = false;
	private String block = "";
	
	public LookAheadPayload() {
		super(JavaCCTag.LOOKAHEAD);
	}

	/* (non-Javadoc)
	 * @see visreed.model.payload.VisreedPayload#format(visreed.model.VisreedNode)
	 */
	@Override
	public String format(VisreedNode currentNode) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see visreed.model.payload.VisreedPayload#dump(java.lang.StringBuffer, int)
	 */
	@Override
	public StringBuffer dump(StringBuffer sb, int indentLevel){
		sb = JavaCCBuilder.dumpPrefix(sb, 0);
		
		sb.append("LOOKAHEAD (");
		if(this.hasInteger){
			sb.append(this.integer);
		}
		if(this.hasChild){
			sb.append(", ");
			this.getNode().getChild(0).getPayload().dump(sb, indentLevel + 1);
		}
		if(block.length() > 0){
			sb.append(", {\n");
			JavaCCBuilder.dumpPrefix(sb, indentLevel + 1);
			sb.append(this.block);
			sb.append("\n");
			JavaCCBuilder.dumpPrefix(sb, indentLevel + 1);
			sb.append("}\n");
		}
		sb.append(")");
		
		return sb;
	}

	/* (non-Javadoc)
	 * @see visreed.model.payload.VisreedPayload#constructView(higraph.view.HigraphView, visreed.model.VisreedNode, tm.backtrack.BTTimeManager)
	 */
	@Override
	public VisreedNodeView constructView(
		HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> sgv,
		VisreedNode node, BTTimeManager timeman
	) {
		return new LookAheadNodeView(sgv, node, timeman);
	}

	/**
	 * @param intValue
	 */
	public void setInteger(int intValue) {
		this.integer = intValue;
	}
	
	public void setHasInteger(boolean value){
		this.hasInteger = value;
	}
	
	public boolean hasChild(){
		return this.hasChild;
	}
	
	public void setHasChild(boolean value){
		this.hasChild = value;
	}

	/**
	 * @param block
	 */
	public void setBlock(String block) {
		this.block = block;
	}

}
