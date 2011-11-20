/**
 * LexcialTerminalTag.java
 * 
 * @date: Nov 14, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.model.VisreedPayload;
import visreed.model.VisreedTag;
import visreed.model.payload.TerminalPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class LexicalTerminalTag extends LexicalTag {
	protected LexicalTerminalTag() {
		super(TagCategory.TERMINAL);
	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultTagSequence()
	 */
	@Override
	public List<VisreedTag> defaultTagSequence() {
		return new ArrayList<VisreedTag>(0);
	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new TerminalPayload(this);
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "LTER";
	}

	private static final LexicalTerminalTag instance = new LexicalTerminalTag();
	protected static JavaCCTag getInstance(){
		return instance;
	}
}
