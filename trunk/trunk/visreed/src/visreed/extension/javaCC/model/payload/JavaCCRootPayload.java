/**
 * JavaCCRootPayload.java
 * 
 * @date: Sep 12, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.payload;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import visreed.extension.javaCC.model.JavaCCWholeGraph;
import visreed.extension.javaCC.model.tag.JavaCCTag;
import visreed.extension.javaCC.view.JavaCCRootNodeView;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.SequencePayload;
import visreed.model.payload.VisreedPayload;
import visreed.view.VisreedNodeView;

/**
 * @author Xiaoyu Guo
 *
 */
public class JavaCCRootPayload extends SequencePayload {

	public JavaCCRootPayload(JavaCCWholeGraph wg){
		super(JavaCCTag.ROOT);
		this.wg = wg;
	}

	private JavaCCWholeGraph wg;
	
    /* (non-Javadoc)
     * @see higraph.model.interfaces.Payload#copy()
     */
    @Override
    public JavaCCRootPayload copy() {
        return this;
    }
    
	/* (non-Javadoc)
	 * @see visreed.model.payload.SequencePayload#format(visreed.model.VisreedNode)
	 */
	@Override
	public String format(VisreedNode currentNode) {
		return this.dump(null, 0).toString();
	}
	
	@Override
	public StringBuffer dump(StringBuffer sb, int indentLevel){
		if(sb == null){
			sb = new StringBuffer();
		}
		if(this.wg == null){
			return sb;
		}
		// options
		this.wg.getOptions().dump(sb, indentLevel);
		
		// parser name
		String parserName = this.wg.getParserName();
		sb.append("PARSER_BEGIN(");
		sb.append(parserName);
		sb.append(")");
		sb.append("\n");

		// compilation unit
		sb.append(this.wg.getCompilationUnit());
		
		// parser end
		sb.append("PARSER_END(");
		sb.append(parserName);
		sb.append(")\n");
		
		// productions
		for(VisreedNode p : this.wg.getTops()){
			if(p.getPayload() instanceof ProductionPayload){
				((ProductionPayload)p.getPayload()).dump(sb, indentLevel);
			}
		}
		return sb;
	}
	
    /* (non-Javadoc)
     * @see visreed.model.VisreedPayload#constructView(higraph.view.HigraphView, visreed.model.RegexNode, java.awt.Color, java.awt.Color, java.awt.Stroke, java.awt.geom.RectangularShape, boolean)
     */
    @Override
    public VisreedNodeView constructView(
        HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> sgv,
        VisreedNode node, 
        BTTimeManager timeman
    ) {
        return new JavaCCRootNodeView(sgv, node, timeman);
    }
}
