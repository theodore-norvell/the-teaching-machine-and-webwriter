/**
 * LexicalSequenceTag.java
 * 
 * @date: Nov 2, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import java.util.List;

import visreed.model.tag.SequenceTag;
import visreed.model.tag.VisreedTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class LexicalSequenceTag extends SequenceTag {
	protected LexicalSequenceTag(){
		super();
	}
	private static final LexicalSequenceTag instance = new LexicalSequenceTag();
	protected static LexicalSequenceTag getInstance(){
		return instance;
	}
	
	protected static final VisreedTag[] LSEQ_CHILD = {
		JavaCCTag.GRAMMAR_ALTERNATION,
		JavaCCTag.LOOKAHEAD,
		TERMINAL,
		JavaCCTag.LINK,
		CHARACTER_LIST,
		JavaCCTag.GRAMMAR_KLEENE_PLUS,
		JavaCCTag.GRAMMAR_KLEENE_STAR,
		JavaCCTag.GRAMMAR_OPTIONAL,
		JavaCCTag.GRAMMAR_REPEAT_RANGE
	};
	
	@Override
	protected VisreedTag[] getAcceptableChildTags(){
		return LSEQ_CHILD;
	}
	
	@Override
	protected boolean contentModelHook(final List<VisreedTag> childTags){
    	return true;
    }
}
