/**
 * JavaCCLinkTag.java
 * 
 * @date: Oct 12, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.extension.javaCC.model.payload.JavaCCLinkPayload;
import visreed.model.payload.VisreedPayload;
import visreed.model.tag.TerminalTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class JavaCCLinkTag extends TerminalTag {

	protected JavaCCLinkTag() {
	}
	
    @Override
    public boolean equals(Object o){
        return (o instanceof JavaCCLinkTag);
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
		return new JavaCCLinkPayload("");
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "LNK";
	}

    private static final JavaCCLinkTag instance = new JavaCCLinkTag();
    
    public static JavaCCLinkTag getInstance(){
        return instance;
    }
}
