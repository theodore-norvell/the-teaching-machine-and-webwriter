/**
 * VisreedTag.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model;

import higraph.model.taggedInterfaces.Tag;

import java.util.ArrayList;
import java.util.List;

import visreed.view.PaintParameter;


/**
 * The VisreedTag provides an extensible content model checking mechanism on
 * sequential normal form. <br />
 * Basic idea: 
 * <ul>
 * 	<li>The common sequential normal form is maintained by "Category"</li>
 * 	<li>Extension-specific structures are maintained by contentModelHook(), 
 * 		which is done by providing a set of "acceptable" tags. 
 * 	</li>
 *  <li>All concrete tags should be in extension level.</li>
 * </ul>
 * @author Xiaoyu Guo
 */
public abstract class VisreedTag 
implements Tag<VisreedTag, VisreedPayload>, IDescribable{
	
	protected PaintParameter paintParameter = null;
	
	/**
	 * Gets the override paint parameter for node views
	 * @return
	 */
	public PaintParameter getPaintParameter(){
		return paintParameter;
	}
    
    /**
     * Defines the tag category. <br />
     * The tag category is used in maintaining the sequential normal form. 
     */
    public enum TagCategory{
    	/** Alternation */
    	ALT,
    	/** Sequence */
    	SEQ,
    	/** Node types with only one SEQ child. (e.g. Repetitions) */
    	SINGLE_SEQ_CHILD,
    	/** Contains no children */
    	TERMINAL,
    	/** Other, should override contentModelHook() */
    	OTHER
    }
    
    protected TagCategory category;
    
    protected VisreedTag(){
    	this.category = TagCategory.SINGLE_SEQ_CHILD;
    }
    
    protected VisreedTag(TagCategory category){
    	this.category = category;
    }
    
    /**
     * Indicates whether the tag can hold only one child. <br />
     * For most non-sequence tags this value should be true.<br />
     * For other tags this value should be false. <br />
     * <br />
     * Extension-specific tags may want to override this, if their category
     * belongs to OTHER or has a different model with the default one.
     * @return
     */
    public boolean canHoldExactOneChild(){
    	switch(category){
    	case SINGLE_SEQ_CHILD:
    		return true;
    	case ALT:
    	case SEQ:
    	case TERMINAL:
		default:
			return false;
    	}
    }
    
    /* (non-Javadoc)
     * @see higraph.model.taggedInterfaces.Tag#contentModel(java.util.List)
     */
    /**
     * This performs check for sequential normal form.
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
    	// category check
    	switch(category){
    	case SEQ:
    		// sequence nodes can not have sequence children
    		for(VisreedTag t : childTags){
    			if(t.category == TagCategory.SEQ){
    				result = false;
    				break;
    			}
    		}
    		break;
    	case SINGLE_SEQ_CHILD:
    		// only one SEQ children
    		result = (childTags.size() == 1);
    		result &= (childTags.get(0).isCategory(TagCategory.SEQ));
    		break;
    	case ALT:
    		// two or more SEQ children
    		result = (childTags.size() >= 2);
    		for(VisreedTag t : childTags){
    			if(!t.isCategory(TagCategory.SEQ)){
    				result = false;
    				break;
    			}
    		}
    		break;
    	case TERMINAL:
    		// terminal nodes does not allow children
    		result = (childTags.size() == 0);
    		break;
    	case OTHER:
		default:
			// OTHER should overwrite contentModel() or contentModelHook()
			// no need to check here.
			break;
    	}
    	
    	result &= contentModelHook(childTags);
    	return result;
    }
    
    /**
     * Give a quick hint for content model check
     * This is checked before an operation is performed on the UI,
     * e.g. in a drop operation, if the structure can be maintained by the tag system
     * itself, then looseContentModel() should return {@value true}.  
     * @param childTags
     * @return {@value true} If the operation is allowed, {@value false} otherwise.
     */
    public boolean looseContentModel(List<VisreedTag> childTags){
    	return true;
    }
    
    /**
     * Additional check for contentModel(), reserved for extensions. <br />
     * Suggested implementation is using isOneOf().
     * @param childTags the tags of children
     * @return {@value true} if the content model is acceptable. 
     */
    protected boolean contentModelHook(final List<VisreedTag> childTags){
    	return true;
    }
    
    /**
     * Category check
     * @param category
     * @return
     */
    public final boolean isCategory(final TagCategory category){
    	if(category == null){
    		return false;
    	}
    	return this.category.equals(category);
    }
    
    /**
     * Category check
     * @param categories
     * @return
     */
    protected boolean isInCategory(final TagCategory[] categories){
    	if(categories == null){
    		return false;
    	}
    	for(int i = 0; i < categories.length; i++){
    		if(this.category.equals(categories[i])){
    			return true;
    		}
    	}
    	return false;
    }
   
    /**
     * Type check using reflection
     * @param acceptableTags
     * @return
     */
    @Deprecated
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
     * Type check using equals()
     * @param acceptableTags
     * @return
     */
    public boolean isOneOf(VisreedTag[] acceptableTags){
    	if(acceptableTags == null){
    		return false;
    	}
    	for(int i = 0; i < acceptableTags.length; i++){
    		if(this.equals(acceptableTags[i])){
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Type check.
     * Note: this method uses reflection. To avoid performance loss, try 
     * equals() if inheritance is not needed. 
     * @param object
     * @return
     */
    @Deprecated
    public boolean is(VisreedTag object){
    	if(object == null){
    		return false;
    	}
		return object.getClass().isAssignableFrom(this.getClass());
    }
    
    /**
     * Insertion check at tag level, at an early stage.<br />
     * Instead of the contentModel(), this operation will give the user 
     * a hint of possible operation. The actual insertion may require 
     * some additional steps (such as creating SEQs). 
     * @param children tags to insert
     * @param position the position
     * @return 
     */
    public boolean canInsert(List<VisreedTag> children, int position){
    	return true;
    }
    
    /**
     * This is used to pre-process the nodes in insertion. <br />
     * Some extra steps may be needed to maintain the sequential normal form. 
     * e.g. adding extra SEQ. This is where these operations are done. 
     * By default this does nothing. <br />
     * @param parent
     * @param children
     * @param position
     * @return a List contains several nodes, which can be inserted to the parent directly.
     */
    public NodeInsertionParameter processInsertion(
		VisreedNode parent, 
		List<VisreedNode> children, 
		int position
	){
    	List<VisreedNode> newChildren = new ArrayList<VisreedNode>();
    	
		VisreedNode insertionTarget = parent;
		if(parent.getTag().category.equals(TagCategory.SINGLE_SEQ_CHILD)){
			insertionTarget = parent.getChild(0);
		}
		boolean parentIsSeq = insertionTarget.getTag().category.equals(TagCategory.SEQ);
		
		for(VisreedNode child : children){
			boolean childIsSeq = child.getTag().category.equals(TagCategory.SEQ);
			if(parentIsSeq){
				if(childIsSeq){
					// extract all children from child
					while(child.getNumberOfChildren() > 0){
						VisreedNode kidkid = child.getChild(0);
						kidkid.detach();
						newChildren.add(kidkid);
					}
				} else {
					newChildren.add(child);
				}
			} else {
				if(childIsSeq){
					newChildren.add(child);
				} else {
					// generate extra SEQ for each children
					// example: ALT <- KLN+
					VisreedNode seq = this.createSEQ(parent.getWholeGraph());
					seq.appendChild(child);
					newChildren.add(seq);
				}
			}
		}
    	
    	NodeInsertionParameter result = new NodeInsertionParameter(
			insertionTarget, 
			newChildren
		);
    	return result;
    }

	/**
	 * @param wholeGraph
	 * @return
	 */
	protected abstract VisreedNode createSEQ(VisreedWholeGraph wholeGraph);
}
