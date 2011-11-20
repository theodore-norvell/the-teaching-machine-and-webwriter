/**
 * LexicalKleenePlusTag.java
 * 
 * @date: Nov 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.model.VisreedPayload;
import visreed.model.payload.KleenePlusPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class LexicalKleenePlusTag extends LexicalTag {
	protected LexicalKleenePlusTag() {
		super(TagCategory.SINGLE_SEQ_CHILD);
	}
	private static final LexicalKleenePlusTag instance = new LexicalKleenePlusTag();
	protected static LexicalKleenePlusTag getInstance(){
		return instance;
	}
	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new KleenePlusPayload(this);
	}
	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "LKLN+";
	}

}
