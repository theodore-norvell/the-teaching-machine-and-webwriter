/**
 * GrammarKleeneStarTag.java
 * 
 * @date: Nov 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.model.VisreedPayload;
import visreed.model.payload.KleeneStarPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class GrammarKleeneStarTag extends GrammarTag {
	protected GrammarKleeneStarTag() {
		super(TagCategory.SINGLE_SEQ_CHILD);
	}
	private static final GrammarKleeneStarTag instance = new GrammarKleeneStarTag();
	protected static GrammarKleeneStarTag getInstance(){
		return instance;
	}
	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new KleeneStarPayload(this);
	}
	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "GKLN*";
	}
}
