package sc.model;

import higraph.model.abstractClasses.AbstractNode;

public class NodeSC extends AbstractNode
          <NodePayloadSC, EdgePayloadSC, 
          WholeGraphSC, SubGraphSC,
          NodeSC, EdgeSC>{ 
	
    protected NodeSC(WholeGraphSC higraph, NodePayloadSC payload) {
        super(higraph, payload);
    }
    
    protected NodeSC(NodeSC original, NodeSC parent) {
        super( original, parent) ;
    }

	
    @Override
    protected NodeSC getThis() { return this ; }

}
