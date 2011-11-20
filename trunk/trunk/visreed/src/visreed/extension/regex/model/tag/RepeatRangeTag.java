/**
 * RepeatRangeTag.java
 * 
 * @date: Oct 3, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.regex.model.tag;

import visreed.model.VisreedPayload;
import visreed.model.payload.RepeatRangePayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class RepeatRangeTag extends RegexTag {
	protected RepeatRangeTag(){
		super(TagCategory.SINGLE_SEQ_CHILD);
	}
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o){
        return (o instanceof RepeatRangeTag);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
    	return "Range";
    }

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new RepeatRangePayload(RegexTag.REPEAT_RANGE);
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "RNG";
	}
    
    private static final RepeatRangeTag instance = new RepeatRangeTag();
    
    protected static RepeatRangeTag getInstance(){
        return instance;
    }

}
