/*
 * Created on 2009-09-08 by Theodore S. Norvell. 
 */
package demo;

import java.awt.Component;

import tm.backtrack.BTTimeManager;

import demo.model.*;
import higraph.view.HigraphView;

public class DemoHigraphView
extends HigraphView<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>
{

    protected DemoHigraphView(DemoViewFactory vf, DemoHigraph theGraph, Component component, BTTimeManager timeMan) {
        super(vf, theGraph, component, timeMan);
    }

    private static final long serialVersionUID = -4569649217212886635L;

    

}
