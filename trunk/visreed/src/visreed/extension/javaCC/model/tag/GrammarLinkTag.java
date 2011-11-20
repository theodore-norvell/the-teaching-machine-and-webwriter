/**
 * GrammarLinkTag.java
 * 
 * @date: Nov 14, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.extension.javaCC.model.payload.GrammarLinkPayload;
import visreed.model.VisreedPayload;
import visreed.model.VisreedTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class GrammarLinkTag extends GrammarTag {

	protected GrammarLinkTag() {
		super(TagCategory.TERMINAL);
	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new GrammarLinkPayload("");
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "GLINK";
	}

	private static final GrammarLinkTag instance = new GrammarLinkTag();
	protected static VisreedTag getInstance() {
		return instance;
	}

}
