/**
 * GrammarAlternationTag.java
 * 
 * @date: Nov 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.model.VisreedPayload;
import visreed.model.VisreedTag;
import visreed.model.payload.AlternationPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class GrammarAlternationTag extends GrammarTag {
	protected GrammarAlternationTag() {
		super(TagCategory.ALT);
	}
	private static final GrammarAlternationTag instance = new GrammarAlternationTag();
	protected static GrammarAlternationTag getInstance(){
		return instance;
	}
	
	@Override
	protected boolean contentModelHook(final List<VisreedTag> childTags){
		boolean result = true;

		for(VisreedTag t : childTags){
			if(!t.equals(GRAMMAR_SEQUENCE)){
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
		return new AlternationPayload(this);
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "GALT";
	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultTagSequence()
	 */
	@Override
	public List<VisreedTag> defaultTagSequence() {
		List<VisreedTag> result = new ArrayList<VisreedTag>(2);
		result.add(GRAMMAR_SEQUENCE);
		result.add(GRAMMAR_SEQUENCE);
		return result;
	}


}
