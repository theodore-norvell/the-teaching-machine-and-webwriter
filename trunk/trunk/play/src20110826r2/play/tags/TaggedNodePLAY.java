package play.tags;

import higraph.model.abstractTaggedClasses.AbstractTaggedNode;
import play.model.EdgePLAY;
import play.model.EdgePayloadPLAY;
import play.model.NodePLAY;
import play.model.NodePayloadPLAY;
import play.model.SubGraphPLAY;
import play.model.WholeGraphPLAY;

public class TaggedNodePLAY
		extends
		AbstractTaggedNode<PLAYTags, NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> {

	protected TaggedNodePLAY(NodePLAY original, NodePLAY parent) {
		super(original, parent);
		// TODO Auto-generated constructor stub
	}

	protected TaggedNodePLAY(WholeGraphPLAY higraph, NodePayloadPLAY payload) {
		super(higraph, payload);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected NodePLAY getThis() {
		// TODO Auto-generated method stub
		return (NodePLAY) this;
	}

	

}
