/**
 * SyntaxViewFactory.java
 * 
 * @date: 2011-6-14
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.regex.view;

import higraph.view.HigraphView;
import higraph.view.ViewFactory;

import java.awt.Component;

import tm.backtrack.BTTimeManager;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.view.VisreedHigraphView;
import visreed.view.VisreedNodeView;

/**
 * @author Xiaoyu Guo
 *
 */
public class SyntaxViewFactory 
    extends ViewFactory<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>
{
    public SyntaxViewFactory(BTTimeManager tm) {
        super(tm);
    }

    @Override
    public VisreedHigraphView makeHigraphView(VisreedHigraph sg, Component display) {
        return new VisreedHigraphView(this, sg, display, this.timeMan ) ;
    }
    
    @Override 
    public VisreedNodeView makeNodeView(
        HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> sgv,
        VisreedNode node
    ) {
        // create actual view according to the payload
        return new SyntaxNodeView(sgv, node, this.timeMan);
    }
}
