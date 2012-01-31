/*
 * Created on 2009-11-18 by Theodore S. Norvell. 
 */
package demo.view;

import tm.backtrack.BTTimeManager;

import higraph.view.NodeView;
import demo.model.*;

// TODO  So far this class isn't really needed.
// I added it to see if subclassing the views actually can be done.
public class DemoNodeView extends NodeView<DemoPayload, DemoEdgeLabel,  DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>
{

    protected DemoNodeView(DemoHigraphView v, DemoNode node,  BTTimeManager timeMan) {
        super(v, node, timeMan);
    }
}
