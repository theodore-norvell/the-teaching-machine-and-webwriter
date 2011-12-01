/**
 * GrammarSequenceTag.java
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
public class GrammarSequenceTag extends GrammarTag {
	protected GrammarSequenceTag(){
		super(TagCategory.SEQ);
	}
	
	private static final GrammarSequenceTag instance = new GrammarSequenceTag();
	protected static GrammarSequenceTag getInstance(){
		return instance;
	}
	
	protected static final VisreedTag[] GSEQ_CHILD = {
//		GRAMMAR_ALTERNATION,
//		LOOKAHEAD,
//		GRAMMAR_TERMINAL,
//		GRAMMAR_OPTIONAL,
//		GRAMMAR_KLEENE_PLUS,
//		GRAMMAR_KLEENE_STAR,
//		GRAMMAR_LINK,
//		LEXICAL_LINK,
//		LEXICAL_TERMINAL,
//		LEXICAL_ALTERNATION,
//		LEXICAL_KLEENE_STAR,
//		LEXICAL_KLEENE_PLUS,
//		LEXICAL_OPTIONAL,
//		LEXICAL_REPEAT_RANGE,
//		CHARACTER_LIST,
		
		GrammarAlternationTag.getInstance(),
		LookAheadTag.getInstance(),
		GrammarTerminalTag.getInstance(),
		GrammarOptionalTag.getInstance(),
		GrammarKleenePlusTag.getInstance(),
		GrammarKleeneStarTag.getInstance(),
		GrammarLinkTag.getInstance(),
		LexicalLinkTag.getInstance(),
		LexicalTerminalTag.getInstance(),
		LexicalAlternationTag.getInstance(),
		LexicalKleeneStarTag.getInstance(),
		LexicalKleenePlusTag.getInstance(),
		LexicalOptionalTag.getInstance(),
		LexicalRepeatRangeTag.getInstance(),
		CharacterListTag.getInstance()
	};
	
	@Override
	protected boolean contentModelHook(final List<VisreedTag> childTags){
    	boolean result = true;
    	for(VisreedTag t : childTags){
    		if(!t.isOneOf(GSEQ_CHILD)){
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
		return "GSEQ";
	}
}
