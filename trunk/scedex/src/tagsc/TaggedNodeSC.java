package tagsc;

import higraph.model.abstractTaggedClasses.AbstractTaggedNode;


public class TaggedNodeSC extends AbstractTaggedNode
                                  <SCTag,TaggedPayloadSC,
                                   EdgePayloadSC,TaggedWholeGraphSC,
                                   TaggedSubGraphSC, TaggedNodeSC, TaggedEdgeSC>{

	protected TaggedNodeSC(TaggedNodeSC original, TaggedNodeSC parent) {
		super(original, parent);
		}
	protected TaggedNodeSC(TaggedWholeGraphSC twholegraph, TaggedPayloadSC tpayload) {
		super(twholegraph, tpayload);
		}
	
	protected TaggedNodeSC getThis() {
		
		return this;
	}

}
