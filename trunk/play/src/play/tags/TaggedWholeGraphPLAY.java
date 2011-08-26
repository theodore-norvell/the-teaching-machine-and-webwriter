package play.tags;

import higraph.model.abstractTaggedClasses.AbstractTaggedWholeGraph;
import play.model.EdgePLAY;
import play.model.EdgePayloadPLAY;
import play.model.NodePLAY;
import play.model.NodePayloadPLAY;
import play.model.SubGraphPLAY;
import play.model.WholeGraphPLAY;

public class TaggedWholeGraphPLAY
		extends
		AbstractTaggedWholeGraph<PLAYTags, NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> {

	@Override
	public WholeGraphPLAY getWholeGraph() {
		// TODO Auto-generated method stub
		return (WholeGraphPLAY) this;
	}

	@Override
	protected EdgePLAY constructEdge(NodePLAY source, NodePLAY target,
			EdgePayloadPLAY label) {
		// TODO Auto-generated method stub
		WholeGraphPLAY wg = (WholeGraphPLAY) this;
		return new EdgePLAY(source, target, label, wg);
	}

	@Override
	protected NodePLAY constructNode(WholeGraphPLAY higraph,
			NodePayloadPLAY payload) {
		// TODO Auto-generated method stub
		return new NodePLAY(higraph, payload);
	}

	@Override
	protected NodePLAY constructNode(NodePLAY original, NodePLAY parent) {
		// TODO Auto-generated method stub
		return new NodePLAY(original, parent);
	}

	@Override
	protected SubGraphPLAY constructSubGraph() {
		// TODO Auto-generated method stub
		WholeGraphPLAY wg = (WholeGraphPLAY) this;
		return new SubGraphPLAY(wg);
	}

}
