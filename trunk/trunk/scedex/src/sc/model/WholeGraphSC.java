package sc.model;

import java.util.List;

import higraph.model.abstractClasses.AbstractWholeGraph;

public class WholeGraphSC extends AbstractWholeGraph
		<NodePayloadSC, EdgePayloadSC,
		WholeGraphSC, SubGraphSC,
		NodeSC, EdgeSC> {

	@Override
	protected EdgeSC constructEdge(NodeSC source, NodeSC target, EdgePayloadSC label) {
		return new EdgeSC(source, target, label, this);
	}

	@Override
	protected NodeSC constructNode(WholeGraphSC higraph, NodePayloadSC payload) {
		return new NodeSC(higraph, payload);
	}

	@Override
	protected NodeSC constructNode(NodeSC original, NodeSC parent) {
		return new NodeSC(original, parent);
	}

	@Override
	protected SubGraphSC constructSubGraph() {
		return new SubGraphSC(this);
	}

	@Override
	public WholeGraphSC getWholeGraph() {
		return this;
	}
	
	public NodeSC getNode(String name){
		List<NodeSC> nodeList = getNodes();
		for(int i = 0; i < nodeList.size(); i++){
			NodeSC node = nodeList.get(i);
			if (node.getPayload().getName() == name)
				return node;
		}
		return null;
	}
	
	public EdgeSC getEdge(NodeSC source, NodeSC target){
		List<EdgeSC> edgeList = getEdges();
		for(int i = 0; i < edgeList.size(); i++){
			EdgeSC edge = edgeList.get(i);
			if (edge.getSource().equals(source) && edge.getTarget().equals(target))
				return edge;
		}
		return null;		
	}


}
