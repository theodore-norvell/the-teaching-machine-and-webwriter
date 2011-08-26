/*
 * Created on 2009-09-08 by Theodore S. Norvell. 
 */
package higraphTests.model;

import higraph.model.abstractClasses.* ;

public class TestEdge
extends AbstractEdge<TestPayload, TestEdgeLabel, TestHigraph, TestWholeGraph, TestSubgraph, TestNode, TestEdge>
{

    protected TestEdge(TestNode source, TestNode target, TestEdgeLabel label,
            TestWholeGraph higraph) {
        super(source, target, label, higraph);
    }

    @Override
    protected TestEdge getThis() { return this ; }

}
