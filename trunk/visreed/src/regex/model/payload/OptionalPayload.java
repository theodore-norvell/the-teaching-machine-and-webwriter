/**
 * OptionalPayload.java
 * 
 * @date: Aug 1, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.model.payload;

import higraph.view.HigraphView;
import regex.model.RegexEdge;
import regex.model.RegexEdgeLabel;
import regex.model.RegexHigraph;
import regex.model.RegexNode;
import regex.model.RegexPayload;
import regex.model.RegexSubgraph;
import regex.model.RegexTag;
import regex.model.RegexWholeGraph;
import regex.view.OptionalNodeView;
import regex.view.RegexNodeView;
import tm.backtrack.BTTimeManager;
import tm.utilities.Assert;

/**
 * @author Xiaoyu Guo
 *
 */
public class OptionalPayload extends RegexPayload {

    /**
     * @param tag
     */
    public OptionalPayload() {
        super(RegexTag.OPTIONAL);
    }
    
    /* (non-Javadoc)
     * @see regex.model.RegexPayload#copy()
     */
    @Override
    public OptionalPayload copy() {
        return this;
    }

    /* (non-Javadoc)
     * @see regex.model.RegexPayload#format(regex.model.RegexNode)
     */
    @Override
    public String format(RegexNode currentNode) {
        StringBuilder result = new StringBuilder();
        int numOfChildren = currentNode.getNumberOfChildren();
        // Optional -> Sequence
        Assert.check(numOfChildren == 1);
            
        RegexNode currentChildN = currentNode.getChild(0);
        RegexPayload currentChildPl = currentChildN.getPayload();
        
        result.append(currentChildPl.format(currentChildN));
        
        if(result.length() > 1){
            result.insert(0, "(");
            result.append(")");
        }
        result.append("?");
    
        return result.toString();
    }

    /* (non-Javadoc)
     * @see regex.model.RegexPayload#constructView(higraph.view.HigraphView, regex.model.RegexNode, tm.backtrack.BTTimeManager)
     */
    @Override
    public RegexNodeView constructView(
        HigraphView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> sgv,
        RegexNode node, BTTimeManager timeman
    ) {
        return new OptionalNodeView(sgv, node, timeman);
    }

}
