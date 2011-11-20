/**
 * RootTag.java
 * 
 * @date: Sep 12, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.extension.javaCC.model.payload.JavaCCRootPayload;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedTag;
import visreed.model.VisreedWholeGraph;

/**
 * @author Xiaoyu Guo
 *
 */
public class RootTag extends JavaCCTag {

	protected RootTag() {
		super(TagCategory.SEQ, Field.GENERAL);
	}

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
    
    protected static final RootTag getInstance(){
        return instance;
    }

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultTagSequence()
	 */
	@Override
	public List<VisreedTag> defaultTagSequence() {
		return new ArrayList<VisreedTag>(0);
	}

	/* (non-Javadoc)
	 * @see visreed.model.VisreedTag#createSEQ(visreed.model.VisreedWholeGraph)
	 */
	@Override
	protected VisreedNode createSEQ(VisreedWholeGraph wholeGraph) {
		return null;
	}
}
