/**
 * JavaCCLinkTag.java
 * 
 * @date: Oct 12, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.extension.javaCC.model.payload.LexicalLinkPayload;
import visreed.model.VisreedPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class LexicalLinkTag extends LexicalTerminalTag {

	protected LexicalLinkTag() {
	}
	
    @Override
    public boolean equals(Object o){
        return (o instanceof LexicalLinkTag);
    }
    
    @Override
    public String toString(){
        return "LINK";
    }

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new LexicalLinkPayload("");
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "LNK";
	}

    private static final LexicalLinkTag instance = new LexicalLinkTag();
    
    protected static LexicalLinkTag getInstance(){
        return instance;
    }
}
