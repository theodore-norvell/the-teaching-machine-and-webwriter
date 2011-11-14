/**
 * GrammarAlternationTag.java
 * 
 * @date: Nov 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import java.util.List;

import visreed.model.payload.AlternationPayload;
import visreed.model.payload.VisreedPayload;
import visreed.model.tag.AlternationTag;
import visreed.model.tag.VisreedTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class GrammarAlternationTag extends AlternationTag {
	protected GrammarAlternationTag() {
		super();
	}
	private static final GrammarAlternationTag instance = new GrammarAlternationTag();
	protected static GrammarAlternationTag getInstance(){
		return instance;
	}
    
    @Override
    public boolean canHoldExactOneChild(){
    	return false;
    }

    @Override
	protected boolean contentModelHook(List<VisreedTag> childTags){
		return childTags.size() >= 2;
	}
	
	@Override
	protected VisreedTag[] getAcceptableChildTags(){
		return JavaCCTag.CHILD_TAG_NON_SEQ_GRA;
	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new AlternationPayload();
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "GALT";
	}
}
