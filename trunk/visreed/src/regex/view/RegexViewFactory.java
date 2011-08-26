
package regex.view;

import higraph.view.HigraphView;
import higraph.view.NodeView;
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
import tm.utilities.Assert;

/**
 * @author Xiaoyu Guo
 *
 */
public class RegexViewFactory
extends ViewFactory<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge>
{
    
    public RegexViewFactory( BTTimeManager tm ) {
        super(tm) ;
    }

    @Override
    public RegexHigraphView makeHigraphView(RegexHigraph sg, Component display) {
        return new RegexHigraphView( this, sg, display, this.timeMan ) ;
    }
    
    @Override 
    public RegexNodeView makeNodeView(
        HigraphView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> sgv,
        RegexNode node
    ) {
        // create actual view according to the payload
        Assert.check(node != null);
        RegexPayload pl = node.getPayload();
        RegexNodeView result = pl.constructView(sgv, node, this.timeMan);
        result.setNodeViewFactory(this);
        return result;
    }
    
    @Override
    public RegexDropZone makeDropZone(NodeView <RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> nv) {
        return new RegexDropZone(nv, timeMan) ;
    }

    public RegexDropZone makeInsertChildDropZone(RegexNodeView nv) {
        return new InsertChildDropZone(nv, timeMan);
    }
    
}
