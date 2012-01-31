/*
 * Created on 2009-11-11 by Theodore S. Norvell. 
 */
package demo.view;

import java.awt.Component;

import tm.backtrack.BTTimeManager;

import demo.model.*;
import higraph.view.HigraphView;
import higraph.view.ViewFactory;

public class DemoViewFactory
    extends ViewFactory<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>
{
    
    public DemoViewFactory( BTTimeManager tm ) {
        super(tm) ;
    }

    @Override public DemoHigraphView makeHigraphView(DemoHigraph sg, Component display) {
        return new DemoHigraphView( this, sg, display, this.timeMan ) ;
    }
    
    @Override public DemoNodeView makeNodeView(HigraphView<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge> hgv, DemoNode node) {
    	//TODO look into a way to eliminate this cast.
    	DemoHigraphView dhgv = (DemoHigraphView) hgv ;
    	return new DemoNodeView(dhgv, node, timeMan);
    }
}
