/**
 * SyntaxViewFactory.java
 * 
 * @date: 2011-6-14
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.view;

import higraph.view.HigraphView;
import higraph.view.ViewFactory;

import java.awt.Component;

import regex.model.RegexEdge;
import regex.model.RegexEdgeLabel;
import regex.model.RegexHigraph;
import regex.model.RegexNode;
import regex.model.RegexPayload;
import regex.model.RegexSubgraph;
import regex.model.RegexWholeGraph;
import tm.backtrack.BTTimeManager;

/**
 * @author Xiaoyu Guo
 *
 */
public class SyntaxViewFactory 
    extends ViewFactory<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge>
{
    public SyntaxViewFactory(BTTimeManager tm) {
        super(tm);
    }

    @Override
    public RegexHigraphView makeHigraphView(RegexHigraph sg, Component display) {
        return new RegexHigraphView(this, sg, display, this.timeMan ) ;
    }
    
    @Override 
    public RegexNodeView makeNodeView(
        HigraphView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> sgv,
        RegexNode node
    ) {
        // create actual view according to the payload
        return new SyntaxNodeView(sgv, node, this.timeMan);
    }
}
