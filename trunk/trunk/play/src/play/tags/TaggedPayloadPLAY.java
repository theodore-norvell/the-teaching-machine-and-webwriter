package play.tags;

import higraph.model.taggedInterfaces.TaggedPayload;
import play.model.NodePayloadPLAY;

/**
 * @author  Charles
 */
public class TaggedPayloadPLAY implements
		TaggedPayload<PLAYTags, NodePayloadPLAY> {

	/**
	 * @uml.property  name="tag"
	 * @uml.associationEnd  
	 */
	private PLAYTags tag;
	/**
	 * @uml.property  name="name"
	 */
	private String name;

	public TaggedPayloadPLAY(String name, PLAYTags tag) {
		this.tag = tag;
		this.name = name;
	}

	/**
	 * @return
	 * @uml.property  name="tag"
	 */
	@Override
	public PLAYTags getTag() {
		// TODO Auto-generated method stub
		return tag;
	}

	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		return this.name;
	}

	@Override
	public NodePayloadPLAY copy() {
		// TODO Auto-generated method stub
		return null;
	}

}
