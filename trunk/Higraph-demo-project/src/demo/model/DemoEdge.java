/*
 * Created on 2009-09-08 by Theodore S. Norvell. 
 */
package demo.model;

import higraph.model.abstractClasses.* ;

public class DemoEdge
extends AbstractEdge<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>
{

    protected DemoEdge(DemoNode source, DemoNode target, DemoEdgeLabel label,
            DemoWholeGraph higraph) {
        super(source, target, label, higraph);
    }

    @Override
    protected DemoEdge getThis() { return this ; }

}
