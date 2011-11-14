/**
 * LexicalRepeatRangeTag.java
 * 
 * @date: Nov 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.model.tag.RepeatRangeTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class LexicalRepeatRangeTag extends RepeatRangeTag {
	protected LexicalRepeatRangeTag() {
		super();
	}
	private static final LexicalRepeatRangeTag instance = new LexicalRepeatRangeTag();
	protected static LexicalRepeatRangeTag getInstance(){
		return instance;
	}
}
