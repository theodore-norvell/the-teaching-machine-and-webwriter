/**
 * RootTag.java
 * 
 * @date: Sep 12, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.extension.javaCC.model.payload.JavaCCRootPayload;
import visreed.model.payload.VisreedPayload;
import visreed.model.tag.SequenceTag;
import visreed.model.tag.VisreedTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class RootTag extends SequenceTag {

	protected RootTag() {
		super();
	}
	
	/* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o){
    	if(o.equals(VisreedTag.SEQUENCE)){
    		return true;
    	}
        return (o instanceof RootTag);
    }

//	/* (non-Javadoc)
//	 * @see higraph.model.taggedInterfaces.Tag#contentModel(java.util.List)
//	 */
//	@Override
//	public boolean contentModel(List<VisreedTag> seq) {
//		// Root nodes must have exactly 1 sequence as child
//        if(seq != null && seq.size() == 1 && seq.get(0).equals(SEQUENCE)){
//            return true;
//        }
//        else{
//            return false;
//        }
//	}
//
//	/* (non-Javadoc)
//	 * @see higraph.model.taggedInterfaces.Tag#defaultTagSequence()
//	 */
//	@Override
//	public List<VisreedTag> defaultTagSequence() {
//        ArrayList<VisreedTag> seq = new ArrayList<VisreedTag>(1);
//        seq.add(SEQUENCE);
//        return seq;
//	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new JavaCCRootPayload(null);
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "ROOT";
	}
	
    private static final RootTag instance = new RootTag();
    
    public static final RootTag getInstance(){
        return instance;
    }
}
