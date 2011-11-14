/**
 * LexicalKleeneStarTag.java
 * 
 * @date: Nov 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.model.tag.KleeneStarTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class LexicalKleeneStarTag extends KleeneStarTag {
	protected LexicalKleeneStarTag() {
		super();
	}
	private static final LexicalKleeneStarTag instance = new LexicalKleeneStarTag();
	protected static LexicalKleeneStarTag getInstance(){
		return instance;
	}
}
