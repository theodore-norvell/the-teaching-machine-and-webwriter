/**
 * LexicalAlternationTag.java
 * 
 * @date: Nov 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.extension.javaCC.model.payload.LexicalAlternationPayload;
import visreed.model.VisreedPayload;
import visreed.model.VisreedTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class LexicalAlternationTag extends LexicalTag {
	protected LexicalAlternationTag() {
		super(TagCategory.ALT);
	}
	private static final LexicalAlternationTag instance = new LexicalAlternationTag();
	protected static LexicalAlternationTag getInstance(){
		return instance;
	}
	
	@Override
	protected boolean contentModelHook(final List<VisreedTag> childTags){
		boolean result = true;

		for(VisreedTag t : childTags){
			if(!t.equals(LEXICAL_SEQUENCE)){
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
		return new LexicalAlternationPayload();
	}
	
	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultTagSequence()
	 */
	@Override
	public List<VisreedTag> defaultTagSequence() {
		List<VisreedTag> result = new ArrayList<VisreedTag>(2);
		result.add(LEXICAL_SEQUENCE);
		result.add(LEXICAL_SEQUENCE);
		return result;
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "LALT";
	}
}
