package play.model;

import higraph.view.SubgraphView;

import java.util.ArrayList;
import java.util.List;

import play.tags.TaggedWholeGraphPLAY;
import play.view.SubGraphViewPLAY;

public class WholeGraphPLAY extends TaggedWholeGraphPLAY {

    // TODO I don't think we should have this field.
    // It is used so that we can find all subgraph views from
    // the whole graph when it comes time to refresh.
    // The whole refresh process needs to be rethought.
	private List<SubGraphViewPLAY> listSGV = new ArrayList<SubGraphViewPLAY>();

	@Override
	public SubGraphViewPLAY getView(int id) {
		return listSGV.get(id);
	}

	public int registerSubgraphView(
			SubgraphView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> view) {
		listSGV.add((SubGraphViewPLAY) view);
		return listSGV.size() - 1;
	}

	@Override
	protected EdgePLAY constructEdge(NodePLAY source, NodePLAY target,
			EdgePayloadPLAY label) {
		// TODO Auto-generated method stub
		return new EdgePLAY(source, target, label, this);
	}

	@Override
	protected NodePLAY constructNode(NodePLAY original, NodePLAY parent) {
		// TODO Auto-generated method stub
		return new NodePLAY(original, parent);
	}

	@Override
	protected NodePLAY constructNode(WholeGraphPLAY higraph,
			NodePayloadPLAY payload) {
		// TODO Auto-generated method stub
		return new NodePLAY(higraph, payload);
	}

	@Override
	protected SubGraphPLAY constructSubGraph() {
		// TODO Auto-generated method stub
		return new SubGraphPLAY(this);
	}

	@Override
	public WholeGraphPLAY getWholeGraph() {
		// TODO Auto-generated method stub
		return this;
	}

	public NodePLAY getNode(String name) {
		List<NodePLAY> nodeList = getNodes();
		for (int i = 0; i < nodeList.size(); i++) {
			NodePLAY node = (NodePLAY) nodeList.get(i);
			if (node.getPayload().getName() == name)
				return node;
		}
		return null;
	}

	public EdgePLAY getEdge(NodePLAY source, NodePLAY target) {
		List<EdgePLAY> edgeList = getEdges();
		for (int i = 0; i < edgeList.size(); i++) {
			EdgePLAY edge = (EdgePLAY) edgeList.get(i);
			if (edge.getSource().equals(source)
					&& edge.getTarget().equals(target))
				return edge;
		}
		return null;
	}
	
	public List<SubGraphViewPLAY> getSgvList(){
		return listSGV;
	}

}
