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
import visreed.extension.javaCC.model.JavaCCOptions;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.SequencePayload;
import visreed.view.TerminalNodeView;
import visreed.view.VisreedNodeView;

/**
 * @author Xiaoyu Guo
 *
 */
public class JavaCCRootPayload extends SequencePayload {

	private String parserName;
	public String getParserName(){
		return this.parserName;
	}
	public void setParserName(String value){
		this.parserName = value;
	}
	
	private JavaCCOptions options;
	public JavaCCOptions getOptions(){
		return this.options;
	}
	
	private String compilationUnit;
	public String getCompilationUnit(){
		return this.compilationUnit;
	}
	
	public void setCompilationUnit(String value){
		this.compilationUnit = value;
	}
	
	public JavaCCRootPayload() {
		super();
		this.parserName = "";
		this.options = new JavaCCOptions();
		this.compilationUnit = "";
	}

	
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
		return "JavaCCRoot";
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
        return new TerminalNodeView(sgv, node, timeman);
    }
}
