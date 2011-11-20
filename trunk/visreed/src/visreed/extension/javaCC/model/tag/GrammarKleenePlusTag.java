/**
 * GrammarKleenePlusTag.java
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
public class GrammarKleenePlusTag extends GrammarTag {
	protected GrammarKleenePlusTag() {
		super(TagCategory.SINGLE_SEQ_CHILD);
	}
	private static final GrammarKleenePlusTag instance = new GrammarKleenePlusTag();
	protected static GrammarKleenePlusTag getInstance(){
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
		return "GKLN+";
	}
}

