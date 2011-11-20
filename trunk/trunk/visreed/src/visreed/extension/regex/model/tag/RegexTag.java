/**
 * RegexTag.java
 * 
 * @date: Nov 14, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.regex.model.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.model.VisreedNode;
import visreed.model.VisreedTag;
import visreed.model.VisreedWholeGraph;

/**
 * @author Xiaoyu Guo
 *
 */
public abstract class RegexTag extends VisreedTag {
	public RegexTag(TagCategory category) {
		super(category);
	}

	public static final VisreedTag SEQUENCE 		= SequenceTag.getInstance();
    public static final VisreedTag ALTERNATION 		= AlternationTag.getInstance();
    public static final VisreedTag KLEENE_PLUS 		= KleenePlusTag.getInstance();
    public static final VisreedTag KLEENE_STAR 		= KleeneStarTag.getInstance();
    public static final VisreedTag TERMINAL 		= TerminalTag.getInstance();
    public static final VisreedTag OPTIONAL			= OptionalTag.getInstance();
    public static final VisreedTag REPEAT_RANGE 	= RepeatRangeTag.getInstance();
    public static final VisreedTag CHARACTER_LIST 	= CharacterListTag.getInstance();
    
    private static final VisreedTag[] VALUES = new VisreedTag[]{
        SEQUENCE,
        ALTERNATION,
        KLEENE_PLUS,
        KLEENE_STAR,
        TERMINAL,
        OPTIONAL,
        REPEAT_RANGE,
        CHARACTER_LIST
    };
    
    /**
     * Gets all the possible values
     * @return
     */
    public static VisreedTag[] values(){
        return VALUES;
    }
    
    /* Frequently used children tags */
    protected static final VisreedTag[] CHILD_TAG_NON_SEQ = {SEQUENCE};
    protected static final VisreedTag[] CHILD_TAG_TER = {};
    protected static final TagCategory[] CHILD_CATE_NON_SEQ = {TagCategory.SEQ};
    
    /**
     * A simplified check for contentModel()
     * @return The array containing all the direct child type
     */
    protected VisreedTag[] getAcceptableChildTags(){
    	return CHILD_TAG_NON_SEQ;
    }
    
    /* (non-Javadoc)
     * @see higraph.model.taggedInterfaces.Tag#defaultTagSequence()
     */
    @Override
    public List<VisreedTag> defaultTagSequence() {
        ArrayList<VisreedTag> seq = new ArrayList<VisreedTag>(1);
        seq.add(SEQUENCE);
        return seq;
    }
    
    /* (non-Javadoc)
     * @see visreed.model.VisreedTag#createSEQ(visreed.model.VisreedWholeGraph)
     */
    @Override
    protected VisreedNode createSEQ(VisreedWholeGraph wholeGraph){
    	return wholeGraph.makeRootNode(SEQUENCE);
    }
}
