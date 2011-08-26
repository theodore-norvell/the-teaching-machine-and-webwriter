package tagsc;

import higraph.model.abstractTaggedClasses.*;

public class TaggedSubGraphSC extends AbstractTaggedSubgraph
                                      < SCTag,TaggedPayloadSC,
                                        EdgePayloadSC,TaggedWholeGraphSC,
                                        TaggedSubGraphSC, TaggedNodeSC, TaggedEdgeSC>{

	protected TaggedSubGraphSC(TaggedWholeGraphSC wholeGraph) {
		super(wholeGraph);
		
	}

	@Override
	protected TaggedSubGraphSC getThis() {
		
		return this;
	}

}
