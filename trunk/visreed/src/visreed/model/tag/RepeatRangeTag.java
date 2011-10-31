/**
 * RepeatRangeTag.java
 * 
 * @date: Oct 3, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.model.payload.RepeatRangePayload;
import visreed.model.payload.VisreedPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class RepeatRangeTag extends VisreedTag {
	RepeatRangeTag(){
		super();
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
	 * @see higraph.model.taggedInterfaces.Tag#contentModel(java.util.List)
	 */
	@Override
	public boolean contentModel(List<VisreedTag> seq) {
        // RepeatRange nodes must have exactly 1 sequence as child
        if(seq != null && seq.size() == 1 && seq.get(0).equals(SEQUENCE)){
            return true;
        }
        else{
            return false;
        }
	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultTagSequence()
	 */
	@Override
	public List<VisreedTag> defaultTagSequence() {
		ArrayList<VisreedTag> seq = new ArrayList<VisreedTag>(1);
		seq.add(SEQUENCE);
		return seq;
	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new RepeatRangePayload();
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "RNG";
	}
    
    private static final RepeatRangeTag instance = new RepeatRangeTag();
    
    public static final RepeatRangeTag getInstance(){
        return instance;
    }

}
