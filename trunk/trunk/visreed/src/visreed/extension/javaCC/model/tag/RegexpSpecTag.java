/**
 * RegexpSpecTag.java
 * 
 * @date: Oct 21, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.extension.javaCC.model.payload.RegexpSpecPayload;
import visreed.model.VisreedPayload;

/**
 * @author Xiaoyu Guo
 */
public class RegexpSpecTag extends LexicalTag {
	
	private RegexpSpecTag(){
		super(TagCategory.SINGLE_SEQ_CHILD);
	}
	
	private static RegexpSpecTag instance = new RegexpSpecTag();
	protected static RegexpSpecTag getInstance(){
		return instance;
	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new RegexpSpecPayload();
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "RegSpec";
	}

}
