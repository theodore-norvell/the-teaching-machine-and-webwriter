/*
 * Created on 2009-09-08 by Theodore S. Norvell. 
 */
package higraphTests.model;

import tm.backtrack.BTTimeManager;
import higraph.model.abstractClasses.* ;


public class TestWholeGraph
extends AbstractWholeGraph<TestPayload, TestEdgeLabel, TestHigraph, TestWholeGraph, TestSubgraph, TestNode, TestEdge>
implements AbstractHigraph<TestPayload, TestEdgeLabel, TestHigraph, TestWholeGraph, TestSubgraph, TestNode, TestEdge>
{
    public TestWholeGraph( BTTimeManager timeMan ) {
        super(timeMan) ;
    }

    @Override
    protected TestEdge constructEdge(TestNode source, TestNode target,
            TestEdgeLabel label) {
        return new TestEdge( source, target, label, this ) ;
    }

    @Override
    protected TestNode constructNode(TestWholeGraph higraph, TestPayload payload) {
        return new TestNode( higraph, payload ) ;
    }

    @Override
    protected TestNode constructNode(TestNode original, TestNode parent) {
        return new TestNode( original, parent ) ;
    }

    @Override
    protected TestSubgraph constructSubGraph() {
        return new TestSubgraph(this) ;
    }

    @Override
    public TestWholeGraph getWholeGraph() {
        return this;
    }

}
