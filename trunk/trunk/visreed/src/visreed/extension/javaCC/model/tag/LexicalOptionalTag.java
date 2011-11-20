/**
 * LexicalOptionalTag.java
 * 
 * @date: Nov 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.model.VisreedPayload;
import visreed.model.payload.OptionalPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class LexicalOptionalTag extends LexicalTag {
	protected LexicalOptionalTag() {
		super(TagCategory.SINGLE_SEQ_CHILD);
	}
	private static final LexicalOptionalTag instance = new LexicalOptionalTag();
	protected static LexicalOptionalTag getInstance(){
		return instance;
	}
	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new OptionalPayload(this);
	}
	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "LOPT";
	}
}
