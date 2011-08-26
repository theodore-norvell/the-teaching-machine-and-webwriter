package play.tags;

import higraph.model.abstractTaggedClasses.AbstractTaggedSubgraph;
import play.model.EdgePLAY;
import play.model.EdgePayloadPLAY;
import play.model.NodePLAY;
import play.model.NodePayloadPLAY;
import play.model.SubGraphPLAY;
import play.model.WholeGraphPLAY;

public class TaggedSubGraphPLAY
		extends
		AbstractTaggedSubgraph<PLAYTags, NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> {

	protected TaggedSubGraphPLAY(WholeGraphPLAY wholeGraph) {
		super(wholeGraph);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected SubGraphPLAY getThis() {
		// TODO Auto-generated method stub
		return (SubGraphPLAY) this;
	}

}
