/**
 * GrammarTerminalTag.java
 * 
 * @date: Nov 14, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.model.VisreedPayload;
import visreed.model.payload.TerminalPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class GrammarTerminalTag extends GrammarTag {
	protected GrammarTerminalTag() {
		super(TagCategory.TERMINAL);
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
		return "GTER";
	}

	private static final GrammarTerminalTag instance = new GrammarTerminalTag();
	protected static JavaCCTag getInstance(){
		return instance;
	}
}
