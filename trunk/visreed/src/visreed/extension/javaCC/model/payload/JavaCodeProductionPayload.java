/**
 * JavaCodePayload.java
 * 
 * @date: Aug 21, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.payload;

import java.util.ArrayList;
import java.util.List;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import visreed.extension.javaCC.parser.Token;
import visreed.extension.javaCC.view.JavaCodeNodeView;
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
 */
public class JavaCodeProductionPayload extends ProductionPayload {

	private String code;
    public JavaCodeProductionPayload() {
        super();
        this.code = "";
    }
    
    public JavaCodeProductionPayload(String code){
    	super();
        this.code = code;
    }
    
    public String getCode(){
    	return this.code;
    }
    public void setCode(String code){
    	this.code = code;
    }
    
    
    /** The maximum length of the code, for display */
    public static final int MAX_DESC_CODE_DISPLAY_LENGTH = 10;

    /* (non-Javadoc)
     * @see visreed.model.VisreedPayload#format(visreed.model.VisreedNode)
     */
    @Override
    public String format(VisreedNode currentNode) {
        String result = "";
        String code = this.getCode();
        if(code == null){
            result = "null";
        } else if (code.length() == 0){
            result = "\"\"";
        } else if (code.length() < MAX_DESC_CODE_DISPLAY_LENGTH){
            result = code;
        } else {
            result = code.substring(0, MAX_DESC_CODE_DISPLAY_LENGTH - 1);
            result += "...";
        }
        return result;
    }
    
    @Override
    public String getDescription(){
        return "Java Code";
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
        return new JavaCodeNodeView(sgv, node, timeman);
    }

}
