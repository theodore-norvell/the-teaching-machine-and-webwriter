/**
 * GrammarTag.java
 * 
 * @date: Nov 14, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import visreed.model.VisreedNode;
import visreed.model.VisreedTag;
import visreed.model.VisreedWholeGraph;
import visreed.view.PaintParameter;

/**
 * @author Xiaoyu Guo
 *
 */
public abstract class GrammarTag extends JavaCCTag {
	protected static final Color BACK_COLOR = new Color(248, 207, 42);
	
	protected GrammarTag(TagCategory category) {
		super(category, Field.GRAMMATICAL);
		this.paintParameter = new PaintParameter();
		this.paintParameter.setBackColor(BACK_COLOR);
	}
	
	/* (non-Javadoc)
	 * @see visreed.model.VisreedTag#createSEQ(visreed.model.VisreedWholeGraph)
	 */
	@Override
	protected VisreedNode createSEQ(VisreedWholeGraph wholeGraph) {
		return wholeGraph.makeRootNode(GRAMMAR_SEQUENCE);
	}
    
    /* (non-Javadoc)
     * @see higraph.model.taggedInterfaces.Tag#defaultTagSequence()
     */
    @Override
    public List<VisreedTag> defaultTagSequence() {
        ArrayList<VisreedTag> seq = new ArrayList<VisreedTag>(1);
        seq.add(GRAMMAR_SEQUENCE);
        return seq;
    }
    
	/* (non-Javadoc)
	 * @see visreed.model.VisreedTag#contentModelHook(java.util.List)
	 */
	@Override
	protected boolean contentModelHook(final List<VisreedTag> childTags){
		boolean result = childTags.get(0).equals(GRAMMAR_SEQUENCE);
		return result;
	}

    /** Common child tags for Grammatical non-SEQs */
    protected static final VisreedTag[] CHILD_TAG_NON_SEQ_GRA = {GRAMMAR_SEQUENCE};
}
