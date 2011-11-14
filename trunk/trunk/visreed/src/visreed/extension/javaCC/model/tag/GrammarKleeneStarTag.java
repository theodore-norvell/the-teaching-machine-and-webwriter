/**
 * GrammarKleeneStarTag.java
 * 
 * @date: Nov 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.model.tag.KleeneStarTag;
import visreed.model.tag.VisreedTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class GrammarKleeneStarTag extends KleeneStarTag {
	protected GrammarKleeneStarTag() {
		super();
	}
	private static final GrammarKleeneStarTag instance = new GrammarKleeneStarTag();
	protected static GrammarKleeneStarTag getInstance(){
		return instance;
	}
	
	@Override
	protected VisreedTag[] getAcceptableChildTags(){
		return JavaCCTag.CHILD_TAG_NON_SEQ_GRA;
	}
}
