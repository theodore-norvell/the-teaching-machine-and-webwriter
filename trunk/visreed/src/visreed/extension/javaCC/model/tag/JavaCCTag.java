/**
 * JavaCCTag.java
 * 
 * @date: Aug 28, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.model.VisreedTag;

/**
 * This is the common definition for all Tags in JavaCC extension. 
 * @author Xiaoyu Guo
 */
public abstract class JavaCCTag extends VisreedTag {
	protected Field field = Field.GENERAL;
	
	protected JavaCCTag(TagCategory category, Field field){
		super(category);
		this.field = field;
	}
	
	protected enum Field{
		LEXICAL, GRAMMATICAL, GENERAL, OTHER
	}
	
	/* JavaCC specific tags */
    public static final VisreedTag ROOT 				= RootTag.getInstance();
    public static final VisreedTag REGEXP_SPEC			= RegexpSpecTag.getInstance();
    public static final VisreedTag LOOKAHEAD 			= LookAheadTag.getInstance();
    public static final VisreedTag JAVA_CODE_PRODUCTION = JavaCodeProductionTag.getInstance();
    
    /* Lexical Tags are used in <> */
    public static final VisreedTag LEXICAL_SEQUENCE 	= LexicalSequenceTag.getInstance();
    public static final VisreedTag LEXICAL_ALTERNATION 	= LexicalAlternationTag.getInstance();
    public static final VisreedTag LEXICAL_KLEENE_PLUS 	= LexicalKleenePlusTag.getInstance();
    public static final VisreedTag LEXICAL_KLEENE_STAR 	= LexicalKleeneStarTag.getInstance();
    public static final VisreedTag LEXICAL_OPTIONAL 	= LexicalOptionalTag.getInstance();
    public static final VisreedTag LEXICAL_REPEAT_RANGE = LexicalRepeatRangeTag.getInstance();
    public static final VisreedTag LEXICAL_LINK 		= LexicalLinkTag.getInstance();
    public static final VisreedTag LEXICAL_TERMINAL		= LexicalTerminalTag.getInstance();
    public static final VisreedTag CHARACTER_LIST		= CharacterListTag.getInstance();
    public static final VisreedTag REGULAR_PRODUCTION 	= RegularExpressionProductionTag.getInstance();
    
    /* Gramma(tical)r Tags are used in bnf productions */
    public static final VisreedTag GRAMMAR_SEQUENCE 	= GrammarSequenceTag.getInstance();
    public static final VisreedTag GRAMMAR_ALTERNATION 	= GrammarAlternationTag.getInstance();
    public static final VisreedTag GRAMMAR_KLEENE_PLUS 	= GrammarKleenePlusTag.getInstance();
    public static final VisreedTag GRAMMAR_KLEENE_STAR 	= GrammarKleeneStarTag.getInstance();
    public static final VisreedTag GRAMMAR_OPTIONAL 	= GrammarOptionalTag.getInstance();
    public static final VisreedTag GRAMMAR_REPEAT_RANGE = GrammarRepeatRangeTag.getInstance();
    public static final VisreedTag GRAMMAR_LINK 		= GrammarLinkTag.getInstance();
    public static final VisreedTag GRAMMAR_TERMINAL		= GrammarTerminalTag.getInstance();
    public static final VisreedTag BNF_PRODUCTION 		= BNFProductionTag.getInstance();
   
    private static final VisreedTag[] VALUES = new VisreedTag[]{
        // lexical tags
        LEXICAL_SEQUENCE, 	
        LEXICAL_ALTERNATION, 
        LEXICAL_KLEENE_PLUS, 
        LEXICAL_KLEENE_STAR, 
        LEXICAL_OPTIONAL, 	
        LEXICAL_REPEAT_RANGE,
        LEXICAL_LINK,
        LEXICAL_TERMINAL,
        CHARACTER_LIST,
        REGULAR_PRODUCTION,
        REGEXP_SPEC,

        // grammatical tags
        GRAMMAR_SEQUENCE, 	
        GRAMMAR_ALTERNATION, 
        GRAMMAR_KLEENE_PLUS, 
        GRAMMAR_KLEENE_STAR, 
        GRAMMAR_OPTIONAL, 	
        GRAMMAR_REPEAT_RANGE,
        GRAMMAR_LINK,
        GRAMMAR_TERMINAL,
        BNF_PRODUCTION,
        LOOKAHEAD,

        // general tags
    	ROOT,
        JAVA_CODE_PRODUCTION
    };
    
    /**
     * Gets all values as an enumeration
     * @return
     */
    public static VisreedTag[] values(){
        return VALUES;
    }
    
}
