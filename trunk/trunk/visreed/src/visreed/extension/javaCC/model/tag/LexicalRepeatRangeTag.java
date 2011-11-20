/**
 * LexicalRepeatRangeTag.java
 * 
 * @date: Nov 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.model.VisreedPayload;
import visreed.model.payload.RepeatRangePayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class LexicalRepeatRangeTag extends LexicalTag {
	protected LexicalRepeatRangeTag() {
		super(TagCategory.SINGLE_SEQ_CHILD);
	}
	private static final LexicalRepeatRangeTag instance = new LexicalRepeatRangeTag();
	protected static LexicalRepeatRangeTag getInstance(){
		return instance;
	}
	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new RepeatRangePayload(this);
	}
	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "LRPN";
	}
}
