package tagsc;

import higraph.model.abstractTaggedClasses.AbstractTaggedEdge;




public class TaggedEdgeSC extends AbstractTaggedEdge
                                  < SCTag,TaggedPayloadSC, 
                                    EdgePayloadSC,TaggedWholeGraphSC,
                                    TaggedSubGraphSC, TaggedNodeSC, TaggedEdgeSC>
                                {

protected TaggedEdgeSC(TaggedNodeSC source, TaggedNodeSC target, EdgePayloadSC label,
        TaggedWholeGraphSC higraph) {
    super(source, target, label, higraph);
}

@Override
protected TaggedEdgeSC getThis() { return this ; }
}
