/**
 * VisreedTag.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model.tag;

import higraph.model.taggedInterfaces.Tag;

import java.util.ArrayList;
import java.util.List;

import visreed.model.IDescribable;
import visreed.model.payload.VisreedPayload;

/**
 * @author Xiaoyu Guo
 */
public abstract class VisreedTag 
implements Tag<VisreedTag, VisreedPayload>, IDescribable{
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
    
    /**
     * Indicates whether the tag can hold only one child. <br />
     * For most non-sequence tags this value should be true.<br />
     * For other tags this value should be false.
     * @return
     */
    public boolean canHoldExactOneChild(){
    	return true;
    }
    
    /* (non-Javadoc)
     * @see higraph.model.taggedInterfaces.Tag#contentModel(java.util.List)
     */
    @Override
    public boolean contentModel(List<VisreedTag> childTags){
    	/*
    	 * A simplified check is provided to all derived tags.
    	 * Usually a derived tag should overwrite getAcceptableChildTags()
    	 * and contentModelHook() for simplified check. <br />
    	 * <br />
    	 * The derived tag can also overwrite the contentModel() completely.
    	 */
    	if(childTags == null){
    		return false;
    	}
    	
    	boolean result = true;
    	for(int i = 0; i < childTags.size(); i++){
    		if(!childTags.get(i).isIn(getAcceptableChildTags())){
    			result = false;
    			break;
    		}
    	}
    	result &= contentModelHook(childTags);
    	return result;
    }
    
    /**
     * Type check
     * @param acceptableTags
     * @return
     */
    protected boolean isIn(final VisreedTag[] acceptableTags){
    	if(acceptableTags == null){
    		return false;
    	}
    	for(int i = 0; i < acceptableTags.length; i++){
    		if(this.is(acceptableTags[i])){
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Type check
     * @param object
     * @return
     */
    public boolean is(VisreedTag object){
    	if(object == null){
    		return false;
    	}
		return object.getClass().isAssignableFrom(this.getClass());
    }
    
    /* Frequently used children tags */
    protected static final VisreedTag[] CHILD_TAG_NON_SEQ = {SEQUENCE};
    protected static final VisreedTag[] CHILD_TAG_TER = {};
    
    /**
     * A simplified check for contentModel()
     * @return The array containing all the direct child type
     */
    protected VisreedTag[] getAcceptableChildTags(){
    	return CHILD_TAG_NON_SEQ;
    }
   
    /**
     * Additional check for contentModel()
     * @param childTags the tags of children
     * @return {@value true} if the content model is acceptable. 
     */
    protected boolean contentModelHook(final List<VisreedTag> childTags){
    	return childTags.size() == 1;
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
}
