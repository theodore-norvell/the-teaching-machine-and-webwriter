package tagsc;

import higraph.model.abstractTaggedClasses.AbstractTaggedWholeGraph;

public class TaggedWholeGraphSC extends AbstractTaggedWholeGraph
                                        < SCTag,TaggedPayloadSC,
                                          EdgePayloadSC,TaggedWholeGraphSC,
                                          TaggedSubGraphSC, TaggedNodeSC, TaggedEdgeSC>{

	
	protected TaggedEdgeSC constructEdge(TaggedNodeSC source,
			TaggedNodeSC target, EdgePayloadSC label) {
		    
		return new TaggedEdgeSC(target, target, label,this);
	}
    protected TaggedNodeSC constructNode(TaggedNodeSC original,
			TaggedNodeSC parent) {
		    
		return new TaggedNodeSC(original, parent);
	}

	@Override
	protected TaggedSubGraphSC constructSubGraph() {
		 return new TaggedSubGraphSC(this);
	}

	@Override
	public TaggedWholeGraphSC getWholeGraph() {
		
		return this;
	}
	@Override
	protected TaggedNodeSC constructNode(TaggedWholeGraphSC higraph,
			TaggedPayloadSC payload) {
		
		return new TaggedNodeSC(higraph, payload);
	}

}
