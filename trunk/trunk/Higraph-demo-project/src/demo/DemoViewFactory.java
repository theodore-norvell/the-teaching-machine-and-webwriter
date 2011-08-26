/*
 * Created on 2009-11-11 by Theodore S. Norvell. 
 */
package demo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import tm.backtrack.BTTimeManager;

import demo.model.*;
import higraph.view.HigraphView;
import higraph.view.ViewFactory;

public class DemoViewFactory
    extends ViewFactory<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>
{
    
    DemoViewFactory( BTTimeManager tm ) {
        super(tm) ;
    }

    @Override public DemoHigraphView makeHigraphView(DemoHigraph sg, Component display) {
        return new DemoHigraphView( this, sg, display, this.timeMan ) ;
    }
    
    @Override public DemoNodeView makeNodeView(HigraphView<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge> hgv, DemoNode node) {
        return new DemoNodeView(hgv, node, timeMan);
    }
}
