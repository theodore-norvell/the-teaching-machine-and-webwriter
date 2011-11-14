/**
 * LexicalOptionalTag.java
 * 
 * @date: Nov 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.model.tag.OptionalTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class LexicalOptionalTag extends OptionalTag {
	protected LexicalOptionalTag() {
		super();
	}
	private static final LexicalOptionalTag instance = new LexicalOptionalTag();
	protected static LexicalOptionalTag getInstance(){
		return instance;
	}
}
