/**
 * LexicalAlternationTag.java
 * 
 * @date: Nov 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.model.tag.AlternationTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class LexicalAlternationTag extends AlternationTag {
	protected LexicalAlternationTag() {
		super();
	}
	private static final LexicalAlternationTag instance = new LexicalAlternationTag();
	protected static LexicalAlternationTag getInstance(){
		return instance;
	}
}
