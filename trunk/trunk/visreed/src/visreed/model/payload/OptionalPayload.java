/**
 * OptionalPayload.java
 * 
 * @date: Aug 1, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model.payload;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import tm.utilities.Assert;
import visreed.extension.javaCC.parser.JavaCCBuilder;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.tag.VisreedTag;
import visreed.view.OptionalNodeView;
import visreed.view.VisreedNodeView;

/**
 * @author Xiaoyu Guo
 *
 */
public class OptionalPayload extends VisreedPayload {

    /**
     * @param tag
     */
    public OptionalPayload() {
        super(VisreedTag.OPTIONAL);
    }
    
    /* (non-Javadoc)
     * @see visreed.model.VisreedPayload#copy()
     */
    @Override
    public OptionalPayload copy() {
        return this;
    }
    
    @Override
    public StringBuffer dump(StringBuffer sb, int indentLevel){
    	sb.append('[');
    	sb.append('\n');
    	JavaCCBuilder.dumpPrefix(sb, indentLevel + 1);
    	this.getNode().getChild(0).getPayload().dump(sb, indentLevel + 1);
    	sb.append('\n');
    	JavaCCBuilder.dumpPrefix(sb, indentLevel);
    	sb.append(']');
    	sb.append('\n');
    	return sb;
    }

    /* (non-Javadoc)
     * @see visreed.model.VisreedPayload#format(visreed.model.RegexNode)
     */
    @Override
    public String format(VisreedNode currentNode) {
        StringBuilder result = new StringBuilder();
        int numOfChildren = currentNode.getNumberOfChildren();
        // Optional -> Sequence
        Assert.check(numOfChildren == 1);
            
        VisreedNode currentChildN = currentNode.getChild(0);
        VisreedPayload currentChildPl = currentChildN.getPayload();
        
        result.append(currentChildPl.format(currentChildN));
        
        if(result.length() > 1){
            result.insert(0, "(");
            result.append(")");
        }
        result.append("?");
    
        return result.toString();
    }

    /* (non-Javadoc)
     * @see visreed.model.VisreedPayload#constructView(higraph.view.HigraphView, visreed.model.RegexNode, tm.backtrack.BTTimeManager)
     */
    @Override
    public VisreedNodeView constructView(
        HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> sgv,
        VisreedNode node, BTTimeManager timeman
    ) {
        return new OptionalNodeView(sgv, node, timeman);
    }

}
