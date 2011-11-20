/**
 * CommentPayload.java
 * 
 * @date: Aug 21, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.payload;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import visreed.extension.javaCC.model.tag.JavaCCTag;
import visreed.extension.javaCC.parser.JavaCCBuilder;
import visreed.extension.javaCC.parser.Token;
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
public class JavaCodeBlockPayload extends TerminalPayload {
	
	private static final int MAX_INLINE_BLOCK_LENGTH = 30;

    private Token firstToken = null, lastToken = null;

    public JavaCodeBlockPayload() {
        super(JavaCCTag.GRAMMAR_TERMINAL);
    }
    
    public JavaCodeBlockPayload(String code){
        super(JavaCCTag.GRAMMAR_TERMINAL, code);
    }
    
    /** The maximum length of the code, for display */
    public static final int MAX_DESC_CODE_DISPLAY_LENGTH = 10;

    /* (non-Javadoc)
     * @see visreed.model.VisreedPayload#format(visreed.model.VisreedNode)
     */
    @Override
    public String format(VisreedNode currentNode) {
        String result = "";
        if(this.terminal == null){
            result = "null";
        } else if (this.terminal.length() == 0){
            result = "\"\"";
        } else if (this.terminal.length() < MAX_DESC_CODE_DISPLAY_LENGTH){
            result = this.terminal;
        } else {
            result = this.terminal.substring(0, MAX_DESC_CODE_DISPLAY_LENGTH - 1);
            result += "...";
        }
        return result;
    }
    
    @Override
    public StringBuffer dump(StringBuffer sb, int indentLevel){
    	sb = JavaCCBuilder.dumpPrefix(sb, indentLevel);
    	sb.append("{");
    	if(this.terminal.length() > MAX_INLINE_BLOCK_LENGTH){
    		sb.append("\n");
    		JavaCCBuilder.dumpPrefix(sb, indentLevel + 1);
    	}
    	
    	sb.append(this.terminal);
    	
    	if(this.terminal.length() > MAX_INLINE_BLOCK_LENGTH){
    		sb.append("\n");
    		JavaCCBuilder.dumpPrefix(sb, indentLevel);
    	}
    	sb.append("}");
		return sb;
    }
    
    @Override
    public String getDescription(){
        return "Java Code Block";
    }

    /* (non-Javadoc)
     * @see visreed.model.VisreedPayload#constructView(higraph.view.HigraphView, visreed.model.VisreedNode, tm.backtrack.BTTimeManager)
     */
    @Override
    public VisreedNodeView constructView(
        HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> sgv,
        VisreedNode node,
        BTTimeManager timeman
    ) {
        return new TerminalNodeView(sgv, node, timeman);
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
}
