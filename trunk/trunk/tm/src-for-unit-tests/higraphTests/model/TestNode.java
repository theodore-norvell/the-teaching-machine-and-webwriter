/*
 * Created on 2009-09-08 by Theodore S. Norvell. 
 */
package higraphTests.model;

import higraph.model.abstractClasses.* ;

public class TestNode
extends AbstractNode<TestPayload, TestEdgeLabel, TestHigraph, TestWholeGraph, TestSubgraph, TestNode, TestEdge>
{

    protected TestNode(TestWholeGraph higraph, TestPayload payload) {
        super(higraph, payload);
    }
    
    protected TestNode(TestNode original, TestNode parent) {
        super( original, parent) ;
    }

    @Override
    protected TestNode getThis() { return this ; }

}
