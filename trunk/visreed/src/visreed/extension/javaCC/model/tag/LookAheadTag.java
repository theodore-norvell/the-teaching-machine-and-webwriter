/**
 * LookAheadTag.java
 * 
 * @date: Nov 2, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.extension.javaCC.model.payload.LookAheadPayload;
import visreed.model.VisreedPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class LookAheadTag extends GrammarTag {

	LookAheadTag(){
		super(TagCategory.SINGLE_SEQ_CHILD);
	}
	
	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new LookAheadPayload();
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "LOOKAHEAD";
	}

	
	private static final LookAheadTag instance = new LookAheadTag();
	
	protected static final LookAheadTag getInstance(){
		return instance;
	}

}
