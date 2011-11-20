/**
 * ProductionTag.java
 * 
 * @date: Aug 22, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.extension.javaCC.model.payload.RegexpProductionPayload;
import visreed.model.VisreedPayload;

/**
 * ProductionTag defines the tag for JavaCC productions.
 * Productions can contain one sequence child.
 * @author Xiaoyu Guo
 */
public class RegularExpressionProductionTag extends LexicalTag{
	protected RegularExpressionProductionTag() {
		super(TagCategory.SINGLE_SEQ_CHILD);
	}

	/* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o){
        return (o instanceof RegularExpressionProductionTag);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return "PRODUCTION";
    }

    /* (non-Javadoc)
     * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
     */
    @Override
    public VisreedPayload defaultPayload() {
        return new RegexpProductionPayload("");
    }

    /* (non-Javadoc)
     * @see visreed.model.IDescribable#getDescription()
     */
    @Override
    public String getDescription() {
        return "PRD";
    }

    private static final RegularExpressionProductionTag instance = new RegularExpressionProductionTag();
    
    protected static final RegularExpressionProductionTag getInstance(){
        return instance;
    }
}
