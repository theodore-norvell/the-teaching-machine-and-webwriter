/**
 * GrammarKleenePlusTag.java
 * 
 * @date: Nov 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.model.tag.KleenePlusTag;
import visreed.model.tag.VisreedTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class GrammarKleenePlusTag extends KleenePlusTag {
	protected GrammarKleenePlusTag() {
		super();
	}
	private static final GrammarKleenePlusTag instance = new GrammarKleenePlusTag();
	protected static GrammarKleenePlusTag getInstance(){
		return instance;
	}
	
	@Override
	protected VisreedTag[] getAcceptableChildTags(){
		return JavaCCTag.CHILD_TAG_NON_SEQ_GRA;
	}
}

