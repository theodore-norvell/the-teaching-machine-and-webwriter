/*
 * Created on 2009-09-12 by Theodore S. Norvell. 
 */
package higraphTests.model;

import higraph.model.abstractClasses.* ;

public class TestSubgraph
extends AbstractSubgraph<TestPayload, TestEdgeLabel, TestHigraph, TestWholeGraph, TestSubgraph, TestNode, TestEdge>
implements AbstractHigraph<TestPayload, TestEdgeLabel, TestHigraph, TestWholeGraph, TestSubgraph, TestNode, TestEdge>
{

    protected TestSubgraph(TestWholeGraph wholeGraph) {
        super(wholeGraph);
    }
    
    protected TestSubgraph getThis() { return this ; }

}
