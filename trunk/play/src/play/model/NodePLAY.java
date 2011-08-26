package play.model;

import java.util.List;

import play.tags.PLAYTags;
import play.tags.TaggedNodePLAY;

public class NodePLAY extends TaggedNodePLAY {


	public NodePLAY(NodePLAY original, NodePLAY parent) {
		super(original, parent);
	}

	@Override
	public boolean canInsertChild(int position, PLAYTags tag) {
		if( 0 <= position && position <= getNumberOfChildren() ) {
            List<PLAYTags> tagList = getTagList() ;
            tagList.add(position, tag) ;
            return getTag().contentModel( tagList ) ; }
        else return false ;
	}

	@Override
	public void insertChild(int position, PLAYTags tag) {
		assert( canInsertChild(position, tag) ) ;
        NodePLAY newNode = getWholeGraph().makeRootNode(tag.defaultPayload()) ;
        insertChild( position, newNode ) ;
	}

	public NodePLAY(WholeGraphPLAY higraph, NodePayloadPLAY payload) {
		super(higraph, payload);
	}

	@Override
	protected NodePLAY getThis() {
		return this;
	}

	public int findIndex() {
		if (this.parent != null) {
			NodePLAY parent = (NodePLAY) this.parent;
			for (int i = 0; i < parent.getNumberOfChildren(); i++) {
				if (this == parent.getChild(i))
					return i;
			}
		}
		return -1;
	}

}
