/*
 * Created on 2009-09-08 by Theodore S. Norvell. 
 */
package demo.model;

import tm.backtrack.BTTimeManager;
import higraph.model.abstractClasses.* ;


public class DemoWholeGraph
extends AbstractWholeGraph<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>
implements DemoHigraph
{

    public DemoWholeGraph(BTTimeManager timeMan) {
        super(timeMan);
    }

    @Override
    protected DemoEdge constructEdge(DemoNode source, DemoNode target,
            DemoEdgeLabel label) {
        return new DemoEdge( source, target, label, this ) ;
    }

    @Override
    protected DemoNode constructNode(DemoWholeGraph higraph, DemoPayload payload) {
        return new DemoNode( higraph, payload ) ;
    }

    @Override
    protected DemoNode constructNode(DemoNode original, DemoNode parent) {
        return new DemoNode( original, parent ) ;
    }

    @Override
    protected DemoSubgraph constructSubGraph() {
        return new DemoSubgraph(this) ;
    }

    @Override
    public DemoWholeGraph getWholeGraph() {
        return this;
    }

}
