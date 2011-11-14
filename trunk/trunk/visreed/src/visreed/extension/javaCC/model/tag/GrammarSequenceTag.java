/**
 * GrammarSequenceTag.java
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
public class GrammarSequenceTag extends SequenceTag {
	protected GrammarSequenceTag(){
		super();
	}
	
	private static final GrammarSequenceTag instance = new GrammarSequenceTag();
	protected static GrammarSequenceTag getInstance(){
		return instance;
	}
	
	protected static final VisreedTag[] LSEQ_CHILD = {
		JavaCCTag.LEXICAL_ALTERNATION,
		TERMINAL,
		JavaCCTag.LINK,
		CHARACTER_LIST,
		JavaCCTag.LEXICAL_KLEENE_PLUS,
		JavaCCTag.LEXICAL_KLEENE_STAR,
		JavaCCTag.LEXICAL_OPTIONAL,
		JavaCCTag.LEXICAL_REPEAT_RANGE
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
