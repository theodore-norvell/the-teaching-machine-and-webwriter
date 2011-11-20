/**
 * LexicalTag.java
 * 
 * @date: Nov 14, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.model.VisreedNode;
import visreed.model.VisreedTag;
import visreed.model.VisreedWholeGraph;

/**
 * @author Xiaoyu Guo
 *
 */
public abstract class LexicalTag extends JavaCCTag {
	protected LexicalTag(TagCategory category) {
		super(category, Field.LEXICAL);
	}

	/* (non-Javadoc)
	 * @see visreed.model.VisreedTag#createSEQ(visreed.model.VisreedWholeGraph)
	 */
	@Override
	protected VisreedNode createSEQ(VisreedWholeGraph wholeGraph) {
		return wholeGraph.makeRootNode(LEXICAL_SEQUENCE);
	}
    
    /* (non-Javadoc)
     * @see higraph.model.taggedInterfaces.Tag#defaultTagSequence()
     */
    @Override
    public List<VisreedTag> defaultTagSequence() {
        ArrayList<VisreedTag> seq = new ArrayList<VisreedTag>(1);
        seq.add(LEXICAL_SEQUENCE);
        return seq;
    }
    
	/* (non-Javadoc)
	 * @see visreed.model.VisreedTag#contentModelHook(java.util.List)
	 */
	@Override
	protected boolean contentModelHook(final List<VisreedTag> childTags){
		boolean result = childTags.get(0).equals(LEXICAL_SEQUENCE);
		return result;
	}

    /** Common child tags for Lexical non-SEQs */
    protected static final VisreedTag[] CHILD_TAG_NON_SEQ_LEX = {LEXICAL_SEQUENCE};
    
}
