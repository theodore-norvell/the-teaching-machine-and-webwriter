package play.tags;

import higraph.model.abstractTaggedClasses.AbstractTaggedEdge;
import play.model.EdgePLAY;
import play.model.EdgePayloadPLAY;
import play.model.NodePLAY;
import play.model.NodePayloadPLAY;
import play.model.SubGraphPLAY;
import play.model.WholeGraphPLAY;

public class TaggedEdgePLAY
		extends
		AbstractTaggedEdge<PLAYTags, NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> {

	protected TaggedEdgePLAY(NodePLAY source, NodePLAY target,
			EdgePayloadPLAY label, WholeGraphPLAY higraph) {
		super(source, target, label, higraph);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected EdgePLAY getThis() {
		// TODO Auto-generated method stub
		return (EdgePLAY) this;
	}


}
