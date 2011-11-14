/**
 * GrammarOptionalTag.java
 * 
 * @date: Nov 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.model.tag.OptionalTag;
import visreed.model.tag.VisreedTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class GrammarOptionalTag extends OptionalTag {
	protected GrammarOptionalTag() {
		super();
	}
	private static final GrammarOptionalTag instance = new GrammarOptionalTag();
	protected static GrammarOptionalTag getInstance(){
		return instance;
	}
	
	@Override
	protected VisreedTag[] getAcceptableChildTags(){
		return JavaCCTag.CHILD_TAG_NON_SEQ_GRA;
	}
}
