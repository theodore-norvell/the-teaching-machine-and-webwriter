/**
 * GrammarRepeatRangeTag.java
 * 
 * @date: Nov 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.model.tag.RepeatRangeTag;
import visreed.model.tag.VisreedTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class GrammarRepeatRangeTag extends RepeatRangeTag {
	protected GrammarRepeatRangeTag() {
		super();
	}
	private static final GrammarRepeatRangeTag instance = new GrammarRepeatRangeTag();
	protected static GrammarRepeatRangeTag getInstance(){
		return instance;
	}
	
	@Override
	protected VisreedTag[] getAcceptableChildTags(){
		return JavaCCTag.CHILD_TAG_NON_SEQ_GRA;
	}
}
