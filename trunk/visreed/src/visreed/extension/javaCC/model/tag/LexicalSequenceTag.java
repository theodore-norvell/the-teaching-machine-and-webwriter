/**
 * LexicalSequenceTag.java
 * 
 * @date: Nov 2, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.model.VisreedPayload;
import visreed.model.VisreedTag;
import visreed.model.payload.SequencePayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class LexicalSequenceTag extends LexicalTag {
	protected LexicalSequenceTag(){
		super(TagCategory.SEQ);
	}
	private static final LexicalSequenceTag instance = new LexicalSequenceTag();
	protected static LexicalSequenceTag getInstance(){
		return instance;
	}
	
	protected static final VisreedTag[] LSEQ_CHILD = {
//		LEXICAL_ALTERNATION,
//		LEXICAL_TERMINAL,
//		LEXICAL_LINK,
//		CHARACTER_LIST,
//		LEXICAL_KLEENE_STAR,
//		LEXICAL_KLEENE_PLUS,
//		LEXICAL_OPTIONAL,
//		LEXICAL_REPEAT_RANGE
		
		LexicalAlternationTag.getInstance(),
		LexicalTerminalTag.getInstance(),
		LexicalLinkTag.getInstance(),
		CharacterListTag.getInstance(),
		LexicalKleeneStarTag.getInstance(),
		LexicalKleenePlusTag.getInstance(),
		LexicalOptionalTag.getInstance(),
		LexicalRepeatRangeTag.getInstance(),
		RegexpSpecTag.getInstance()
	};
	
	@Override
	protected boolean contentModelHook(final List<VisreedTag> childTags){
    	boolean result = true;
    	for(VisreedTag t : childTags){
    		if(!t.isOneOf(LSEQ_CHILD)){
    			result = false;
    			break;
    		}
    	}
    	return result;
    }

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new SequencePayload(this);
	}
	
	@Override
	public List<VisreedTag> defaultTagSequence(){
		return new ArrayList<VisreedTag>(0);
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "LSEQ";
	}
}
