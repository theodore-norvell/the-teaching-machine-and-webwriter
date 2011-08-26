/*
 * Created on 2009-09-12 by Theodore S. Norvell. 
 */
package demo.model;

import higraph.model.abstractClasses.* ;

public class DemoSubgraph
extends AbstractSubgraph<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>
implements DemoHigraph
{

    protected DemoSubgraph(DemoWholeGraph wholeGraph) {
        super(wholeGraph);
    }
    
    protected DemoSubgraph getThis() { return this ; }

}
