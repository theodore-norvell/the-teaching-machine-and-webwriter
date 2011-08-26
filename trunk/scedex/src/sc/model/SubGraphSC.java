package sc.model;


import higraph.model.abstractClasses.AbstractSubgraph;



public class SubGraphSC extends AbstractSubgraph 
                   <NodePayloadSC, EdgePayloadSC, 
                    WholeGraphSC, SubGraphSC,
                    NodeSC, EdgeSC>{

	protected SubGraphSC (WholeGraphSC wholeGraph){
		super(wholeGraph);
	}

	@Override
	protected SubGraphSC getThis() {
		return this;
	}
}
