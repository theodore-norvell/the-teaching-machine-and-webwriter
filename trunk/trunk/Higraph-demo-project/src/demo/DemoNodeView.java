/*
 * Created on 2009-11-18 by Theodore S. Norvell. 
 */
package demo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Stroke;
import java.awt.geom.RectangularShape;

import tm.backtrack.BTTimeManager;

import higraph.view.NodeView;
import higraph.view.HigraphView;
import higraph.view.ViewFactory;
import demo.model.*;

// TODO  So far this class isn't really needed.
// I added it to see if subclassing the views actually can be done.
public class DemoNodeView extends NodeView<DemoPayload, DemoEdgeLabel,  DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>
{

    protected DemoNodeView(HigraphView v, DemoNode node,  BTTimeManager timeMan) {
        super(v, node, timeMan);
    }
}
