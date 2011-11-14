/**
 * LexicalKleenePlusTag.java
 * 
 * @date: Nov 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.model.tag.KleenePlusTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class LexicalKleenePlusTag extends KleenePlusTag {
	protected LexicalKleenePlusTag() {
		super();
	}
	private static final LexicalKleenePlusTag instance = new LexicalKleenePlusTag();
	protected static LexicalKleenePlusTag getInstance(){
		return instance;
	}

}
