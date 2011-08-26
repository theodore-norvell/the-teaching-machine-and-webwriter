/*
 * Created on 2009-09-08 by Theodore S. Norvell. 
 */
package demo.model;

import higraph.model.abstractClasses.* ;

public class DemoNode
extends AbstractNode<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>
{

    protected DemoNode(DemoWholeGraph higraph, DemoPayload payload) {
        super(higraph, payload);
    }
    
    protected DemoNode(DemoNode original, DemoNode parent) {
        super( original, parent) ;
    }

    @Override
    protected DemoNode getThis() { return this ; }

}
