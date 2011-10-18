/**
 * ProductionPayload.java
 * 
 * @date: Aug 28, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.payload;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import visreed.extension.javaCC.model.tag.ProductionTag;
import visreed.extension.javaCC.parser.JavaCCBuilder;
import visreed.extension.javaCC.parser.Token;
import visreed.extension.javaCC.view.ProductionNodeView;
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
public class ProductionPayload extends VisreedPayload {

    public ProductionPayload() {
        super(ProductionTag.getInstance());
    }
    
    public ProductionPayload(String name){
    	super(ProductionTag.getInstance());
    	this.name = name;
    }

    /* (non-Javadoc)
     * @see visreed.model.VisreedPayload#format(visreed.model.VisreedNode)
     */
    @Override
    public String format(VisreedNode currentNode) {
        return "";
    }
    
    /* (non-Javadoc)
     * @see visreed.model.VisreedPayload#getDescription()
     */
    @Override
    public String getDescription(){
        return "Production_" + this.getName();
    }

    /* (non-Javadoc)
     * @see visreed.model.VisreedPayload#constructView(higraph.view.HigraphView, visreed.model.VisreedNode, tm.backtrack.BTTimeManager)
     */
    @Override
    public VisreedNodeView constructView(
        HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> sgv,
        VisreedNode node, BTTimeManager timeman
    ) {
        return new ProductionNodeView(sgv, node, timeman);
    }
    
    private String name = "";
    private int column = 0;
    private int line = 0;
    private Token firstToken = null, lastToken = null;

    private String modifier = "";
    private String returnType = "";
    private String parameter = "";
    private String declaration = "";
    private boolean isPrivate = false;
    private List<Token> throws_list = new ArrayList<Token>();
    
    protected String eol = System.getProperty("line.separator", "\n");

    public StringBuffer dump(StringBuffer sb, int indentLevel) {
    	sb = JavaCCBuilder.dumpPrefix(sb, indentLevel);
    	
    	return sb;
    }
    
    public String getName(){
        String result = this.name;
        if(result == null){
            result = "null";
        } else if (result.length() == 0){
            result = "\"\"";
        }
        return result;
    }
    
    public void setName(String value){
    	boolean changed = (!this.name.equals(value));
        this.name = value;
        if(changed && this.getNode() != null){
        	this.getNode().notifyObservers();
        }
    }

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public void setReturnType(String value){
		this.returnType = value;
	}
	public String getReturnType() {
		return returnType;
	}

	public void setParameterList(String value){
		this.parameter = value;
	}
	public String getParameterList() {
		return parameter;
	}

	public List<Token> getThrowsList() {
		return throws_list;
	}

	public Token getFirstToken() {
		return firstToken;
	}

	public void setFirstToken(Token firstToken) {
		this.firstToken = firstToken;
	}

	public Token getLastToken() {
		return lastToken;
	}

	public void setLastToken(Token lastToken) {
		this.lastToken = lastToken;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	
}
