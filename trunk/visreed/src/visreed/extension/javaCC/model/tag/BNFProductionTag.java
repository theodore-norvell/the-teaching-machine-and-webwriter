/**
 * BNFProductionTag.java
 * 
 * @date: Nov 14, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.extension.javaCC.model.payload.BNFProductionPayload;
import visreed.model.VisreedPayload;
import visreed.model.VisreedTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class BNFProductionTag extends GrammarTag {
	protected BNFProductionTag() {
		super(TagCategory.SINGLE_SEQ_CHILD);
	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new BNFProductionPayload();
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "BNFP";
	}

	private static final VisreedTag instance = new BNFProductionTag();
	public static VisreedTag getInstance() {
		return instance;
	}

}
