/**
 * JavaCCTag.java
 * 
 * @date: Aug 28, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.model.tag.VisreedTag;

/**
 * This is the common definition for all Tags in JavaCC extension. 
 * @author Xiaoyu Guo
 */
public abstract class JavaCCTag extends VisreedTag {
	/* JavaCC specific tags */
    public static final VisreedTag PRODUCTION 			= ProductionTag.getInstance();
    public static final VisreedTag ROOT 				= RootTag.getInstance();
    public static final VisreedTag LINK 				= JavaCCLinkTag.getInstance();
    public static final VisreedTag REGEXP_SPEC			= RegexpSpecTag.getInstance();
    public static final VisreedTag LOOKAHEAD 			= LookAheadTag.getInstance();
    
    /* Lexical Tags are used in <> */
    public static final VisreedTag LEXICAL_SEQUENCE 	= LexicalSequenceTag.getInstance();
    public static final VisreedTag LEXICAL_ALTERNATION 	= LexicalAlternationTag.getInstance();
    public static final VisreedTag LEXICAL_KLEENE_PLUS 	= LexicalKleenePlusTag.getInstance();
    public static final VisreedTag LEXICAL_KLEENE_STAR 	= LexicalKleeneStarTag.getInstance();
    public static final VisreedTag LEXICAL_OPTIONAL 	= LexicalOptionalTag.getInstance();
    public static final VisreedTag LEXICAL_REPEAT_RANGE = LexicalRepeatRangeTag.getInstance();
    
    /* Gramma(tical)r Tags are used in bnf productions */
    public static final VisreedTag GRAMMAR_SEQUENCE 	= GrammarSequenceTag.getInstance();
    public static final VisreedTag GRAMMAR_ALTERNATION 	= GrammarAlternationTag.getInstance();
    public static final VisreedTag GRAMMAR_KLEENE_PLUS 	= GrammarKleenePlusTag.getInstance();
    public static final VisreedTag GRAMMAR_KLEENE_STAR 	= GrammarKleeneStarTag.getInstance();
    public static final VisreedTag GRAMMAR_OPTIONAL 	= GrammarOptionalTag.getInstance();
    public static final VisreedTag GRAMMAR_REPEAT_RANGE = GrammarRepeatRangeTag.getInstance();
    
    private static final VisreedTag[] VALUES = new VisreedTag[]{
    	// commonly used inheriented values
        TERMINAL,
        
        // lexical tags
        LEXICAL_SEQUENCE, 	
        LEXICAL_ALTERNATION, 
        LEXICAL_KLEENE_PLUS, 
        LEXICAL_KLEENE_STAR, 
        LEXICAL_OPTIONAL, 	
        LEXICAL_REPEAT_RANGE,

        // grammatical tags
        GRAMMAR_SEQUENCE, 	
        GRAMMAR_ALTERNATION, 
        GRAMMAR_KLEENE_PLUS, 
        GRAMMAR_KLEENE_STAR, 
        GRAMMAR_OPTIONAL, 	
        GRAMMAR_REPEAT_RANGE,

        // javaCC tags
    	ROOT,
        PRODUCTION,
        LINK,
        REGEXP_SPEC,
        LOOKAHEAD
    };
    
    /**
     * Gets all values as an enumeration
     * @return
     */
    public static VisreedTag[] values(){
        return VALUES;
    }
    
    /** Common child tags for Lexical non-SEQs */
    protected static final VisreedTag[] CHILD_TAG_NON_SEQ_LEX = {LEXICAL_SEQUENCE};
    
    /** Common child tags for Grammatical non-SEQs */
    protected static final VisreedTag[] CHILD_TAG_NON_SEQ_GRA = {GRAMMAR_SEQUENCE};
}
